package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.Order;

public interface OrderService {
    void createOrder(Order order);
}
