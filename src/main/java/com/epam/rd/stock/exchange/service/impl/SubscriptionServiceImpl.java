package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.model.Subscription;
import com.epam.rd.stock.exchange.repository.SubscriptionRepository;
import com.epam.rd.stock.exchange.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public List<Subscription> findByValuableId(String valuableId) {
        return subscriptionRepository.findByValuableId(valuableId);
    }

    @Override
    public void remove(Subscription subscription) {
         subscriptionRepository.delete(subscription);
    }

}
