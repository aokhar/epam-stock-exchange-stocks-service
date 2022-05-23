package com.epam.rd.stock.exchange.repository;

import com.epam.rd.stock.exchange.model.Valuable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValuableRepository extends JpaRepository<Valuable, Long> {
    Valuable findBySymbol(String symbol);
}
