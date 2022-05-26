package com.epam.rd.stock.exchange.repository;

import com.epam.rd.stock.exchange.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
