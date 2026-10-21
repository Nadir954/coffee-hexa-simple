package org.formation.coffee.adapter.in.web.dto;

import org.jspecify.annotations.Nullable;
public record UpdateCoffeeRequest(@Nullable String name, @Nullable Integer price) {}
