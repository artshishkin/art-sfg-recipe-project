package com.artarkatesoft.domain;


import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Data
@EqualsAndHashCode(exclude = {"notes", "ingredients", "categories"})
@Document
public class Recipe {

    @Id
    private String id;
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

//    private LocalDateTime created;

//    private LocalDateTime modified;

    public void addIngredient(Ingredient ingredient) {
        if (ingredient == null) return;
        ingredients.add(ingredient);
    }

    public Set<Ingredient> getIngredients() {
        return Collections.unmodifiableSet(ingredients);
    }

    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
    }

    public void removeIngredientById(String ingredientId) {
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
