package ru.sovkombank.project.views.product;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import ru.sovkombank.project.entities.Category;
import ru.sovkombank.project.entities.Product;
import ru.sovkombank.project.entities.Supplier;
import ru.sovkombank.project.services.CartService;
import ru.sovkombank.project.services.ProductService;
import ru.sovkombank.project.services.SupplierService;
import ru.sovkombank.project.services.UserService;
import ru.sovkombank.project.views.MainLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route(layout = MainLayout.class)
@PermitAll
public class ProductView extends VerticalLayout {
    private final ProductService productService;
    private Category selectedCategory;
    private final UserService userService;
    private final CartService cartService;
    private final Grid<Product> productGrid;
    private final SupplierService supplierService;

    public ProductView(ProductService productService, UserService userService, CartService cartService, SupplierService supplierService) {
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
        this.supplierService = supplierService;

        productGrid = new Grid<>(Product.class);
        configureAddSupplierForm();
        configureProductGrid();

        refreshGrid();
    }

    private void configureAddSupplierForm() {
        Button addProductButton = new Button("Добавить товар");
        addProductButton.getStyle().set("color", "green");

        addProductButton.addClickListener(e -> showAddProductDialog());
        if (this.userService.getCurrentUser() != null) {
            add(addProductButton);
        }
    }

    private void configureProductGrid() {
        productGrid.setColumns("name", "price", "quantity");
        productGrid.getColumnByKey("name").setHeader("Товар");
        productGrid.getColumnByKey("price").setHeader("Цена");
        productGrid.getColumnByKey("quantity").setHeader("Количество");
        productGrid.addColumn(this::getSuppliersList).setHeader("Поставщики");

        if (this.userService.getCurrentUser() != null) {
            productGrid.addComponentColumn(this::createAddToCartButton);
            productGrid.addComponentColumn(this::createDeleteButton);
            productGrid.addComponentColumn(this::createEditButton);
        }

        add(productGrid);
    }

    private void refreshGrid() {
        if (selectedCategory != null) {
            List<Product> productsInCategory = productService.getProductsByCategory(selectedCategory);
            productGrid.setItems(productsInCategory);
        } else {
            productGrid.setItems(productService.getAllProducts());
        }
    }

    private void showAddProductDialog() {
        Dialog dialog = new Dialog();
        dialog.setModal(true);

        VerticalLayout layout = new VerticalLayout();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Название товара");
        TextField priceField = new TextField("Цена");
        TextField quantityField = new TextField("Количество");

        MultiSelectListBox<Supplier> supplierListBox = new MultiSelectListBox<>();
        supplierListBox.setItems(supplierService.getAllSuppliers());

        supplierListBox.setRenderer(new TextRenderer<>(Supplier::getName));

        Button saveButton = new Button("Сохранить");
        Button cancelButton = new Button("Отмена");

        VerticalLayout supplierLayout = new VerticalLayout();
        supplierLayout.setAlignItems(Alignment.CENTER);
        supplierLayout.add(new H4("Поставщики"), supplierListBox);

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);

        saveButton.addClickListener(e -> {
            BigDecimal price;
            int quantity;
            try {
                price = new BigDecimal(priceField.getValue());
                quantity = Integer.parseInt(quantityField.getValue());
                if (price.compareTo(BigDecimal.ZERO) < 0 || quantity < 0) {
                    Notification.show("Цена и количество не могут быть отрицательными.");
                    return;
                }
            } catch (NumberFormatException e1) {
                Notification.show("Цена и количество должны быть числами.");
                return;
            }

            Product product = new Product();
            product.setName(nameField.getValue());
            product.setPrice(price);
            product.setQuantity(quantity);

            List<Supplier> selectedSuppliers = new ArrayList<>(supplierListBox.getSelectedItems());
            product.setSuppliers(selectedSuppliers);

            product.setCategory(selectedCategory);
            productService.addNewProduct(product, selectedCategory);
            refreshGrid();
            dialog.close();
        });

        cancelButton.addClickListener(e -> dialog.close());

        formLayout.add(nameField, priceField, quantityField);

        layout.add(formLayout, new Hr(), supplierLayout, buttonLayout);

        dialog.add(layout);
        dialog.open();
    }

    public void setCategory(Category category) {
        this.selectedCategory = category;
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
        Button cancelButton = new Button("Отмена");

        saveButton.addClickListener(e -> {
            BigDecimal price;
            int quantity;
            try {
                price = new BigDecimal(priceField.getValue());
                quantity = Integer.parseInt(quantityField.getValue());
                if (price.compareTo(BigDecimal.ZERO) < 0 || quantity < 0) {
                    Notification.show("Цена и количество не могут быть отрицательными.");
                    return;
                }
            } catch (NumberFormatException e1) {
                Notification.show("Цена и количество должны быть числами.");
                return;
            }

            product.setName(nameField.getValue());
            product.setPrice(price);
            product.setQuantity(quantity);
            productService.updateProduct(product);
            refreshGrid();
            dialog.close();
        });

        cancelButton.addClickListener(e -> dialog.close());

        formLayout.add(nameField, priceField, quantityField);
        dialog.add(formLayout, saveButton, cancelButton);
        dialog.open();
    }


    private Button createAddToCartButton(Product product) {
        Button addToCartButton = new Button("Купить");
        addToCartButton.addClickListener(e -> {
            this.cartService.addToCart(this.userService.getCurrentUser().getId(), product.getId());
        });
        return addToCartButton;
    }

    private Button createDeleteButton(Product product) {
        Button deleteButton = new Button("Удалить");
        deleteButton.getStyle().set("color", "red");
        deleteButton.addClickListener(e -> {
            productService.deleteProductById(product.getId());
            refreshGrid();
        });
        return deleteButton;
    }

    private Button createEditButton(Product product) {
        Button editButton = new Button("Редактировать");
        editButton.addClickListener(e -> {
            showEditProductDialog(product);
            refreshGrid();
        });
        return editButton;
    }

    private String getSuppliersList(Product product) {
        List<Supplier> suppliers = product.getSuppliers();
        if (suppliers != null && !suppliers.isEmpty()) {
            return suppliers.stream()
                    .map(Supplier::getName)
                    .collect(Collectors.joining(", "));
        }
        return "Нет поставщиков";
    }
}
