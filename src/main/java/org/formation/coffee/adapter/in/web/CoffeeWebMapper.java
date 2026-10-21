package org.formation.coffee.adapter.in.web;

import java.util.Objects;
import org.mapstruct.Mapper;
import org.formation.coffee.adapter.in.web.dto.*;
import org.formation.coffee.application.port.in.*;
import org.formation.coffee.domain.model.*;

@Mapper
interface CoffeeWebMapper {
    CoffeeResponse toResponse(Coffee coffee);
    ReviewResponse toResponse(Review review);
    default long toId(CoffeeId id) { return Objects.requireNonNull(id).value(); }
    default long toId(ReviewId id) { return id.value(); }
    default int toAmount(Price price) { return price.amount(); }
    default CreateCoffeeCommand toCommand(CreateCoffeeRequest request) {
        return new CreateCoffeeCommand(request.name(), new Price(request.price()));
    }
    default UpdateCoffeeCommand toCommand(long id, UpdateCoffeeRequest request) {
        return new UpdateCoffeeCommand(new CoffeeId(id), request.name(),
                request.price() == null ? null : new Price(request.price()));
    }
}
