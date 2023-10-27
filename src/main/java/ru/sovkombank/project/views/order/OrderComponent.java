package ru.sovkombank.project.views.order;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.sovkombank.project.entities.Order;
import ru.sovkombank.project.entities.OrderItem;
import ru.sovkombank.project.services.OrderService;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderComponent extends VerticalLayout {
    private final OrderService orderService;

    public OrderComponent(Order order, OrderService orderService) {
        this.orderService = orderService;
        H3 orderHeader = new H3("Заказ №" + order.getId());
        add(orderHeader);

        addOrderInfo(order);
    }

    private void addOrderInfo(Order order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        Div dateTimeDiv = new Div();
        dateTimeDiv.setText("Время заказа: " + order.getDateTime().format(formatter));
        add(dateTimeDiv);

        List<OrderItem> orderItems = order.getOrderItems();
        if (orderItems != null && !orderItems.isEmpty()) {
            Grid<OrderItem> itemsGrid = new Grid<>(OrderItem.class);
            itemsGrid.setItems(orderItems);
            itemsGrid.setColumns("name", "price", "quantity");

            itemsGrid.getColumnByKey("name").setHeader("Товар");
            itemsGrid.getColumnByKey("price").setHeader("Цена");
            itemsGrid.getColumnByKey("quantity").setHeader("Количество");

            add(itemsGrid);
        }

        H2 totalPriceDiv = new H2("Общая стоимость: " + order.getTotalPrice() + " руб.");
        add(totalPriceDiv);

        Button deleteButton = new Button("Удалить заказ");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(e -> {
            orderService.deleteOrder(order.getId());
            UI.getCurrent().getPage().reload();
        });
        add(deleteButton);
    }
}
