package com.ahmedukamel.orderservice.repository;

import com.ahmedukamel.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
