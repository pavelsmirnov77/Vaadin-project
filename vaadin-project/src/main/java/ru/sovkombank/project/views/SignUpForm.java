package ru.sovkombank.project.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.sovkombank.project.entities.User;
import ru.sovkombank.project.services.UserService;

@Route("/api/signup")
@PageTitle("Регистрация пользователя")
public class SignUpForm extends VerticalLayout {
    public SignUpForm(UserService userService) {
        FormLayout form = new FormLayout();
        TextField username = new TextField("Имя пользователя");
        EmailField email = new EmailField("Email");
        PasswordField password = new PasswordField("Пароль");
        Button registerButton = new Button("Зарегистрировать");
        form.add(username, email, password, registerButton);

        Binder<User> binder = new Binder<>(User.class);

        binder.forField(username)
                .asRequired("Имя пользователя не может быть пустым")
                .bind(User::getUsername, User::setUsername);

        binder.forField(email)
                .asRequired("Email не может быть пустым")
                .bind(User::getEmail, User::setEmail);

        binder.forField(password)
                .asRequired("Пароль не может быть пустым")
                .bind(User::getPassword, User::setPassword);

        registerButton.addClickListener(e -> {
            if (binder.writeBeanIfValid(new User())) {
                User user = new User(username.getValue(), email.getValue(), password.getValue());
                userService.signUp(user);
            }
        });

        add(form);
    }
}
