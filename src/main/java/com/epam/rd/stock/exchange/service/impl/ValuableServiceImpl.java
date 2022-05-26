package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.dto.ValuableUpdateDto;
import com.epam.rd.stock.exchange.model.Valuable;
import com.epam.rd.stock.exchange.repository.ValuableRepository;
import com.epam.rd.stock.exchange.service.ValuableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValuableServiceImpl implements ValuableService {

    private final ValuableRepository valuableRepository;

    @Override
    public void updateValuable(Valuable valuable) {
        valuableRepository.save(valuable);
    }

    @Override
    public Valuable get(String symbol) {
        return valuableRepository.findBySymbol(symbol);
    }

}

