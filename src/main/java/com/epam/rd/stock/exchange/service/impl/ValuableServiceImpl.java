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
    public String updateValuable(ValuableUpdateDto valuableUpdateDto) {
        Valuable valuable = valuableRepository.findBySymbol(valuableUpdateDto.getSymbol());
        valuable = Valuable.builder().name(valuable.getName())
                    .symbol(valuableUpdateDto.getSymbol())
                    .price(valuableUpdateDto.getPrice())
                    .trend(valuableUpdateDto.getPrice().subtract(valuable.getPrice()))
                    .type(valuable.getType())
                    .build();
        valuableRepository.save(valuable);
        return valuable.getId();
    }

}

