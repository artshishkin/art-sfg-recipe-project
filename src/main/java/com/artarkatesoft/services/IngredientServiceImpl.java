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
import java.util.UUID;

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
                        Mono.just(toIngredientConverter.convert(command))
                                .log("toIngredientConverter.convert")
                                .doOnNext(ingredient -> ingredient.setId(UUID.randomUUID().toString()))
                                .log("setId(UUID.randomUUID().toString())")
                )
                .zipWith(uomMono)
                .log("zipWith(uomMono)")
                .map(tuple2 -> {
                    Ingredient ingredient = tuple2.getT1();
                    UnitOfMeasure uom = tuple2.getT2();
                    ingredient.setDescription(command.getDescription());
                    ingredient.setAmount(command.getAmount());
                    ingredient.setUom(uom);
                    return ingredient;
                })
                .log("updatedIngredient");

        Mono<Ingredient> savedIngredientMono = Mono.zip(recipeMono, updatedIngredient)
                .flatMap(tuple2 -> {
                    Recipe recipe = tuple2.getT1();
                    Ingredient ingredient = tuple2.getT2();
                    recipe.removeIngredientById(ingredient.getId());
                    recipe.addIngredient(ingredient);
                    return recipeRepository.save(recipe).then(Mono.just(ingredient));
                })
                .log("savedIngredientMono");

        return savedIngredientMono.map(toIngredientCommandConverter::convert)
                .doOnNext(ingredientCommand -> ingredientCommand.setRecipeId(recipeId));
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
