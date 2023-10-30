package ru.sovkombank.project.services;

import ru.sovkombank.project.entities.Category;
import ru.sovkombank.project.entities.Product;

import java.util.List;

public interface ProductService {
    /**
     * Добавляет новый товар
     *
     * @param product Новый товар
     */
    void addNewProduct(Product product, Category category);

    /**
     * Получает все товары
     */
    List<Product> getAllProducts();

    /**
     * Изменяет товар
     *
     * @param product Обновленный товар
     */
    void updateProduct(Product product);

    /**
     * Удаляет товар по идентификатору
     *
     * @param productId Уникальный идентификатор товара
     */
    void deleteProductById(Long productId);

    /**
     * Получает все товары по категории
     *
     * @param category категория товаров
     * @return список товаров по категории
     */
    List<Product> getProductsByCategory(Category category);
}
