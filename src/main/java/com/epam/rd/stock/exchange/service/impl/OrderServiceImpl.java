package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.repository.OrderRepository;
import com.epam.rd.stock.exchange.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public void createOrder(Order order) {
        orderRepository.save(order);
    }
}
