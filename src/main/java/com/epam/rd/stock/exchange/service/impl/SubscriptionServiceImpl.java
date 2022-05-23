package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.model.enums.ValuableType;
import com.epam.rd.stock.exchange.repository.SubscriptionRepository;
import com.epam.rd.stock.exchange.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository repository;

    @Override
    public void updateSubscriptions(String id, ValuableType type) {
        repository.findB
    }
}
