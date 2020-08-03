package com.artarkatesoft.services;

import com.artarkatesoft.commands.IngredientCommand;
import reactor.core.publisher.Mono;

public interface IngredientService {
    Mono<IngredientCommand> findIngredientCommandByIdAndRecipeId(String id, String recipeId);

    Mono<IngredientCommand> saveIngredientCommand(IngredientCommand ingredientCommand);

    Mono<Void> deleteByIdAndRecipeId(String id, String recipeId);
}
