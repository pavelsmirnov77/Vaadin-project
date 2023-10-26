package ru.sovkombank.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sovkombank.project.entities.CartProduct;

import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    /**
     * Получает список товаров в корзине по id корзины
     *
     * @param cartId id корзины
     * @return список товаров в корзине
     */
    List<CartProduct> findCartProductsByCartId(Long cartId);

    /**
     * Ищет товар в корзине по id корзины и id товара
     *
     * @param cartId  id корзины
     * @param productId id товара
     * @return товар в корзине
     */
    Optional<CartProduct> findCartProductByCart_IdAndProduct_Id(Long cartId,Long productId);

    /**
     * Удаляет товар из корзины по id корзины и id товара
     *
     * @param cartId id корзины
     * @param productId id товара
     */
    void deleteCartProductByCart_IdAndProduct_Id(long cartId, long productId);
}
