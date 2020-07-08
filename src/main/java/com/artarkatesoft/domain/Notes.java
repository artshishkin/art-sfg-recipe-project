package com.artarkatesoft.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String notes;

    @OneToOne
    @EqualsAndHashCode.Exclude
    private Recipe recipe;

    public void setRecipe(Recipe recipe) {
        if (this.recipe == recipe) return;
        this.recipe = recipe;

        if (recipe != null)
            recipe.setNotes(this);
    }
}
