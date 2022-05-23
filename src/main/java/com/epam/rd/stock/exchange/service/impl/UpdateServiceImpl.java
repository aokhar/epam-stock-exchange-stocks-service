package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.dto.ValuableUpdateDto;
import com.epam.rd.stock.exchange.service.SubscriptionService;
import com.epam.rd.stock.exchange.service.UpdateService;
import com.epam.rd.stock.exchange.service.ValuableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateServiceImpl implements UpdateService {

    private final ValuableService valuableService;

    private final SubscriptionService subscriptionService;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void update(ValuableUpdateDto update) {

    }
}
