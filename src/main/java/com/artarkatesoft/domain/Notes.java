package com.artarkatesoft.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
public class Notes {

    private String id;


    private String notes;


    @EqualsAndHashCode.Exclude
    private Recipe recipe;

    public void setRecipe(Recipe recipe) {
        if (this.recipe == recipe) return;
        this.recipe = recipe;

        if (recipe != null)
            recipe.setNotes(this);
    }
}
