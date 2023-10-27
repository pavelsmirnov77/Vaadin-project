package ru.sovkombank.project.services;

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
    public void addToCart(long userId, long productId) {
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
                    CartProduct newCartProduct = new CartProduct();
                    newCartProduct.setCart(cart);
                    newCartProduct.setProduct(productToAdd);
                    newCartProduct.setQuantity(1);
                    cartProductRepository.save(newCartProduct);
                }
            }
        }
    }

    @Override
    public void updateProductQuantity(long userId, long productId, int quantity) {
        Optional<CartProduct> cartProduct = cartProductRepository.findCartProductByCart_IdAndProduct_Id(userId, productId);

        if (cartProduct.isPresent()) {
            CartProduct product = cartProduct.get();
            product.setQuantity(quantity);
            cartProductRepository.save(product);
        }
        else {
            throw new ProductException("Товар не найден");
        }
    }

    @Override
    @Transactional
    public void deleteProduct(long cartId, long cartProductId) {
        cartProductRepository.deleteCartProductByCart_IdAndProduct_Id(cartId, cartProductId);
    }

    @Override
    public void clearCart() {
        cartProductRepository.deleteAll();
    }

    @Override
    public List<Product> getListOfProductsInCart(long userId) {
        Optional<Cart> cart = cartRepository.findCartByUser_Id(userId);

        List<Product> productsInCart = new ArrayList<>();

        List<CartProduct> cartProducts = cartProductRepository.findCartProductsByCartId(cart.get().getId());

        for (CartProduct cartProduct : cartProducts) {
            Product product = cartProduct.getProduct();
            product.setQuantity(cartProduct.getQuantity());
            productsInCart.add(product);
        }

        return productsInCart;
    }
}
