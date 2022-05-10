package com.epam.rd.stock.exchange.scheduler;

import com.epam.rd.stock.exchange.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@RequiredArgsConstructor
public class StockScheduler {

    private final StockService stockService;

    private final HttpClient httpClient;

    @Value("${user.service.process.stocks.and.order.url}")
    private String processOrdersUrl;

    @Scheduled(fixedDelayString = "${stock.schedule.fixed.delay}", initialDelay = 60000)
    public void liveStock() {
        stockService.updateStocks();
        processOrders();
    }

    private void processOrders() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(processOrdersUrl))
                    .build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
