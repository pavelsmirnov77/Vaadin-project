package ru.sovkombank.project.views.header;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import ru.sovkombank.project.services.UserService;

public class HeaderView extends HorizontalLayout {
    private UserService userService;

    public HeaderView(UserService userService) {
        if (userService.isAuthenticated()) {
            this.userService = userService;
            String userName = userService.getCurrentUser().getUsername();
            updateUsername(userName);
        } else {
            Button loginButton = createButton("Вход", "api/auth/signin");
            Button registerButton = createButton("Регистрация", "api/auth/signup");

            add(loginButton, registerButton);
        }
    }

    private Button createButton(String text, String route) {
        Button button = new Button(text);
        button.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(route)));

        return button;
    }

    public void updateUsername(String username) {
        H4 userInfo = new H4("User: " + username);
        userInfo.getStyle().set("margin-top", "10px");
        Button logoutButton = new Button("Выход");
        logoutButton.getStyle().set("color", "red");

        logoutButton.addClickListener(e -> {
            userService.logout();
            Notification.show("Вы успешно вышли!");
            UI.getCurrent().getPage().executeJs("location.assign('')");
        });

        add(userInfo, logoutButton);
    }
}
