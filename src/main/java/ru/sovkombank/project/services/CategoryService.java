package ru.sovkombank.project.services;

import ru.sovkombank.project.entities.Category;

import java.util.List;

public interface CategoryService {
    /**
     * Получает все категории товаров
     *
     * @return список категорий товаров
     */
    List<Category> getAllCategories();

    /**
     * Создает категорию товаров
     */
    void createCategory(Category category);

    /**
     * Обновляет информацию о категории
     *
     * @param category обновляемая категория
     */
    void updateCategory(Category category);

    /**
     * Удаляет категорию
     *
     * @param categoryId уникальный идентификатор категории
     */
    void deleteCategory(Long categoryId);
}
