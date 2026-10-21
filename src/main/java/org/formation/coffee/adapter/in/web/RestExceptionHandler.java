package org.formation.coffee.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;
import org.formation.coffee.domain.exception.*;

@RestControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(CoffeeNotFoundException.class)
    ProblemDetail notFound(CoffeeNotFoundException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Coffee not found");
        problem.setProperty("coffeeId", exception.coffeeId().value());
        return problem;
    }
    @ExceptionHandler(DomainValidationException.class)
    ProblemDetail invalid(DomainValidationException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage());
        problem.setTitle("Business rule violated");
        return problem;
    }
}
