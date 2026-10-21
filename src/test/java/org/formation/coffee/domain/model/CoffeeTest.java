package org.formation.coffee.domain.model;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.formation.coffee.domain.exception.DomainValidationException;
import static org.assertj.core.api.Assertions.*;

class CoffeeTest {
    @ParameterizedTest @ValueSource(ints = {-1, 201, 500})
    void rejectsPricesOutsideTheRange(int amount) {
        assertThatThrownBy(() -> new Price(amount)).isInstanceOf(DomainValidationException.class);
    }
    @ParameterizedTest @ValueSource(ints = {0, 200})
    void acceptsPriceBoundaries(int amount) { assertThat(new Price(amount).amount()).isEqualTo(amount); }
    @ParameterizedTest @ValueSource(strings = {"", " ", "\t"})
    void rejectsBlankNames(String name) {
        assertThatThrownBy(() -> Coffee.create(name, new Price(10))).isInstanceOf(DomainValidationException.class);
    }
    @Test void checksNameLengthOnCreationAndRename() {
        Coffee coffee = Coffee.create("a".repeat(255), new Price(0));
        assertThatThrownBy(() -> coffee.rename("a".repeat(256))).isInstanceOf(DomainValidationException.class);
        assertThat(coffee.getName()).hasSize(255);
        assertThatThrownBy(() -> Coffee.create("a".repeat(256), new Price(10)))
                .isInstanceOf(DomainValidationException.class);
    }
    @ParameterizedTest @ValueSource(ints = {0, 6})
    void rejectsInvalidRatings(int rating) {
        assertThatThrownBy(() -> new Review(new ReviewId(1), "Ana", "Intense", rating))
                .isInstanceOf(DomainValidationException.class);
    }
    @Test void exposesAnImmutableReviewList() {
        Coffee coffee = Coffee.reconstitute(new CoffeeId(1), "Granador", new Price(10),
                List.of(new Review(new ReviewId(1), "Ana", "Intense", 5)));
        assertThatThrownBy(() -> coffee.getReviews().clear()).isInstanceOf(UnsupportedOperationException.class);
    }
    @Test void comparesPersistedCoffeesById() {
        Coffee first = Coffee.reconstitute(new CoffeeId(1), "A", new Price(10), List.of());
        Coffee second = Coffee.reconstitute(new CoffeeId(1), "B", new Price(20), List.of());
        assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
        assertThat(Coffee.create("A", new Price(10))).isNotEqualTo(Coffee.create("A", new Price(10)));
    }
    @ParameterizedTest @ValueSource(longs = {0, -1})
    void rejectsInvalidIds(long id) {
        assertThatThrownBy(() -> new CoffeeId(id)).isInstanceOf(DomainValidationException.class);
        assertThatThrownBy(() -> new ReviewId(id)).isInstanceOf(DomainValidationException.class);
    }
}
