package com.artarkatesoft.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.OneToOne;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
public class Ingredient {

    private String id;

    private String description;
    private BigDecimal amount;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Recipe recipe;

    @OneToOne
    private UnitOfMeasure uom;

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom) {
        this.description = description;
        this.amount = amount;
        this.uom = uom;
    }
}
