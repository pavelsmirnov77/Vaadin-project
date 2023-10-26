package ru.sovkombank.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkombank.project.entities.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Получает категорию товара по id
     *
     * @param id Уникальный идентификатор категории товара
     * @return категорию
     */
    Optional<Category> findCategoryById(Long id);
}
