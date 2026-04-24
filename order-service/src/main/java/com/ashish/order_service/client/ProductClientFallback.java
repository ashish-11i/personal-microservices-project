package com.ashish.order_service.client;

import com.ashish.order_service.dto.ProductDto;
import org.springframework.stereotype.Component;

// Triggered when product-service is DOWN. Returning stock=0 causes the order
// flow to reject the request with a clear "out of stock" message rather than a 500.
@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public ProductDto getProduct(Long id) {
        return ProductDto.builder()
                .id(id)
                .name("Unavailable")
                .stock(0)
                .available(false)
                .build();
    }

    @Override
    public ProductDto reduceStock(Long id, Integer quantity) {
        return ProductDto.builder().id(id).stock(0).build();
    }
}
