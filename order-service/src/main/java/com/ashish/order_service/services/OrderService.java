package com.ashish.order_service.services;

import org.springframework.stereotype.Service;

import com.ashish.order_service.client.ProductClient;
import com.ashish.order_service.client.UserClient;
import com.ashish.order_service.models.Order;
import com.ashish.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final ProductClient productClient;

    public Order placeOrder(Long userId, Long productId, Integer quantity) {

        // 🔥 1. User validate
        userClient.getUser(userId);

        // 🔥 2. Product check
        var product = productClient.getProduct(productId);

        if (product.getStock() < quantity) {
            throw new RuntimeException("Product out of stock");
        }

        // 🔥 3. Calculate price
        double totalPrice = product.getPrice() * quantity;

        // 🔥 4. Save order
        Order order = Order.builder()
                .userId(userId)
                .productId(productId)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .status("CREATED")
                .build();

        return orderRepository.save(order);
    }
}