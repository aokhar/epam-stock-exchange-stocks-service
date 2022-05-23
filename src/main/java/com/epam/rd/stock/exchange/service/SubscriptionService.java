package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.enums.ValuableType;

public interface SubscriptionService {
    void updateSubscriptions(String id, ValuableType type);
}
