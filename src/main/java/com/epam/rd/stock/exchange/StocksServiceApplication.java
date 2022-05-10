package com.epam.rd.stock.exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class StocksServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StocksServiceApplication.class, args);
    }

}
