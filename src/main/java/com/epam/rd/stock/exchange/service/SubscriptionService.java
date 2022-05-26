package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.Subscription;
import com.epam.rd.stock.exchange.model.Valuable;

import java.util.List;

public interface SubscriptionService {
    List<Subscription> findByValuableId(String valuableId);
    void remove(Subscription subscription);
}
