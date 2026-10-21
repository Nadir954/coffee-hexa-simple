package org.formation.coffee.application.port.in;

import org.formation.coffee.domain.model.Coffee;

public interface GetCoffeeQuery {
    Coffee getCoffee(org.formation.coffee.domain.model.CoffeeId id);
}
