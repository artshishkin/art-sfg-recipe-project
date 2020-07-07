package com.artarkatesoft.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String notes;

    @OneToOne
    private Recipe recipe;

    public void setRecipe(Recipe recipe) {
        if (this.recipe == recipe) return;
        this.recipe = recipe;

        if (recipe != null)
            recipe.setNotes(this);
    }
}
