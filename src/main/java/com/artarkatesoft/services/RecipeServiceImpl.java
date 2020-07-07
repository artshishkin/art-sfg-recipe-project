package com.artarkatesoft.services;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public Recipe getRecipe(Long id) {
        return recipeRepository.findById(id).get();
    }

    @Override
    public Set<Recipe> getAllRecipes() {
        Iterable<Recipe> iterable = recipeRepository.findAll();
//        return iterable;
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toSet());
    }

}
