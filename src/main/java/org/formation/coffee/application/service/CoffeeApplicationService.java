package org.formation.coffee.application.service;

import java.util.List;
import org.formation.coffee.application.port.in.*;
import org.formation.coffee.application.port.out.*;
import org.formation.coffee.domain.exception.CoffeeNotFoundException;
import org.formation.coffee.domain.model.*;

public final class CoffeeApplicationService implements ListCoffeesQuery, GetCoffeeQuery,
        CreateCoffeeUseCase, UpdateCoffeeUseCase {
    private final LoadCoffeePort load;
    private final SaveCoffeePort save;
    private final TransactionRunner transactions;
    public CoffeeApplicationService(LoadCoffeePort load, SaveCoffeePort save, TransactionRunner transactions) {
        this.load = load;
        this.save = save;
        this.transactions = transactions;
    }
    @Override public List<Coffee> listCoffees() {
        return transactions.readOnly(load::loadAll);
    }
    @Override public Coffee getCoffee(CoffeeId id) {
        return transactions.readOnly(() -> find(id));
    }
    @Override public Coffee createCoffee(CreateCoffeeCommand command) {
        return transactions.inTransaction(() -> save.save(Coffee.create(command.name(), command.price())));
    }
    @Override public Coffee updateCoffee(UpdateCoffeeCommand command) {
        return transactions.inTransaction(() -> {
            Coffee coffee = find(command.id());
            if (command.name() != null) coffee.rename(command.name());
            if (command.price() != null) coffee.changePrice(command.price());
            return save.save(coffee);
        });
    }
    private Coffee find(CoffeeId id) {
        return load.loadById(id).orElseThrow(() -> new CoffeeNotFoundException(id));
    }
}
