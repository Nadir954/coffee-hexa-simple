package org.formation.coffee.application.port.out;

import java.util.function.Supplier;
public interface TransactionRunner {
    <T> T readOnly(Supplier<T> action);
    <T> T inTransaction(Supplier<T> action);
}
