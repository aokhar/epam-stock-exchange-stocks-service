package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.model.ValuableHistory;
import com.epam.rd.stock.exchange.repository.ValuableHistoryRepository;
import com.epam.rd.stock.exchange.service.ValuableHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValuableHistoryServiceImpl implements ValuableHistoryService {

    private final ValuableHistoryRepository valuableHistoryRepository;

    @Override
    public void addHistory(ValuableHistory valuableHistory) {
        valuableHistoryRepository.save(valuableHistory);
    }
}
