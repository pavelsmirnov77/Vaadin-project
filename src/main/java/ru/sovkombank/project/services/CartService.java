package ru.sovkombank.project.services;

import ru.sovkombank.project.entities.Product;

import java.util.List;

public interface CartService {
    /**
     * Добавление товара в корзину
     *
     * @param userId    Уникальный идентификатор пользователя
     * @param productId Уникальный идентификатор товара
     */
    void addToCart(Long userId, Long productId);

    /**
     * Изменение количества товара в корзине
     *
     * @param userId    Уникальный идентификатор пользователя
     * @param productId Уникальный идентификатор товара
     * @param quantity  Количество добавляемого товара
     */
    void updateProductQuantity(Long userId, Long productId, Integer quantity);

    /**
     * Удаление товара из корзины
     *
     * @param productId Уникальный идентификатор товара
     */
    void deleteProduct(Long userId, Long productId);

    /**
     * Полностью очищает корзину пользователя
     */
    void clearCart();

    /**
     * Выдает список товаров в корзине пользователя
     *
     * @param userId Уникальный идентификатор пользователя
     * @return список товаров
     */
    List<Product> getListOfProductsInCart(Long userId);
}
