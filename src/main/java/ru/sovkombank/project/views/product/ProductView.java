package ru.sovkombank.project.views.product;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import ru.sovkombank.project.entities.Category;
import ru.sovkombank.project.entities.Product;
import ru.sovkombank.project.services.CartService;
import ru.sovkombank.project.services.ProductService;
import ru.sovkombank.project.services.UserService;
import ru.sovkombank.project.views.MainLayout;

import java.math.BigDecimal;
import java.util.List;

@Route(layout = MainLayout.class)
public class ProductView extends VerticalLayout {
    private final ProductService productService;
    private final Category category;
    private final UserService userService;
    private final CartService cartService;
    private final Grid<Product> productGrid;

    public ProductView(ProductService productService, Category category, UserService userService, CartService cartService) {
        this.productService = productService;
        this.category = category;
        this.userService = userService;
        this.cartService = cartService;

        productGrid = new Grid<>(Product.class);

        Button addProductButton = new Button("Добавить товар");
        addProductButton.getStyle().set("color", "green");

        addProductButton.addClickListener(e -> showAddProductDialog());

        Grid<Product> productGrid = new Grid<>(Product.class);
        productGrid.setColumns("name", "price", "quantity");
        productGrid.getColumnByKey("name").setHeader("Товар");
        productGrid.getColumnByKey("price").setHeader("Цена");
        productGrid.getColumnByKey("quantity").setHeader("Количество");

        List<Product> productsInCategory = productService.getProductsByCategory(category);
        productGrid.setItems(productsInCategory);

        if (this.userService.getCurrentUser() != null) {
            productGrid.addColumn(new ComponentRenderer<>(product -> {
                Button addToCartButton = new Button("Добавить в корзину");
                addToCartButton.addClickListener(e -> {
                    this.cartService.addToCart(this.userService.getCurrentUser().getId(), product.getId());
                });
                return addToCartButton;
            }));

            productGrid.addColumn(new ComponentRenderer<>(product -> {
                Button deleteButton = new Button("Удалить");
                deleteButton.getStyle().set("color", "red");
                deleteButton.addClickListener(e -> {
                    productService.deleteProductById(product.getId());
                    refreshGrid();
                    UI.getCurrent().getPage().reload();
                });
                return deleteButton;
            }));

            productGrid.addColumn(new ComponentRenderer<>(product -> {
                Button editButton = new Button("Редактировать");
                editButton.addClickListener(e -> {
                    showEditProductDialog(product);
                    refreshGrid();
                });
                return editButton;
            }));
        }

        add(addProductButton, productGrid);
        refreshGrid();
    }

    private void refreshGrid() {
        productGrid.setItems(productService.getAllProducts());
    }

    private void showAddProductDialog() {
        Dialog dialog = new Dialog();
        dialog.setModal(true);

        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField("Название товара");
        TextField priceField = new TextField("Цена");
        TextField quantityField = new TextField("Количество");

        Button saveButton = new Button("Сохранить");
        saveButton.addClickListener(e -> {
            Product product = new Product();
            product.setName(nameField.getValue());
            product.setPrice(new BigDecimal(priceField.getValue()));
            product.setQuantity(Integer.parseInt(quantityField.getValue()));
            product.setCategory(category);
            productService.addNewProduct(product, category);
            refreshGrid();
            UI.getCurrent().getPage().reload();
            dialog.close();
        });

        Button cancelButton = new Button("Отмена");
        cancelButton.addClickListener(e -> dialog.close());

        formLayout.add(nameField, priceField, quantityField);
        dialog.add(formLayout, saveButton, cancelButton);
        dialog.open();
        refreshGrid();
    }

    private void showEditProductDialog(Product product) {
        Dialog dialog = new Dialog();
        dialog.setModal(true);

        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField("Название товара");
        TextField priceField = new TextField("Цена");
        TextField quantityField = new TextField("Количество");

        nameField.setValue(product.getName());
        priceField.setValue(String.valueOf(product.getPrice()));
        quantityField.setValue(String.valueOf(product.getQuantity()));

        Button saveButton = new Button("Сохранить");
        saveButton.addClickListener(e -> {
            product.setName(nameField.getValue());
            product.setPrice(new BigDecimal(priceField.getValue()));
            product.setQuantity(Integer.parseInt(quantityField.getValue()));
            productService.updateProduct(product);
            refreshGrid();
            UI.getCurrent().getPage().reload();
            dialog.close();
        });

        Button cancelButton = new Button("Отмена");
        cancelButton.addClickListener(e -> dialog.close());

        formLayout.add(nameField, priceField, quantityField);
        dialog.add(formLayout, saveButton, cancelButton);
        dialog.open();
    }
}
