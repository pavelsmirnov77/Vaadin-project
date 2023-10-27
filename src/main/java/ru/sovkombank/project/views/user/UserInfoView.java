package ru.sovkombank.project.views.user;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import ru.sovkombank.project.entities.User;
import ru.sovkombank.project.services.UserService;
import ru.sovkombank.project.views.MainLayout;
import ru.sovkombank.project.views.product.ProductView;

@Route(value = "user", layout = MainLayout.class)
@PageTitle("Информация о пользователе")
public class UserInfoView extends VerticalLayout {
    private final UserService userService;
    private final User currentUser;

    public UserInfoView(UserService userService) {
        this.userService = userService;
        this.currentUser = userService.getCurrentUser();

        if (currentUser != null) {
            H1 pageTitle = new H1("Информация о пользователе");
            add(pageTitle);

            FormLayout userInfoLayout = new FormLayout();
            H3 nameLabel = new H3("Имя: " + currentUser.getUsername());
            H3 emailLabel = new H3("Email: " + currentUser.getEmail());
            userInfoLayout.add(nameLabel, emailLabel);
            add(userInfoLayout);

            Button updateButton = new Button("Обновить");
            Button deleteButton = new Button("Удалить аккаунт");
            Button logoutButton = new Button("Выйти");

            updateButton.addClickListener(e -> showUpdateUserDialog());
            deleteButton.addClickListener(e -> deleteUserAccount());
            logoutButton.addClickListener(e -> logoutUser());

            HorizontalLayout buttonsLayout = new HorizontalLayout(updateButton, deleteButton, logoutButton);
            buttonsLayout.setSpacing(true);
            add(buttonsLayout);
        } else {
            add(new H2("Вы не вошли в аккаунт. Авторизируйтесь или зарегистрируйтесь."));
        }
    }

    private void showUpdateUserDialog() {
        Dialog updateDialog = new Dialog();
        updateDialog.setModal(true);

        FormLayout updateLayout = new FormLayout();

        TextField nameField = new TextField("Имя");
        Button saveButton = new Button("Сохранить");
        Button cancelButton = new Button("Отмена");

        nameField.setValue(currentUser.getUsername());

        saveButton.addClickListener(e -> {
            currentUser.setUsername(nameField.getValue());
            userService.updateUser(currentUser);
            updateDialog.close();
            Notification.show("Информация о пользователе обновлена");
            UI.getCurrent().getPage().reload();
        });

        cancelButton.addClickListener(e -> updateDialog.close());

        updateLayout.add(nameField, saveButton, cancelButton);
        updateDialog.add(updateLayout);
        updateDialog.open();
    }

    private void deleteUserAccount() {
        userService.deleteUserById(currentUser.getId());
        Notification.show("Аккаунт успешно удален");
        getUI().ifPresent(ui -> ui.navigate(""));
        VaadinSession.getCurrent().getSession().invalidate();
    }

    private void logoutUser() {
        userService.logout();
        Notification.show("Вы успешно вышли!");
        getUI().ifPresent(ui -> ui.navigate(ProductView.class));
        VaadinSession.getCurrent().getSession().invalidate();
    }
}
