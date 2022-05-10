package com.epam.rd.stock.exchange.repository;

import com.epam.rd.stock.exchange.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findBySymbol(String symbol);
}
