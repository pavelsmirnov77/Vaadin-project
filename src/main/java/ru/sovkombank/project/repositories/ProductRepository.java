package ru.sovkombank.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkombank.project.entities.Category;
import ru.sovkombank.project.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * Получает все товары по определенной категории
     *
     * @param category категория товаров
     * @return список товаров определенной категории
     */
    List<Product> getAllByCategory(Category category);
}
