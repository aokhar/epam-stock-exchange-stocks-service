package com.epam.rd.stock.exchange.handler;

import com.epam.rd.stock.exchange.dto.ValuableUpdateDto;
import com.epam.rd.stock.exchange.request.Params;
import com.epam.rd.stock.exchange.request.SubscriptionRequest;
import com.epam.rd.stock.exchange.service.ValuableService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Service
@Slf4j
public class UpdateHandler extends TextWebSocketHandler {

    @Value("${valuable.api.stock.symbols}")
    private String symbols;

    @Autowired
    private ValuableService valuableService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        log.info("Update Received [" + message.getPayload() + "]");
        ValuableUpdateDto valuableUpdate = objectMapper.readValue(message.getPayload(), ValuableUpdateDto.class);
        valuableService.updateValuable(valuableUpdate);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connected");
        Params params = new Params(symbols);
        SubscriptionRequest request = new SubscriptionRequest(params);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(request)));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("Transport Error", exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        log.info("Connection Closed [" + status.getReason() + "]");
    }
}
