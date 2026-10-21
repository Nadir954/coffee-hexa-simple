package org.formation.coffee.application.service;

import java.util.*;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.formation.coffee.application.port.in.*;
import org.formation.coffee.application.port.out.*;
import org.formation.coffee.domain.model.*;
import org.formation.coffee.domain.exception.CoffeeNotFoundException;
import static org.assertj.core.api.Assertions.*;

class CoffeeApplicationServiceTest {
    private final MemoryCoffees storage = new MemoryCoffees();
    private final TestTransactions transactions = new TestTransactions();
    private final CoffeeApplicationService service = new CoffeeApplicationService(storage, storage, transactions);
    @BeforeEach void seed() {
        storage.save(Coffee.create("Granador", new Price(10)));
    }
    @Test void updatesPriceWithoutChangingNameOrReviews() {
        Coffee result = service.updateCoffee(new UpdateCoffeeCommand(new CoffeeId(1), null, new Price(12)));
        assertThat(result.getName()).isEqualTo("Granador");
        assertThat(result.getPrice()).isEqualTo(new Price(12));
        assertThat(transactions.writes).isEqualTo(1);
    }
    @Test void leavesAbsentFieldsAlone() {
        Coffee result = service.updateCoffee(new UpdateCoffeeCommand(new CoffeeId(1), null, null));
        assertThat(result.getName()).isEqualTo("Granador");
        assertThat(result.getPrice()).isEqualTo(new Price(10));
    }
    @Test void reportsMissingCoffeesWithoutSaving() {
        assertThatThrownBy(() -> service.updateCoffee(new UpdateCoffeeCommand(new CoffeeId(99), "X", null)))
                .isInstanceOf(CoffeeNotFoundException.class);
        assertThat(storage.loadAll()).hasSize(1);
    }
    @Test void createsAndReadsThroughThePorts() {
        Coffee created = service.createCoffee(new CreateCoffeeCommand("Estrellas", new Price(4)));
        assertThat(created.getId()).isEqualTo(new CoffeeId(2));
        assertThat(service.getCoffee(new CoffeeId(2)).getName()).isEqualTo("Estrellas");
        assertThat(service.listCoffees()).hasSize(2);
        assertThat(transactions.reads).isEqualTo(2);
        assertThat(transactions.writes).isEqualTo(1);
    }
    private static class MemoryCoffees implements LoadCoffeePort, SaveCoffeePort {
        private final Map<CoffeeId, Coffee> coffees = new LinkedHashMap<>();
        public List<Coffee> loadAll() { return List.copyOf(coffees.values()); }
        public Optional<Coffee> loadById(CoffeeId id) { return Optional.ofNullable(coffees.get(id)); }
        public Coffee save(Coffee coffee) {
            CoffeeId id = coffee.getId() == null ? new CoffeeId(coffees.size() + 1) : coffee.getId();
            Coffee stored = Coffee.reconstitute(id, coffee.getName(), coffee.getPrice(), coffee.getReviews());
            coffees.put(id, stored);
            return stored;
        }
    }
    private static class TestTransactions implements TransactionRunner {
        int reads, writes;
        public <T> T readOnly(Supplier<T> action) { reads++; return action.get(); }
        public <T> T inTransaction(Supplier<T> action) { writes++; return action.get(); }
    }
}
