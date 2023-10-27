package ru.sovkombank.project.views.header;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import ru.sovkombank.project.services.UserService;

public class HeaderView extends HorizontalLayout {

    public HeaderView(UserService userService) {
        if (userService.isAuthenticated()) {
            String userName = userService.getCurrentUser().getUsername();

            H4 userInfo = new H4(new Text("User: " + userName));
            userInfo.getStyle().set("margin-top", "10px");

            Button logoutButton = new Button("Выход");
            logoutButton.getStyle().set("color", "red");

            logoutButton.addClickListener(e -> {
                userService.logout();
                Notification.show("Вы успешно вышли!");
                getUI().ifPresent(ui -> ui.navigate(""));
                UI.getCurrent().getPage().reload();
            });

            add(userInfo, logoutButton);
        } else {
            Button loginButton = createButton("Вход", "api/signin");
            Button registerButton = createButton("Регистрация", "api/signup");

            add(loginButton, registerButton);
        }
    }

    private Button createButton(String text, String route) {
        Button button = new Button(text);
        button.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(route)));

        return button;
    }
}