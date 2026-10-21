package org.formation.coffee.application.port.in;

import org.formation.coffee.domain.model.CoffeeId;
import org.formation.coffee.domain.model.Price;
import org.jspecify.annotations.Nullable;

// Un champ absent ou null laisse la valeur actuelle inchangée.
public record UpdateCoffeeCommand(CoffeeId id, @Nullable String name, @Nullable Price price) {}
