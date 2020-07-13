package com.artarkatesoft.domain;


import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = {"notes", "ingredients", "categories"})
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    @Lob
    private String directions;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Lob
    private byte[] image;

    @OneToOne(cascade = CascadeType.ALL)
    private Notes notes;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Set<Ingredient> ingredients = new HashSet<>();

    @ManyToMany
    @Setter(AccessLevel.NONE)
    private Set<Category> categories = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
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
