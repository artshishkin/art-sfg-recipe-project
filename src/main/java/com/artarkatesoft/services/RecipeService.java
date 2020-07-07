package com.artarkatesoft.services;

import com.artarkatesoft.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getAllRecipes();
}
