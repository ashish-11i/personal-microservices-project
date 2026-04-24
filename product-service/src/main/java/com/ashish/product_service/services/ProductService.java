package com.ashish.product_service.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ashish.product_service.exception.ResourceNotFoundException;
import com.ashish.product_service.models.Product;
import com.ashish.product_service.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        product.setAvailable(true);
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Product updateProduct(Long id, Product updates) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        existing.setName(updates.getName());
        existing.setDescription(updates.getDescription());
        existing.setPrice(updates.getPrice());
        existing.setStock(updates.getStock());
        existing.setCategory(updates.getCategory());
        existing.setBrand(updates.getBrand());
        existing.setImageUrl(updates.getImageUrl());
        existing.setAvailable(updates.isAvailable());

        return productRepository.save(existing);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    // Called by order-service (via Feign) after an order is placed.
    // Deducts the ordered quantity from stock so inventory stays accurate.
    public Product reduceStock(Long id, Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product id: " + id);
        }

        product.setStock(product.getStock() - quantity);
        if (product.getStock() == 0) {
            product.setAvailable(false);
        }

        return productRepository.save(product);
    }
}
