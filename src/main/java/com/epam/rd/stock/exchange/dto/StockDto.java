package com.epam.rd.stock.exchange.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class StockDto {

    private BigDecimal price;

    private String name;

    private BigDecimal change;

    private BigDecimal changePercent;

    private String symbol;

    @JsonCreator
    public StockDto(@JsonProperty(value = "symbol", required = true) String symbol,
                    @JsonProperty(value = "percent_change", required = true) BigDecimal changePercent,
                    @JsonProperty(value = "change", required = true) BigDecimal change,
                    @JsonProperty(value = "name", required = true) String name,
                    @JsonProperty(value = "close", required = true) BigDecimal price) {
        this.name = name;
        this.symbol = symbol;
        this.changePercent = changePercent;
        this.change = change.setScale(2, RoundingMode.HALF_UP);
        this.price = price.setScale(2,RoundingMode.HALF_UP);
    }

}
