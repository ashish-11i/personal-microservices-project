package com.ashish.order_service.controllers;

import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping
    public Order placeOrder(
            @RequestParam Long userId,    
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        return orderService.placeOrder(userId, productId, quantity);
    }
}