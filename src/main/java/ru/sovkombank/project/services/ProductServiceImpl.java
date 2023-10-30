package ru.sovkombank.project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sovkombank.project.entities.Category;
import ru.sovkombank.project.entities.Product;
import ru.sovkombank.project.exceptions.ProductException;
import ru.sovkombank.project.repositories.ProductRepository;

import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void addNewProduct(Product product, Category category) {
        log.info("Добавление товара");
        productRepository.save(product);
    }

    @Override
    @Transactional
    public List<Product> getAllProducts() {
        log.info("Получаем все товары");
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public void updateProduct(Product product) {
        log.info("Обновляем товар с id {}", product.getId());
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProductById(Long productId) {
        if (productRepository.existsById(productId)) {
            log.info("Удаляем товар с id {}", productId);
            try {
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new ProductException("Товар с id " + productId + " не найден"));

                product.getSuppliers().clear();

                Category category = product.getCategory();
                if (category != null) {
                    category.getProducts().remove(product);
                }

                productRepository.delete(product);
                log.info("Товар успешно удален");
            } catch (ProductException e) {
                log.error("Ошибка при удалении товара: Товар не найден");
            } catch (Exception e) {
                log.error("Ошибка при удалении товара: {}", e.getMessage());
            }
        } else {
            log.warn("Товар с id {} не найден и не был удален", productId);
        }
    }

    @Override
    @Transactional
    public List<Product> getProductsByCategory(Category category) {
        log.info("Получаем товары категории с id {}", category.getId());
        return productRepository.getAllByCategory(category);
    }
}
