package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.Valuable;

public interface ValuableService {

    void updateValuable(Valuable valuable);

    Valuable get(String symbol);
}
