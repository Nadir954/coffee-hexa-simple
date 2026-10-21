package org.formation.coffee.application.port.out;

import java.util.List;
import java.util.Optional;
import org.formation.coffee.domain.model.Coffee;
import org.formation.coffee.domain.model.CoffeeId;

public interface LoadCoffeePort {
    List<Coffee> loadAll();
    Optional<Coffee> loadById(CoffeeId id);
}
