package com.artarkatesoft.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {

    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        ingredient = new Ingredient();
    }

    @Test
    void getId() {
        long id = 4L;
        ingredient.setId(id);
        assertEquals(id, ingredient.getId());
    }

    @Test
    void getDescription() {
    }

    @Test
    void getRecipe() {
    }
}