package com.ashish.order_service.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ashish.order_service.models.Order;
import com.ashish.order_service.services.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // POST /orders?userId=&productId=&quantity=&address=
    // Validates user + product, checks stock, saves order, then deducts stock in product-service
    @PostMapping
    public ResponseEntity<Order> placeOrder(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String address) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.placeOrder(userId, productId, quantity, address));
    }

    // GET /orders — list all orders (admin / ops use)
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // GET /orders/{id} — fetch a single order by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    // GET /orders/user/{userId} — fetch all orders placed by a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    // PUT /orders/{id}/status?status=PAID — advance order through lifecycle
    // Valid transitions: CREATED → PAID → SHIPPED → DELIVERED
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }
}
