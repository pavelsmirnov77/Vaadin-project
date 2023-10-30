package ru.sovkombank.project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sovkombank.project.entities.Order;
import ru.sovkombank.project.entities.OrderItem;
import ru.sovkombank.project.entities.Product;
import ru.sovkombank.project.entities.User;
import ru.sovkombank.project.exceptions.EmptyCartException;
import ru.sovkombank.project.exceptions.ProductException;
import ru.sovkombank.project.repositories.OrderItemRepository;
import ru.sovkombank.project.repositories.OrderRepository;
import ru.sovkombank.project.repositories.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Order createOrder(User user, List<Product> productsInCart) {
        if (productsInCart.isEmpty()) {
            throw new EmptyCartException("Ваша корзина пуста. Добавьте товары в корзину перед оформлением заказа.");
        }

        List<Product> allProducts = productRepository.findAll();

        for (Product product : productsInCart) {
            Product productInList = allProducts.stream()
                    .filter(p -> p.getId().equals(product.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ProductException("Товар " + product.getName() + " не найден."));

            if (productInList.getQuantity() < product.getQuantity()) {
                throw new ProductException("Товар " + product.getName() + " недостаточно в наличии.");
            }
        }

        Order order = new Order();
        order.setDateTime(LocalDateTime.now());
        order.setUser(user);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalOrderPrice = BigDecimal.ZERO;

        for (Product product : productsInCart) {
            OrderItem orderItem = new OrderItem();
            orderItem.setName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(product.getQuantity());
            orderItem.setOrder(order);

            BigDecimal itemTotalPrice = product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity()));
            totalOrderPrice = totalOrderPrice.add(itemTotalPrice);
            orderItems.add(orderItem);

            Product productInList = allProducts.stream()
                    .filter(p -> p.getId().equals(product.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ProductException("Товар " + product.getName() + " не найден."));

            int remainingQuantity = productInList.getQuantity() - product.getQuantity();
            productInList.setQuantity(remainingQuantity);
        }

        productRepository.saveAll(allProducts);

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalOrderPrice);

        orderRepository.save(order);
        log.info("Заказ с номером {} создан", order.getId());

        return order;
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            orderItemRepository.deleteByOrder(order);
            log.info("Удаляем заказ с номером {} удален", orderId);

            orderRepository.delete(order);
        }
    }

    @Override
    public List<Order> getAllOrdersByUserId(Long userId) {
        log.info("Получаем все заказы пользователя с id {}", userId);
        return orderRepository.findAllByUser_Id(userId);
    }
}
