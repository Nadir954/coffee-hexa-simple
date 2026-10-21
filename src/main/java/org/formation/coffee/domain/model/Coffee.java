package org.formation.coffee.domain.model;

import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;
import org.formation.coffee.domain.exception.DomainValidationException;

public final class Coffee {
    private final @Nullable CoffeeId id;
    private String name;
    private Price price;
    private final List<Review> reviews;

    private Coffee(@Nullable CoffeeId id, String name, Price price, List<Review> reviews) {
        this.id = id;
        validateName(name);
        this.name = name;
        this.price = Objects.requireNonNull(price);
        this.reviews = List.copyOf(reviews);
    }
    public static Coffee create(String name, Price price) {
        return new Coffee(null, name, price, List.of());
    }
    public static Coffee reconstitute(CoffeeId id, String name, Price price, List<Review> reviews) {
        return new Coffee(Objects.requireNonNull(id), name, price, reviews);
    }
    public @Nullable CoffeeId getId() { return id; }
    public String getName() { return name; }
    public Price getPrice() { return price; }
    public List<Review> getReviews() { return reviews; }
    public void rename(String name) { validateName(name); this.name = name; }
    public void changePrice(Price price) { this.price = Objects.requireNonNull(price); }
    private static void validateName(String name) {
        if (name == null || name.isBlank() || name.length() > 255) {
            throw new DomainValidationException("Coffee name must contain 1 to 255 characters");
        }
    }
    @Override public boolean equals(@Nullable Object other) {
        if (this == other) return true;
        // Deux cafés non enregistrés n'ont pas encore d'identité commune.
        return other instanceof Coffee coffee && id != null && id.equals(coffee.id);
    }
    @Override public int hashCode() { return Coffee.class.hashCode(); }
}
