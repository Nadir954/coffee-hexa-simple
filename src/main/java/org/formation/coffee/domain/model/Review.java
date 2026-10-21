package org.formation.coffee.domain.model;

import java.util.Objects;
import org.formation.coffee.domain.exception.DomainValidationException;

public record Review(ReviewId id, String author, String comment, int rating) {
    public Review {
        Objects.requireNonNull(id);
        if (author == null || author.isBlank() || author.length() > 255) {
            throw new DomainValidationException("Review author must contain 1 to 255 characters");
        }
        if (comment == null || comment.length() > 255) {
            throw new DomainValidationException("Review comment must contain at most 255 characters");
        }
        if (rating < 1 || rating > 5) {
            throw new DomainValidationException("Rating must be between 1 and 5, got " + rating);
        }
    }
}
