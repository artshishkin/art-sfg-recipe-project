package com.artarkatesoft.domain;


import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Data
@EqualsAndHashCode(exclude = {"notes", "ingredients", "categories"})
public class Recipe {

    private Long id;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;

    private String directions;


    private Difficulty difficulty;


    private byte[] image;


    private Notes notes;


    @Setter(AccessLevel.NONE)
    private Set<Ingredient> ingredients = new HashSet<>();


    @Setter(AccessLevel.NONE)
    private Set<Category> categories = new HashSet<>();


    private LocalDateTime created;


    private LocalDateTime modified;

    public void setNotes(Notes notes) {
        if (this.notes == notes) return;
        this.notes = notes;

        if (notes != null)
            notes.setRecipe(this);
    }

    public void addIngredient(Ingredient ingredient) {
        if (ingredient == null) return;
        ingredients.add(ingredient);
        ingredient.setRecipe(this);
    }

    public Set<Ingredient> getIngredients() {
        return Collections.unmodifiableSet(ingredients);
    }

    public void removeIngredient(Ingredient ingredient) {
        ingredient.setRecipe(null);
        ingredients.remove(ingredient);
    }

    public void removeIngredientById(Long ingredientId) {
        ingredients.removeIf(ingredient -> Objects.equals(ingredient.getId(), ingredientId));
    }

    public void addCategory(Category category) {
        if (category == null) return;
        categories.add(category);
    }

    public Set<Category> getCategories() {
        return Collections.unmodifiableSet(categories);
    }
}
