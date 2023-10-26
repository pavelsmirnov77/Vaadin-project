package ru.sovkombank.project.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import ru.sovkombank.project.entities.User;
import ru.sovkombank.project.services.UserService;

@Route("/api/signin")
@PageTitle("Авторизация")
public class SignInForm extends VerticalLayout {
    public SignInForm(UserService userService) {

        FormLayout form = new FormLayout();
        EmailField email = new EmailField("Email");
        PasswordField password = new PasswordField("Пароль");
        Button loginButton = new Button("Войти");

        form.add(email, password, loginButton);

        loginButton.addClickListener(e -> {
            String userEmail = email.getValue();
            String userPassword = password.getValue();

            if (userService.signIn(userEmail, userPassword)) {
                Notification.show("Вы успешно вошли!");

                User currentUser = userService.getCurrentUser(userEmail);
                Long userId = currentUser.getId();
                VaadinSession.getCurrent().setAttribute("userId", userId);
                getUI().ifPresent(ui -> ui.navigate(ProductList.class));

            } else {
                Notification.show("Неверный email или пароль. Попробуйте снова.");
            }
        });

        add(form);
    }
}
