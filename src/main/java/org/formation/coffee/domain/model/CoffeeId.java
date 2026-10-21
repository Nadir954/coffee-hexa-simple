package org.formation.coffee.domain.model;

import org.formation.coffee.domain.exception.DomainValidationException;

public record CoffeeId(long value) {
    public CoffeeId {
        if (value <= 0) throw new DomainValidationException("Id must be positive, got " + value);
    }
}
