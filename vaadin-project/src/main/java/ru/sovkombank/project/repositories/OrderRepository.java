package ru.sovkombank.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkombank.project.entities.Order;
import ru.sovkombank.project.entities.User;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<OrderRepository> findOrderByUser(User user);
}
