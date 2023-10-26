package ru.sovkombank.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkombank.project.entities.Cart;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    /**
     * Получает корзину по id клиента
     *
     * @param userId Уникальный идентификатор пользователя
     * @return Возвращает найденную корзину
     */
    List<Cart> findCartByUser_Id(Long userId);

    /**
     * Подсчитывает количество корзин, в которых есть определенных товар
     *
     * @param productId Уникальный идентификатор товара
     * @return Возвращает количество корзин
     */
    Integer countCartsByProducts_Id(long productId);
}
