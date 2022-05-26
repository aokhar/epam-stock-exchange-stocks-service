package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.model.Alert;
import com.epam.rd.stock.exchange.repository.AlertRepository;
import com.epam.rd.stock.exchange.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;

    @Override
    public void createAlert(Alert alert) {
        alertRepository.save(alert);
    }
}
