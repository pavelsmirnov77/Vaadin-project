package ru.sovkombank.project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sovkombank.project.entities.Category;
import ru.sovkombank.project.repositories.CategoryRepository;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        log.info("Получаем все категории");
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public void createCategory(Category category) {
        log.info("Создаем категорию");
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void updateCategory(Category category) {
        log.info("Обновляем категорию с id {}", category.getId());
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        log.info("Удаляем категорию с id {}", categoryId);
        categoryRepository.deleteById(categoryId);
    }
}
