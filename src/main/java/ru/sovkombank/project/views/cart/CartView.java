package ru.sovkombank.project.views.cart;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.sovkombank.project.entities.Order;
import ru.sovkombank.project.entities.Product;
import ru.sovkombank.project.entities.User;
import ru.sovkombank.project.services.CartService;
import ru.sovkombank.project.services.OrderService;
import ru.sovkombank.project.services.UserService;
import ru.sovkombank.project.views.MainLayout;

import java.util.List;

@Route(value = "cart", layout = MainLayout.class)
@PageTitle("Корзина")
public class CartView extends VerticalLayout {
    private final CartService cartService;
    private final OrderService orderService;
    private final Grid<Product> cartGrid;
    private final Long userId;

    public CartView(CartService cartService, OrderService orderService, UserService userService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.cartGrid = new Grid<>(Product.class);
        this.userId = userService.getCurrentUser() != null ? userService.getCurrentUser().getId() : null;
        User currentUser = userService.getCurrentUser();

        if (userId != null) {
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

            Button checkoutButton = new Button("Оформить заказ");
            checkoutButton.addClickListener(e -> {
                createOrderAndShowConfirmationDialog(currentUser);
            });

            clearCartButton.getStyle().set("color", "red");
            checkoutButton.getStyle().set("color", "green");

            add(cartGrid);

            HorizontalLayout buttonsLayout = new HorizontalLayout(checkoutButton, clearCartButton);
            buttonsLayout.setSpacing(true);
            add(buttonsLayout);

            refreshGrid();
        } else {
            H1 notLoggedInLabel = new H1("Вы не вошли в систему!");
            add(notLoggedInLabel);
        }
    }

    private void createOrderAndShowConfirmationDialog(User user) {
        List<Product> productsInCart = cartService.getListOfProductsInCart(userId);
        Order order = orderService.createOrder(user, productsInCart);
        cartService.clearCart();

        Dialog confirmationDialog = new Dialog();
        confirmationDialog.add("Заказ успешно оформлен. Номер заказа: " + order.getId());
        confirmationDialog.open();
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
