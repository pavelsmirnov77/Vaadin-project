package ru.sovkombank.project.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import ru.sovkombank.project.services.UserService;

public class Header extends HorizontalLayout {

    public Header(UserService userService) {
        Button cartButton = createButton("Корзина", "cart");
        Button mainPageButton = createButton("Главная", "");

        if (userService.isAuthenticated()) {
            String userName = userService.getCurrentUser().getUsername();
            H3 userInfo = new H3(new Text(userName));
            Button logoutButton = new Button("Выход");

            logoutButton.addClickListener(e -> {
                userService.logout();
                Notification.show("Вы успешно вышли!");
                getUI().ifPresent(ui -> ui.navigate(""));
            });

            add(userInfo, cartButton, mainPageButton, logoutButton);
        } else {
            Button loginButton = createButton("Вход", "api/signin");
            Button registerButton = createButton("Регистрация", "api/signup");
            add(cartButton, mainPageButton, loginButton, registerButton);
        }
    }

    private Button createButton(String text, String route) {
        Button button = new Button(text);
        button.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(route)));
        return button;
    }
}
