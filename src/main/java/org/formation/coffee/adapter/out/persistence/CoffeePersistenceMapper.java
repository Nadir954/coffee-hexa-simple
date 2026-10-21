package org.formation.coffee.adapter.out.persistence;

import java.util.Objects;
import org.mapstruct.*;
import org.formation.coffee.domain.model.*;

@Mapper
interface CoffeePersistenceMapper {
    default Coffee toDomain(CoffeeJpaEntity entity) {
        return Coffee.reconstitute(new CoffeeId(Objects.requireNonNull(entity.getId())),
                entity.getName(), new Price(entity.getPrice()),
                entity.getReviews().stream().map(this::toReview).toList());
    }
    default Review toReview(ReviewJpaEntity entity) {
        return new Review(new ReviewId(Objects.requireNonNull(entity.getId())),
                entity.getAuthor(), entity.getComment(), entity.getRating());
    }
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    CoffeeJpaEntity toEntity(Coffee coffee);

    // Les avis appartiennent à l'agrégat, mais ces cas d'usage ne les modifient pas.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    void update(Coffee coffee, @MappingTarget CoffeeJpaEntity entity);
    default int toAmount(Price price) { return price.amount(); }
}
