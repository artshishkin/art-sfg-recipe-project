package com.artarkatesoft.services;

import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.exceptions.NotFoundException;
import com.artarkatesoft.repositories.reactive.RecipeReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeReactiveRepository recipeRepository;
    private final Converter<RecipeCommand, Recipe> toRecipeConverter;
    private final Converter<Recipe, RecipeCommand> toRecipeCommandConverter;

    @Override
    public Flux<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Mono<Recipe> getById(String id) {
        return recipeRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe with id " + id + " Not found")));
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand) {
        if (StringUtils.isEmpty(recipeCommand.getId())) recipeCommand.setId(null);
        Recipe detachedRecipe = toRecipeConverter.convert(recipeCommand);
        Mono<Recipe> savedRecipe = recipeRepository.save(detachedRecipe);
        return savedRecipe.map(toRecipeCommandConverter::convert);
    }

    @Override
    public Mono<RecipeCommand> getCommandById(String id) {
        return getById(id).map(toRecipeCommandConverter::convert);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return recipeRepository.deleteById(id);
    }

}
