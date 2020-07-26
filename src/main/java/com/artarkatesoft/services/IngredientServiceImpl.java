package com.artarkatesoft.services;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.converters.IngredientCommandToIngredientConverter;
import com.artarkatesoft.converters.IngredientToIngredientCommandConverter;
import com.artarkatesoft.domain.Ingredient;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.domain.UnitOfMeasure;
import com.artarkatesoft.repositories.RecipeRepository;
import com.artarkatesoft.repositories.UnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository uomRepository;
    private final IngredientToIngredientCommandConverter toIngredientCommandConverter;
    private final IngredientCommandToIngredientConverter toIngredientConverter;

    @Override
    public IngredientCommand findIngredientCommandByIdAndRecipeId(String id, String recipeId) {
        return findIngredientByIdAndRecipeId(id, recipeId)
                .map(toIngredientCommandConverter::convert)
                .orElseThrow(() -> new RuntimeException("Ingredient not Found"));
    }

    private Optional<Ingredient> findIngredientByIdAndRecipeId(String id, String recipeId) {
        Recipe recipe = recipeRepository
                .findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        return recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(id))
                .findFirst();
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        var recipeId = command.getRecipeId();
        Recipe recipeRepo = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe with id " + recipeId + " not found"));
        Optional<Ingredient> ingredientOptional = recipeRepo.getIngredients()
                .stream()
                .filter(ingredient -> Objects.equals(command.getId(), ingredient.getId()))
                .findAny();

        UnitOfMeasure uom = uomRepository.findById(command.getUom().getId())
                .orElseThrow(() -> new RuntimeException("UOM not found"));

        Ingredient ingredient;
        if (ingredientOptional.isPresent()) {
            ingredient = ingredientOptional.get();
            ingredient.setDescription(command.getDescription());
            ingredient.setAmount(command.getAmount());

            ingredient.setUom(uom);

        } else {
            ingredient = toIngredientConverter.convert(command);
            ingredient.setUom(uom);
            recipeRepo.addIngredient(ingredient);
        }
        Recipe savedRecipe = recipeRepository.save(recipeRepo);


        Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients()
                .stream()
                .filter(ingr -> Objects.equals(ingr.getId(), ingredient.getId()))
                .findAny();

        if (!savedIngredientOptional.isPresent()) {
            savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredient -> recipeIngredient.getDescription().equals(command.getDescription()))
                    .filter(recipeIngredient -> recipeIngredient.getAmount().equals(command.getAmount()))
                    .filter(recipeIngredient -> recipeIngredient.getUom().getId().equals(command.getUom().getId()))
                    .findAny();
        }
        return toIngredientCommandConverter.convert(
                savedIngredientOptional.orElseThrow(() -> new RuntimeException("Ingredient not saved")));
    }

    @Override
    public void deleteByIdAndRecipeId(String id, String recipeId) {
        Ingredient ingredient = findIngredientByIdAndRecipeId(id, recipeId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));
        Recipe recipe = ingredient.getRecipe();
        recipe.removeIngredient(ingredient);
        recipeRepository.save(recipe);
    }
}
