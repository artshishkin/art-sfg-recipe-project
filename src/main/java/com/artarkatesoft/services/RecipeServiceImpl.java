package com.artarkatesoft.services;

import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.exceptions.NotFoundException;
import com.artarkatesoft.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final Converter<RecipeCommand, Recipe> toRecipeConverter;
    private final Converter<Recipe, RecipeCommand> toRecipeCommandConverter;

    @Override
    public Set<Recipe> getAllRecipes() {
        log.debug("I'm in the service");
        Iterable<Recipe> iterable = recipeRepository.findAll();
//        return iterable;
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toSet());
    }

    @Override
    public Recipe getById(String id) {
        return recipeRepository.findById(id).orElseThrow(() -> new NotFoundException("Recipe with id " + id + " Not found"));
    }

    @Override
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        Recipe detachedRecipe = toRecipeConverter.convert(recipeCommand);
        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        return toRecipeCommandConverter.convert(savedRecipe);
    }

    @Override
    public RecipeCommand getCommandById(String id) {
        return toRecipeCommandConverter.convert(getById(id));
    }

    @Override
    public void deleteById(String id) {
        recipeRepository.deleteById(id);
    }

}
