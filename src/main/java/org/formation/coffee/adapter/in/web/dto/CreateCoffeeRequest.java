package org.formation.coffee.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public record CreateCoffeeRequest(@NotBlank String name, @NotNull Integer price) {}
