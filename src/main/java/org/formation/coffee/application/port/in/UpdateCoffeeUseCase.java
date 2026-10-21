package org.formation.coffee.application.port.in;

import org.formation.coffee.domain.model.Coffee;

public interface UpdateCoffeeUseCase {
    Coffee updateCoffee(UpdateCoffeeCommand command);
}
