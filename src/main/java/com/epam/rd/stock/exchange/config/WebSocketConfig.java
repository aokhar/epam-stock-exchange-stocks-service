package com.epam.rd.stock.exchange.config;

import com.epam.rd.stock.exchange.handler.UpdateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebSocket
public class WebSocketConfig {

    @Value("${valuable.api.twelvedata.baseurl}")
    private String url;

    @Value("${valuable.api.twelvedata.key.stock}")
    private String apiKey;

    @Autowired
    private UpdateHandler updateHandler;

    @PostConstruct
    public void connect(){
        WebSocketConnectionManager stockConnectionManager = new WebSocketConnectionManager(new StandardWebSocketClient(), updateHandler, url + apiKey);
        stockConnectionManager.start();
    }
}
