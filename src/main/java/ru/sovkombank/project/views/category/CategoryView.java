package ru.sovkombank.project.views.category;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.sovkombank.project.entities.Category;
import ru.sovkombank.project.services.CartService;
import ru.sovkombank.project.services.CategoryService;
import ru.sovkombank.project.services.ProductService;
import ru.sovkombank.project.services.UserService;
import ru.sovkombank.project.views.MainLayout;
import ru.sovkombank.project.views.product.ProductView;

import java.util.List;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Категории товаров")
public class CategoryView extends VerticalLayout {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final CartService cartService;
    private final UserService userService;

    public CategoryView(CategoryService categoryService, ProductService productService, CartService cartService, UserService userService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.cartService = cartService;
        this.userService = userService;

        if (userService.getCurrentUser() != null) {
            Button createCategoryButton = new Button("Создать категорию");
            createCategoryButton.getStyle().set("color", "green");
            createCategoryButton.addClickListener(e -> showCreateCategoryDialog());
            add(createCategoryButton);
        } else if (categoryService.getAllCategories().isEmpty()) {
            H2 noCategoriesMessage = new H2("Категорий нет. Авторизуйтесь, чтобы их создать.");
            add(noCategoriesMessage);
        }

        refreshGrid();
    }

    private void showCreateCategoryDialog() {
        Dialog dialog = new Dialog();
        TextField categoryNameField = new TextField("Название категории");
        Button saveButton = new Button("Сохранить");

        saveButton.addClickListener(e -> {
            Category category = new Category();
            category.setName(categoryNameField.getValue());
            categoryService.createCategory(category);
            dialog.close();
            refreshGrid();
            UI.getCurrent().getPage().reload();
        });

        dialog.add(categoryNameField, saveButton);
        dialog.open();
    }

    private void refreshGrid() {

        List<Category> categories = categoryService.getAllCategories();

        for (Category category : categories) {
            VerticalLayout categoryLayout = new VerticalLayout();

            categoryLayout.add(new HorizontalLayout(new H2("Категория: " + category.getName()), createButtonsForCategory(category)));

            ProductView productView = new ProductView(productService, userService, cartService);
            productView.setCategory(category);
            categoryLayout.add(productView);

            add(categoryLayout);
        }
    }

    private HorizontalLayout createButtonsForCategory(Category category) {
        if (userService.getCurrentUser() != null) {
            Button editCategoryButton = new Button("Редактировать");
            editCategoryButton.addClickListener(e -> {
                showEditCategoryDialog(category);
            });

            Button deleteCategoryButton = new Button("Удалить");
            deleteCategoryButton.getStyle().set("color", "red");

            deleteCategoryButton.addClickListener(e -> {
                deleteCategory(category);
                UI.getCurrent().getPage().reload();
            });

            return new HorizontalLayout(editCategoryButton, deleteCategoryButton);
        }

        return new HorizontalLayout();
    }

    private void showEditCategoryDialog(Category category) {
        Dialog dialog = new Dialog();
        TextField categoryNameField = new TextField("Название категории", category.getName());
        Button saveButton = new Button("Сохранить");

        saveButton.addClickListener(e -> {
            category.setName(categoryNameField.getValue());
            categoryService.updateCategory(category);
            dialog.close();
            UI.getCurrent().getPage().reload();
            refreshGrid();
        });

        dialog.add(categoryNameField, saveButton);
        dialog.open();
    }

    private void deleteCategory(Category category) {
        categoryService.deleteCategory(category.getId());
        refreshGrid();
    }
}
