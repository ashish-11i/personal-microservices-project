package com.ashish.order_service.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ashish.order_service.client.ProductClient;
import com.ashish.order_service.client.UserClient;
import com.ashish.order_service.exception.ResourceNotFoundException;
import com.ashish.order_service.models.Order;
import com.ashish.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final ProductClient productClient;

    public Order placeOrder(Long userId, Long productId, Integer quantity, String address) {

        // 1. Validate user exists — throws via fallback or Feign error if not found
        userClient.getUser(userId);

        // 2. Fetch product details for stock check and price calculation
        var product = productClient.getProduct(productId);

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + product.getStock());
        }

        double totalPrice = product.getPrice() * quantity;

        // 3. Persist the order first
        Order order = Order.builder()
                .userId(userId)
                .productId(productId)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .status("CREATED")
                .address(address)
                .build();

        Order saved = orderRepository.save(order);

        // 4. Deduct stock in product-service after the order is confirmed.
        //    If this call fails the order is already saved — in production this
        //    would be handled by a saga/outbox pattern. For this project the
        //    circuit-breaker fallback prevents a hard crash.
        productClient.reduceStock(productId, quantity);

        return saved;
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // Advances the order lifecycle: CREATED → PAID → SHIPPED → DELIVERED
    public Order updateStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
