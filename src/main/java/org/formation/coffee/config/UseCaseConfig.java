package org.formation.coffee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.formation.coffee.application.port.out.*;
import org.formation.coffee.application.service.CoffeeApplicationService;

@Configuration(proxyBeanMethods = false)
class UseCaseConfig {
    @Bean
    CoffeeApplicationService coffeeApplicationService(LoadCoffeePort load, SaveCoffeePort save,
            TransactionRunner transactions) {
        return new CoffeeApplicationService(load, save, transactions);
    }
}
