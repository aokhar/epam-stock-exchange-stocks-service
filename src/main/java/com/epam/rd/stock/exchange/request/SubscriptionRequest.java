package com.epam.rd.stock.exchange.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscriptionRequest {
    private final String action = "subscribe";
    private Params params;

}

