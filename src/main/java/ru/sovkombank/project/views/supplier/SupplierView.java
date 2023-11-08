package ru.sovkombank.project.views.supplier;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.sovkombank.project.entities.Role;
import ru.sovkombank.project.entities.Supplier;
import ru.sovkombank.project.services.SupplierService;
import ru.sovkombank.project.services.UserService;
import ru.sovkombank.project.views.MainLayout;

@Route(value = "suppliers", layout = MainLayout.class)
@PageTitle("Наши поставщики")
public class SupplierView extends VerticalLayout {
    private final SupplierService supplierService;
    private final UserService userService;
    private final Grid<Supplier> supplierGrid = new Grid<>(Supplier.class);

    public SupplierView(SupplierService supplierService, UserService userService) {
        this.supplierService = supplierService;
        this.userService = userService;

        if(this.userService.getCurrentUser() != null && userService.getCurrentUser().getRole() == Role.ADMIN) {
            configureAddSupplierForm();
        }

        configureSupplierGrid();
        refreshSupplierGrid();
    }

    private void configureSupplierGrid() {
        supplierGrid.setColumns("name");
        supplierGrid.getColumnByKey("name").setHeader("Имя поставщика");

        if (this.userService.getCurrentUser() != null && userService.getCurrentUser().getRole() == Role.ADMIN) {
            supplierGrid.addComponentColumn(this::createUpdateButton);
            supplierGrid.addComponentColumn(this::createDeleteButton);
        }
        add(supplierGrid);
    }

    private void configureAddSupplierForm() {
        Button addSupplierButton = new Button("Добавить поставщика");
        addSupplierButton.getStyle().set("color", "green");

        addSupplierButton.addClickListener(e -> openAddSupplierDialog());
        add(addSupplierButton);
    }

    private void openAddSupplierDialog() {
        Dialog dialog = new Dialog();
        TextField nameField = new TextField("Название поставщика");
        Button addButton = new Button("Добавить");
        addButton.addClickListener(e -> {
            String name = nameField.getValue();
            if (!name.isEmpty()) {
                Supplier newSupplier = new Supplier();
                newSupplier.setName(name);
                supplierService.createSupplier(newSupplier);
                refreshSupplierGrid();
                dialog.close();
            }
        });

        Button cancelButton = new Button("Отмена");
        cancelButton.addClickListener(e -> dialog.close());
        dialog.add(nameField, addButton, cancelButton);
        dialog.open();
    }

    private void refreshSupplierGrid() {
        supplierGrid.setItems(supplierService.getAllSuppliers());
    }

    private Button createUpdateButton(Supplier supplier) {
        Button updateButton = new Button("Обновить");
        updateButton.addClickListener(e -> openUpdateSupplierDialog(supplier));
        return updateButton;
    }

    private Button createDeleteButton(Supplier supplier) {
        Button deleteButton = new Button("Удалить");
        deleteButton.getStyle().set("color", "red");
        deleteButton.addClickListener(e -> deleteSupplier(supplier));
        return deleteButton;
    }

    private void openUpdateSupplierDialog(Supplier supplier) {
        Dialog dialog = new Dialog();
        TextField nameField = new TextField("Название поставщика", supplier.getName());
        Button updateButton = new Button("Обновить");
        updateButton.addClickListener(e -> {
            String name = nameField.getValue();
            if (!name.isEmpty()) {
                supplier.setName(name);
                supplierService.updateSupplier(supplier);
                refreshSupplierGrid();
                dialog.close();
            }
        });
        Button cancelButton = new Button("Отмена");
        cancelButton.addClickListener(e -> dialog.close());
        dialog.add(nameField, updateButton, cancelButton);
        dialog.open();
    }

    private void deleteSupplier(Supplier supplier) {
        if (supplier.getProducts() != null) {
            showProductsWithSupplierDialog();
        }
        else {
            supplierService.deleteSupplier(supplier.getId());
        }

        refreshSupplierGrid();
    }

    private void showProductsWithSupplierDialog() {
        Dialog dialog = new Dialog();
        dialog.add("У данного поставщика есть товары в магазине, удаление невозможно");
        dialog.open();
    }
}
