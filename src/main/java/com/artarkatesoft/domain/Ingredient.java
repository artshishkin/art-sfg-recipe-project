package com.artarkatesoft.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private BigDecimal amount;

    @ManyToOne
    private Recipe recipe;

    @OneToOne
    private UnitOfMeasure uom;

    public Ingredient(Recipe recipe, String description, BigDecimal amount, UnitOfMeasure uom) {
        this.recipe = recipe;
        this.description = description;
        this.amount = amount;
        this.uom = uom;
    }
}
