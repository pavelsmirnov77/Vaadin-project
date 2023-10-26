package ru.sovkombank.project.services;

import org.springframework.stereotype.Service;
import ru.sovkombank.project.entities.Product;
import ru.sovkombank.project.exceptions.ProductException;
import ru.sovkombank.project.repositories.CartRepository;
import ru.sovkombank.project.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public ProductServiceImpl(ProductRepository productRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public void addNewProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public boolean update(Product product) {
        productRepository.save(product);
        return true;
    }

    @Override
    public void deleteProductById(Long productId) {
        if (productRepository.existsById(productId)) {
            if (cartRepository.countCartsByProducts_Id(productId) == 0) {
                productRepository.deleteById(productId);
                return;
            }
            throw new ProductException("Товара не существует");
        }

    }
}
