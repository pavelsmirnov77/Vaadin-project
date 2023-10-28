package ru.sovkombank.project.views.authorization;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import ru.sovkombank.project.entities.User;
import ru.sovkombank.project.services.UserService;
import ru.sovkombank.project.views.MainLayout;
import ru.sovkombank.project.views.product.ProductView;

@Route(value = "api/signin", layout = MainLayout.class)
@PageTitle("Авторизация")
public class SignInView extends VerticalLayout {
    public SignInView(UserService userService) {

        FlexLayout container = new FlexLayout();
        container.setSizeFull();
        container.setJustifyContentMode(JustifyContentMode.CENTER);
        container.setAlignItems(Alignment.CENTER);

        FormLayout form = new FormLayout();
        H1 label = new H1("Авторизация");
        label.getStyle().set("text-align", "center");
        EmailField email = new EmailField("Email");
        PasswordField password = new PasswordField("Пароль");
        Button loginButton = new Button("Войти");

        form.setMaxWidth("300px");
        email.setWidthFull();
        password.setWidthFull();
        loginButton.setWidthFull();

        form.add(label, email, password, loginButton);

        loginButton.addClickListener(e -> {
            String userEmail = email.getValue();
            String userPassword = password.getValue();

            User currentUser = userService.getCurrentUser(userEmail);

            if (userService.signIn(userEmail, userPassword)) {
                Notification.show("Вы успешно вошли!");

                Long userId = currentUser.getId();
                VaadinSession.getCurrent().setAttribute("userId", userId);
                getUI().ifPresent(ui -> ui.navigate(ProductView.class));
                UI.getCurrent().getPage().reload();
            } else {
                Notification.show("Неверная электронная почта или пароль. Попробуйте снова.");
            }
        });

        container.add(form);
        add(container);
    }
}
