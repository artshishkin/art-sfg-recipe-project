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

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Long recipeId = command.getRecipeId();
        Recipe recipeRepo = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe with id " + recipeId + " not found"));
        Optional<Ingredient> ingredientOptional = recipeRepo.getIngredients()
                .stream()
                .filter(ingredient -> Objects.equals(command.getId(), ingredient.getId()))
                .findAny();

        if (ingredientOptional.isPresent()) {
            Ingredient ingredientFound = ingredientOptional.get();
            ingredientFound.setDescription(command.getDescription());
            ingredientFound.setAmount(command.getAmount());
            UnitOfMeasure uom = uomRepository.findById(command.getUom().getId())
                    .orElseThrow(() -> new RuntimeException("UOM not found"));
            ingredientFound.setUom(uom);

        } else {
            recipeRepo.addIngredient(toIngredientConverter.convert(command));
        }
        Recipe savedRecipe = recipeRepository.save(recipeRepo);
        IngredientCommand savedCommand = savedRecipe.getIngredients()
                .stream()
                .filter(ingredient -> Objects.equals(ingredient.getId(), command.getId()))
                .map(toIngredientCommandConverter::convert)
                .findAny()
                .orElseThrow(() -> new RuntimeException("Can not find id of ingredient"));
        return savedCommand;
    }
}
