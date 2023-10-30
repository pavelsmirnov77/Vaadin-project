package ru.sovkombank.project.services;

import ru.sovkombank.project.entities.Order;
import ru.sovkombank.project.entities.Product;
import ru.sovkombank.project.entities.User;

import java.util.List;

public interface OrderService {
    /**
     * Создает заказ
     *
     * @param user пользователь
     * @param productsInCart список товаров в заказе
     * @return сформированный заказ
     */
    Order createOrder(User user, List<Product> productsInCart);

    /**
     * Получает все заказы клиента по его id
     *
     * @param userId уникальный идентификатор клиента
     * @return список заказов
     */
    List<Order> getAllOrdersByUserId(Long userId);

    /**
     * Удаляет заказ по id
     *
     * @param orderId уникальный идентификатор заказа
     */
    void deleteOrder(Long orderId);
}
