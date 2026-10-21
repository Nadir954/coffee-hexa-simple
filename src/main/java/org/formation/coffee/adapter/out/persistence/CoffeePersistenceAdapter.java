package org.formation.coffee.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.formation.coffee.application.port.out.*;
import org.formation.coffee.domain.model.*;
import org.formation.coffee.domain.exception.CoffeeNotFoundException;

@Component
class CoffeePersistenceAdapter implements LoadCoffeePort, SaveCoffeePort {
    private final CoffeeJpaRepository repository;
    private final CoffeePersistenceMapper mapper;
    CoffeePersistenceAdapter(CoffeeJpaRepository repository, CoffeePersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    @Override public List<Coffee> loadAll() {
        return repository.findAllByOrderByIdAsc().stream().map(mapper::toDomain).toList();
    }
    @Override public Optional<Coffee> loadById(CoffeeId id) {
        return repository.findById(id.value()).map(mapper::toDomain);
    }
    @Override public Coffee save(Coffee coffee) {
        CoffeeId id = coffee.getId();
        if (id == null) return mapper.toDomain(repository.save(mapper.toEntity(coffee)));
        CoffeeJpaEntity entity = repository.findById(id.value())
                .orElseThrow(() -> new CoffeeNotFoundException(id));
        mapper.update(coffee, entity);
        // L'entité est gérée : Hibernate écrira les changements au commit.
        return mapper.toDomain(entity);
    }
}
