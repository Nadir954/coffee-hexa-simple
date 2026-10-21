package org.formation.coffee.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

interface CoffeeJpaRepository extends JpaRepository<CoffeeJpaEntity, Long> {
    @EntityGraph(attributePaths = "reviews")
    List<CoffeeJpaEntity> findAllByOrderByIdAsc();
    @Override @EntityGraph(attributePaths = "reviews")
    Optional<CoffeeJpaEntity> findById(Long id);
}
