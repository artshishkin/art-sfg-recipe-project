package com.artarkatesoft.services;

import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getAllRecipes();
    Recipe getById(String id);
    RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand);
    RecipeCommand getCommandById(String id);

    void deleteById(String id);
}
