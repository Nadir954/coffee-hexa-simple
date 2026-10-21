package org.formation.coffee.domain.model;

import org.formation.coffee.domain.exception.DomainValidationException;

public record Price(int amount) {
    public Price {
        if (amount < 0 || amount > 200) {
            throw new DomainValidationException("Price must be between 0 and 200, got " + amount);
        }
    }
}
