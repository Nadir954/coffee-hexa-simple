package org.formation.coffee.application.port.in;

import org.formation.coffee.domain.model.Price;
public record CreateCoffeeCommand(String name, Price price) {}
