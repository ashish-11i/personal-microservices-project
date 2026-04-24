package com.ashish.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ashish.order_service.dto.ProductDto;

@FeignClient(name = "product-service", fallback = ProductClientFallback.class)
public interface ProductClient {

    // Fetch product details to validate availability and calculate total price
    @GetMapping("/products/{id}")
    ProductDto getProduct(@PathVariable Long id);

    // Deduct ordered quantity from product stock after a successful order
    @PatchMapping("/products/{id}/reduce-stock")
    ProductDto reduceStock(@PathVariable Long id, @RequestParam Integer quantity);
}
