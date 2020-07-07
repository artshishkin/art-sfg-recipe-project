package com.artarkatesoft.services;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public Recipe getRecipe(Long id) {
        return recipeRepository.findById(id).get();
    }

    public Iterable<Recipe> getAllRecipes(){
        return recipeRepository.findAll();
    }

}
