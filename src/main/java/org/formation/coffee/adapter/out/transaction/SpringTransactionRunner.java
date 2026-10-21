package org.formation.coffee.adapter.out.transaction;

import java.util.Objects;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.formation.coffee.application.port.out.TransactionRunner;

@Component
class SpringTransactionRunner implements TransactionRunner {
    private final TransactionTemplate read;
    private final TransactionTemplate write;
    SpringTransactionRunner(PlatformTransactionManager manager) {
        read = new TransactionTemplate(manager);
        read.setReadOnly(true);
        write = new TransactionTemplate(manager);
    }
    @Override public <T> T readOnly(Supplier<T> action) {
        return Objects.requireNonNull(read.execute(status -> action.get()));
    }
    @Override public <T> T inTransaction(Supplier<T> action) {
        return Objects.requireNonNull(write.execute(status -> action.get()));
    }
}
