package ru.sovkombank.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sovkombank.project.entities.Order;
import ru.sovkombank.project.entities.OrderItem;
import ru.sovkombank.project.entities.Product;
import ru.sovkombank.project.entities.User;
import ru.sovkombank.project.repositories.OrderItemRepository;
import ru.sovkombank.project.repositories.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional
    public Order createOrder(User user, List<Product> productsInCart) {
        Order order = new Order();
        order.setDateTime(LocalDateTime.now());
        order.setUser(user);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalOrderPrice = BigDecimal.ZERO;

        for (Product product : productsInCart) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(product.getQuantity());
            orderItem.setOrder(order);

            BigDecimal itemTotalPrice = product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity()));
            totalOrderPrice = totalOrderPrice.add(itemTotalPrice);
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalOrderPrice);

        orderRepository.save(order);

        return order;
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            orderItemRepository.deleteByOrder(order);
            orderRepository.delete(order);
        }
    }

    @Override
    @Transactional
    public List<Order> getAllOrdersByUserId(Long userId) {
        return orderRepository.findAllByUser_Id(userId);
    }
}
