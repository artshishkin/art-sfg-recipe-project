package com.artarkatesoft.services;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.converters.IngredientCommandToIngredientConverter;
import com.artarkatesoft.converters.IngredientToIngredientCommandConverter;
import com.artarkatesoft.domain.Ingredient;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.domain.UnitOfMeasure;
import com.artarkatesoft.exceptions.NotFoundException;
import com.artarkatesoft.repositories.reactive.RecipeReactiveRepository;
import com.artarkatesoft.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final RecipeReactiveRepository recipeRepository;
    private final UnitOfMeasureReactiveRepository uomRepository;
    private final IngredientToIngredientCommandConverter toIngredientCommandConverter;
    private final IngredientCommandToIngredientConverter toIngredientConverter;

    @Override
    public Mono<IngredientCommand> findIngredientCommandByIdAndRecipeId(String id, String recipeId) {
        return findIngredientByIdAndRecipeId(id, recipeId)
                .switchIfEmpty(Mono.error(new NotFoundException("Ingredient not found")))
                .map(toIngredientCommandConverter::convert)
                .doOnNext(command -> command.setRecipeId(recipeId));
    }

    private Mono<Ingredient> findIngredientByIdAndRecipeId(String id, String recipeId) {

        Mono<Recipe> recipeMono = recipeRepository
                .findById(recipeId)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe not found")));

        return recipeMono.flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equals(id))
                .next();
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {
        var recipeId = command.getRecipeId();

        Mono<Recipe> recipeMono = recipeRepository
                .findById(recipeId)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe not found")));

        Mono<Ingredient> ingredientMono = recipeMono.flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> Objects.equals(command.getId(), ingredient.getId()))
                .next();

        Mono<UnitOfMeasure> uomMono = uomRepository.findById(command.getUom().getId())
                .switchIfEmpty(Mono.error(new NotFoundException("UOM not found")));

        Mono<Ingredient> updatedIngredient = ingredientMono

                .switchIfEmpty(
                        Mono.justOrEmpty(toIngredientConverter.convert(command))
                )
                .zipWith(uomMono)
                .map(tuple2 -> {
                    Ingredient ingredient = tuple2.getT1();
                    UnitOfMeasure uom = tuple2.getT2();
                    ingredient.setDescription(command.getDescription());
                    ingredient.setAmount(command.getAmount());
                    ingredient.setUom(uom);
                    return ingredient;
                });
        Mono<Recipe> updatedRecipeMono = Mono.zip(recipeMono, updatedIngredient)
                .map(tuple2 -> {
                    Recipe recipe = tuple2.getT1();
                    Ingredient ingredient = tuple2.getT2();
                    recipe.removeIngredientById(ingredient.getId());
                    recipe.addIngredient(ingredient);
                    return recipe;
                })
                .flatMap(recipeRepository::save);

        Mono<Ingredient> savedIngredientMono = updatedRecipeMono.flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> Objects.equals(command.getId(), ingredient.getId()))
                .next()
                .switchIfEmpty(Mono.error(new RuntimeException("Ingredient did not save properly")));

        return savedIngredientMono.map(toIngredientCommandConverter::convert)
                .doOnNext(ingredientCommand -> ingredientCommand.setRecipeId(recipeId));

//        Ingredient ingredientOld;
//        ingredientMono.map(
//                ingredient -> {
//                    ingredient.setDescription(command.getDescription());
//                    ingredient.setAmount(command.getAmount());
//                    ingredient.setUom(uom);
//                    return ingredient;
//                }
//        );
//        if (ingredientOptional.isPresent()) {
//            ingredientOld = ingredientOptional.get();
//            ingredientOld.setDescription(command.getDescription());
//            ingredientOld.setAmount(command.getAmount());
//
//            ingredientOld.setUom(uom);
//
//        } else {
//            ingredientOld = toIngredientConverter.convert(command);
//            ingredientOld.setUom(uom);
//            recipeRepo.addIngredient(ingredientOld);
//        }
//        Recipe savedRecipe = recipeRepository.save(recipeRepo);
//
//
//        Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients()
//                .stream()
//                .filter(ingr -> Objects.equals(ingr.getId(), ingredientOld.getId()))
//                .findAny();
//
//        if (!savedIngredientOptional.isPresent()) {
//            savedIngredientOptional = savedRecipe.getIngredients().stream()
//                    .filter(recipeIngredient -> recipeIngredient.getDescription().equals(command.getDescription()))
//                    .filter(recipeIngredient -> recipeIngredient.getAmount().equals(command.getAmount()))
//                    .filter(recipeIngredient -> recipeIngredient.getUom().getId().equals(command.getUom().getId()))
//                    .findAny();
//        }
//        IngredientCommand ingredientCommand = toIngredientCommandConverter.convert(
//                savedIngredientOptional.orElseThrow(() -> new RuntimeException("Ingredient not saved")));
//        if (ingredientCommand != null) {
//            ingredientCommand.setRecipeId(savedRecipe.getId());
//        }
//        return ingredientCommand;
    }

    @Override
    public Mono<Void> deleteByIdAndRecipeId(String id, String recipeId) {
        Mono<Recipe> recipeMono = recipeRepository
                .findById(recipeId)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe not found")));
        return recipeMono.doOnNext(recipe -> recipe.removeIngredientById(id))
                .flatMap(recipeRepository::save)
                .then();
    }
}
