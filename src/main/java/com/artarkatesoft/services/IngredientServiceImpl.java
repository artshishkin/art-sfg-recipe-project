package com.artarkatesoft.services;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.commands.RecipeCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final RecipeService recipeService;

    @Override
    public IngredientCommand findIngredientCommandByIdAndRecipeId(Long id, Long recipeId) {
        RecipeCommand recipeCommand = recipeService.getCommandById(recipeId);
        return recipeCommand.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ingredient not Found"));
    }
}
