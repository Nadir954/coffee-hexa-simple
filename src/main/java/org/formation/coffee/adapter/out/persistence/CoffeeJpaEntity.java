package org.formation.coffee.adapter.out.persistence;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

@Entity
@Table(name = "coffee")
class CoffeeJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Nullable Long id;
    @Column(nullable = false, length = 255)
    private String name = "";
    @Column(nullable = false)
    private int price;
    @OneToMany(mappedBy = "coffee", fetch = FetchType.LAZY)
    @OrderBy("id")
    private List<ReviewJpaEntity> reviews = new ArrayList<>();
    protected CoffeeJpaEntity() {}
    public @Nullable Long getId() { return id; }
    public void setId(@Nullable Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public List<ReviewJpaEntity> getReviews() { return reviews; }
    public void setReviews(List<ReviewJpaEntity> reviews) { this.reviews = reviews; }
}
