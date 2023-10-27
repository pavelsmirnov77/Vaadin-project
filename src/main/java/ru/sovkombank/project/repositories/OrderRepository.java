package ru.sovkombank.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkombank.project.entities.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Получает список заказов по пользователю
     *
     * @param userId уникальный идентификатор пользователя
     * @return список заказов
     */
    List<Order> findAllByUser_Id(Long userId);
}
