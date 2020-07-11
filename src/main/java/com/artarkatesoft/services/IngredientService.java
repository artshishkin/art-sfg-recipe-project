package com.artarkatesoft.services;

import com.artarkatesoft.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findIngredientCommandByIdAndRecipeId(Long id, Long recipeId);
}
