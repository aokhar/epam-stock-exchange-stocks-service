package com.epam.rd.stock.exchange.repository;

import com.epam.rd.stock.exchange.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, String> {
}
