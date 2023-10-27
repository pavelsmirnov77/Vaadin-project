package ru.sovkombank.project.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.sovkombank.project.entities.Product;
import ru.sovkombank.project.entities.User;
import ru.sovkombank.project.services.CartService;
import ru.sovkombank.project.services.UserService;

import java.util.List;

@Route("/cart")
@PageTitle("Корзина")
public class CartForm extends VerticalLayout {
    private final CartService cartService;
    private final Grid<Product> cartGrid;
    private final Long userId;

    public CartForm(CartService cartService, UserService userService) {
        this.cartService = cartService;
        User currentUser = userService.getCurrentUser();
        userId = currentUser.getId();
        Header header = new Header(userService);
        add(header);

        cartGrid = new Grid<>(Product.class);
        cartGrid.setColumns("name", "price", "quantity");
        cartGrid.addColumn(new ComponentRenderer<>(product -> {
            Button deleteButton = new Button("Удалить");
            deleteButton.addClickListener(e -> {
                cartService.deleteProduct(userId, product.getId());
                refreshGrid();
            });

            Button changeQuantityButton = new Button("Изменить кол-во");
            changeQuantityButton.addClickListener(e -> {
                showQuantityChangeDialog(product);
            });

            return new HorizontalLayout(changeQuantityButton, deleteButton);
        }));

        Button clearCartButton = new Button("Очистить корзину");
        clearCartButton.addClickListener(e -> {
            cartService.clearCart();
            refreshGrid();
        });

        add(cartGrid, clearCartButton);

        refreshGrid();
    }

    private void showQuantityChangeDialog(Product product) {
        Dialog dialog = new Dialog();
        NumberField quantityField = new NumberField("Введите новое кол-во:");
        quantityField.setValue((double) product.getQuantity());
        Button saveButton = new Button("Сохранить");

        saveButton.addClickListener(e -> {
            int newQuantity = (int) Math.round(quantityField.getValue());
            cartService.updateProductQuantity(userId, product.getId(), newQuantity);
            dialog.close();
            refreshGrid();
        });

        dialog.add(quantityField, saveButton);
        dialog.open();
    }

    private void refreshGrid() {
        List<Product> productsInCart = cartService.getListOfProductsInCart(userId);
        cartGrid.setItems(productsInCart);
    }
}