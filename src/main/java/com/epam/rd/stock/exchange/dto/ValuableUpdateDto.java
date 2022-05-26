package com.epam.rd.stock.exchange.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValuableUpdateDto {

    private BigDecimal price;

    private String symbol;

    @JsonCreator
    public ValuableUpdateDto(@JsonProperty(value = "symbol", required = true) String symbol,
                             @JsonProperty(value = "price", required = true) BigDecimal price) {
        this.symbol = symbol;
        this.price = price.setScale(2,RoundingMode.HALF_UP);
    }

}
