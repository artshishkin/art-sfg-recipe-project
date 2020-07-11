package com.artarkatesoft.services;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.converters.IngredientToIngredientCommandConverter;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommandConverter toIngredientCommandConverter;

    @Override
    public IngredientCommand findIngredientCommandByIdAndRecipeId(Long id, Long recipeId) {
        Recipe recipe = recipeRepository
                .findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        return recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(id))
                .findFirst()
                .map(toIngredientCommandConverter::convert)
                .orElseThrow(() -> new RuntimeException("Ingredient not Found"));
    }
}
