package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.dto.StockDto;
import com.epam.rd.stock.exchange.model.enums.StockType;
import com.epam.rd.stock.exchange.exception.ApiResponseException;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.repository.StockRepository;
import com.epam.rd.stock.exchange.service.StockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {

    @Value("${stock.api.twelvedata.baseurl}")
    private String baseUrlTwelveData;

    @Value("${stock.api.twelvedata.key.indexes}")
    private String apiKeyIndexes;

    @Value("${stock.api.twelvedata.key.stocks}")
    private String apiKeyStocks;

    @Value("${stock.api.stock.symbols}")
    private String[] stockSymbols;

    @Value("${stock.api.index.symbols}")
    private String[] indexSymbols;

    private final HttpClient httpClient;

    private final ObjectMapper mapper;

    private final StockRepository stockRepository;

    @Override
    public void updateStocks() {
        update(stockSymbols, apiKeyStocks, StockType.STOCK);
        update(indexSymbols, apiKeyIndexes, StockType.INDEX);
    }

    private void update(String[] symbols, String apiKey, StockType type) {
        log.info("Update of " + Arrays.toString(symbols));
        try {
            List<StockDto> info = getInfo(symbols, apiKey);
            info.parallelStream().forEach(stockDto -> update(stockDto, type));
        } catch (ApiResponseException e) {
            log.error("Update of " + Arrays.toString(symbols) + " failed with exception " + e.getMessage());
        }
    }

    private void update(StockDto stockDto, StockType type) {
        Stock stock = stockRepository.findBySymbol(stockDto.getSymbol());
        if (stock != null) {
            stock.setName(stockDto.getName());
            stock.setPrice(stockDto.getPrice());
            stock.setTrend(stockDto.getChange());
        } else {
            stock = Stock.builder().name(stockDto.getName())
                    .symbol(stockDto.getSymbol())
                    .price(stockDto.getPrice())
                    .trend(stockDto.getChange())
                    .type(type)
                    .build();
        }
        stockRepository.save(stock);
    }

    private List<StockDto> getInfo(String[] symbols, String apiKey) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(buildURI(symbols, apiKey)).build();
            HttpResponse<String> response = httpClient.sendAsync(request,
                    HttpResponse.BodyHandlers.ofString()).get();
            return parseJson(response.body(), symbols);
        } catch (InterruptedException | ExecutionException | IOException e) {
            log.info(e.getMessage());
            throw new ApiResponseException(e.getMessage());
        }
    }

    private URI buildURI(String[] symbols, String apiKey) {
        return UriComponentsBuilder.fromUriString(baseUrlTwelveData)
                .queryParam("symbol", String.join(",", symbols))
                .queryParam("apikey", apiKey)
                .build().toUri();
    }

    private List<StockDto> parseJson(String response, String[] symbols) throws JsonProcessingException {
        List<StockDto> stocks = new ArrayList<>();
        JsonNode node = mapper.readTree(response);
        for (String symbol : symbols) {
            String json = node.get(symbol).toString();
            StockDto stock = mapper.readValue(json, StockDto.class);
            stocks.add(stock);
        }
        return stocks;
    }

}

