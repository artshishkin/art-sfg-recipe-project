package com.artarkatesoft.services;

import com.artarkatesoft.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findIngredientCommandByIdAndRecipeId(String id, String recipeId);

    IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand);

    void deleteByIdAndRecipeId(String id, String recipeId);
}
