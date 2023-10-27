package ru.sovkombank.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkombank.project.entities.Order;
import ru.sovkombank.project.entities.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    /**
     * Удаление заказа
     *
     * @param order удаляемый заказ
     */
    void deleteByOrder(Order order);
}
