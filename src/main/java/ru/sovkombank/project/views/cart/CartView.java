package ru.sovkombank.project.views.cart;

import com.vaadin.flow.component.UI;
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
import ru.sovkombank.project.repositories.ProductRepository;
import ru.sovkombank.project.services.CartService;
import ru.sovkombank.project.services.OrderService;
import ru.sovkombank.project.services.UserService;
import ru.sovkombank.project.views.MainLayout;
import ru.sovkombank.project.views.authorization.SignInView;
import ru.sovkombank.project.views.registration.SignUpView;

import java.util.List;

@Route(value = "cart", layout = MainLayout.class)
@PageTitle("Корзина")
public class CartView extends VerticalLayout {
    private final CartService cartService;
    private final OrderService orderService;
    private final ProductRepository productRepository;
    private final Grid<Product> cartGrid;
    private final Long userId;

    public CartView(CartService cartService, OrderService orderService, ProductRepository productRepository, UserService userService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.productRepository = productRepository;
        this.cartGrid = new Grid<>(Product.class);
        this.userId = userService.getCurrentUser() != null ? userService.getCurrentUser().getId() : null;
        User currentUser = userService.getCurrentUser();

        if (userId != null) {
            configureCartGrid(currentUser);
        } else {
            H1 notLoggedInLabel = new H1("Вы не вошли в систему!");
            add(notLoggedInLabel);
            Button loginButton = new Button("Авторизация");
            loginButton.addClickListener(e -> UI.getCurrent().navigate(SignInView.class));

            Button registerButton = new Button("Регистрация");
            registerButton.addClickListener(e -> UI.getCurrent().navigate(SignUpView.class));

            HorizontalLayout buttonsLayout = new HorizontalLayout(loginButton, registerButton);
            buttonsLayout.setSpacing(true);
            add(buttonsLayout);
        }
    }

    private void configureCartGrid(User currentUser) {
        cartGrid.setColumns("name", "price", "quantity");

        cartGrid.getColumnByKey("name").setHeader("Товар");
        cartGrid.getColumnByKey("price").setHeader("Цена");
        cartGrid.getColumnByKey("quantity").setHeader("Количество");

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

        HorizontalLayout buttonsLayout = new HorizontalLayout(checkoutButton, clearCartButton);
        buttonsLayout.setSpacing(true);
        add(cartGrid, buttonsLayout);

        refreshGrid();
    }

    private void createOrderAndShowConfirmationDialog(User user) {
        List<Product> productsInCart = cartService.getListOfProductsInCart(userId);
        Dialog confirmationDialog = new Dialog();

        for (Product cartProduct : productsInCart) {
            Product fullProduct = productRepository.findById(cartProduct.getId()).orElse(null);
            if (fullProduct != null && fullProduct.getQuantity() < cartProduct.getQuantity()) {
                confirmationDialog.add("Товара " + fullProduct.getName() + " недостаточно в наличии. В корзине: " +
                        cartProduct.getQuantity() + ", доступно: " + fullProduct.getQuantity());
                confirmationDialog.open();
                return;
            }
        }

        if (productsInCart.isEmpty()) {
            Dialog errorDialog = new Dialog();
            errorDialog.add("Ваша корзина пуста. Добавьте товары перед оформлением заказа.");
            errorDialog.open();
        } else {
            Order order = orderService.createOrder(user, productsInCart);
            cartService.clearCart();

            confirmationDialog.add("Заказ успешно оформлен. Номер заказа: " + order.getId());
            confirmationDialog.open();
        }

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
