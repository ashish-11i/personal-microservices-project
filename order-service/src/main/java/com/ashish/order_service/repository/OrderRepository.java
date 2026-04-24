package com.ashish.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ashish.order_service.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
