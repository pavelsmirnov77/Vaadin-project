package ru.sovkombank.project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sovkombank.project.entities.Cart;
import ru.sovkombank.project.entities.CartProduct;
import ru.sovkombank.project.entities.Product;
import ru.sovkombank.project.exceptions.ProductException;
import ru.sovkombank.project.repositories.CartProductRepository;
import ru.sovkombank.project.repositories.CartRepository;
import ru.sovkombank.project.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, CartProductRepository cartProductRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartProductRepository = cartProductRepository;
    }

    @Override
    @Transactional
    public void addToCart(Long userId, Long productId) {
        Optional<Cart> userCart = cartRepository.findCartByUser_Id(userId);

        if (userCart.isPresent()) {
            Cart cart = userCart.get();

            Optional<Product> product = productRepository.findById(productId);

            if (product.isPresent()) {
                Product productToAdd = product.get();

                Optional<CartProduct> cartProduct = cartProductRepository.findCartProductByCart_IdAndProduct_Id(cart.getId(), productToAdd.getId());

                if (cartProduct.isPresent()) {
                    CartProduct existingCartProduct = cartProduct.get();
                    existingCartProduct.setQuantity(existingCartProduct.getQuantity() + 1);
                    cartProductRepository.save(existingCartProduct);
                } else {
                    CartProduct newCartProduct = CartProduct.builder()
                            .cart(cart)
                            .product(productToAdd)
                            .quantity(1)
                            .build();

                    log.info("Добавляем в корзину товар с id {}", productId);
                    cartProductRepository.save(newCartProduct);
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateProductQuantity(Long userId, Long productId, Integer quantity) {
        Optional<CartProduct> cartProduct = cartProductRepository.findCartProductByCart_IdAndProduct_Id(userId, productId);

        if (cartProduct.isPresent()) {
            CartProduct product = cartProduct.get();
            product.setQuantity(quantity);
            log.info("Обновляем в корзине количество товара с id {}", productId);
            cartProductRepository.save(product);
        }
        else {
            throw new ProductException("Товар не найден");
        }
    }

    @Override
    @Transactional
    public void deleteProduct(Long cartId, Long cartProductId) {
        log.info("Удаляем из корзины товар с id {}", cartProductId);
        cartProductRepository.deleteCartProductByCart_IdAndProduct_Id(cartId, cartProductId);
    }

    @Override
    @Transactional
    public void clearCart() {
        log.info("Очищаем корзину");
        cartProductRepository.deleteAll();
    }

    @Override
    public List<Product> getListOfProductsInCart(Long userId) {
        Optional<Cart> cart = cartRepository.findCartByUser_Id(userId);

        List<Product> productsInCart = new ArrayList<>();

        List<CartProduct> cartProducts = cartProductRepository.findCartProductsByCartId(cart.get().getId());

        for (CartProduct cartProduct : cartProducts) {
            Product product = cartProduct.getProduct();
            product.setQuantity(cartProduct.getQuantity());
            productsInCart.add(product);
        }
        log.info("Получаем товары в корзине");
        return productsInCart;
    }
}
