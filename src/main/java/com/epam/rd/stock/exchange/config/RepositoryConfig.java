package com.epam.rd.stock.exchange.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.epam.rd.stock.exchange.repository")
public class RepositoryConfig {
}
