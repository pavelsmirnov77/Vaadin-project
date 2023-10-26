package ru.sovkombank.project.services;

import ru.sovkombank.project.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    /**
     * Добавляет новый товар
     *
     * @param product Новый товар
     */
    void addNewProduct(Product product);

    /**
     * Ищет товар по его идентификатору
     *
     * @param productId Уникальный идентификатор товара
     * @return Возвращает найденный товар
     */
    Optional<Product> findById(long productId);

    /**
     * Получает все товары
     */
    List<Product> getAllProducts();

    /**
     * Изменяет товар
     *
     * @param product Обновленный товар
     * @return Возвращает статус обновления товара
     */
    boolean update(Product product);

    /**
     * Удаляет товар по идентификатору
     *
     * @param productId Уникальный идентификатор товара
     */
    void deleteProductById(Long productId);
}
