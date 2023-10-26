package ru.sovkombank.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkombank.project.entities.Cart;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    /**
     * Получает корзину по id клиента
     *
     * @param userId Уникальный идентификатор пользователя
     * @return Возвращает найденную корзину
     */
    Optional<Cart> findCartByUser_Id(Long userId);

    /**
     * Удаляет из БД все записи о корзине пользователя
     *
     * @param userId Уникальный идентификатор пользователя
     */
    void deleteAllByUser_Id(long userId);
}
