package ru.sovkombank.project.views.registration;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.sovkombank.project.entities.User;
import ru.sovkombank.project.services.UserService;
import ru.sovkombank.project.views.MainLayout;
import ru.sovkombank.project.views.authorization.SignInView;

@Route(value = "api/auth/signup", layout = MainLayout.class)
@PageTitle("Регистрация")
public class SignUpView extends VerticalLayout {
    public SignUpView(UserService userService) {

        FlexLayout container = new FlexLayout();
        container.setSizeFull();
        container.setJustifyContentMode(JustifyContentMode.CENTER);
        container.setAlignItems(Alignment.CENTER);

        FormLayout form = new FormLayout();

        H1 label = new H1("Регистрация");
        label.getStyle().set("text-align", "center");

        H4 loginLabel = new H4("Есть аккаунт? Авторизируйся");
        loginLabel.getStyle().set("text-align", "center");
        loginLabel.getStyle().set("margin-top", "20px");

        TextField username = new TextField("Имя пользователя");
        EmailField email = new EmailField("Email");
        PasswordField password = new PasswordField("Пароль");
        Button registerButton = new Button("Зарегистрироваться");
        Button loginButton = new Button("Авторизироваться");
        loginButton.addClickListener(e -> UI.getCurrent().navigate(SignInView.class));

        form.setMaxWidth("300px");
        username.setWidthFull();
        email.setWidthFull();
        password.setWidthFull();
        registerButton.setWidthFull();

        form.add(label, username, email, password, registerButton, loginLabel, loginButton);

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
                getUI().ifPresent(ui -> ui.navigate(SignInView.class));
            }
        });

        container.add(form);
        add(container);
    }
}
