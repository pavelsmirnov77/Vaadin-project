package ru.sovkombank.project.views.order;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.sovkombank.project.entities.Order;
import ru.sovkombank.project.entities.User;
import ru.sovkombank.project.services.OrderService;
import ru.sovkombank.project.services.UserService;
import ru.sovkombank.project.views.MainLayout;
import ru.sovkombank.project.views.authorization.SignInView;
import ru.sovkombank.project.views.registration.SignUpView;

import java.util.List;

@Route(value = "orders", layout = MainLayout.class)
@PageTitle("Заказы")
public class OrderView extends VerticalLayout {

    public OrderView(OrderService orderService, UserService userService) {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            List<Order> orders = orderService.getAllOrdersByUserId(currentUser.getId());

            if (orders.isEmpty()) {
                H1 noOrdersLabel = new H1("Заказов нет!");
                add(noOrdersLabel);
            } else {
                for (Order order : orders) {
                    OrderComponent orderComponent = new OrderComponent(order, orderService);
                    add(orderComponent);
                }
            }
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
}
