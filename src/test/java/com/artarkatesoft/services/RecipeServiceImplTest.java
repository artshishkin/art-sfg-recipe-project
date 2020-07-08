package com.artarkatesoft.services;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    @InjectMocks
    RecipeServiceImpl recipeService;

    Set<Recipe> recipes;

    @BeforeEach
    void setUp() {
        recipes = new HashSet<>();
        Recipe recipe;
        recipe = new Recipe();
        recipe.setId(1L);
        recipe.setDescription("Desc1");
        recipes.add(recipe);

        recipe = new Recipe();
        recipe.setId(2L);
        recipe.setDescription("Desc2");
        recipes.add(recipe);

    }

    @Test
    void getAllRecipes() {
        //given
        given(recipeRepository.findAll()).willReturn(recipes);
        //when
        Set<Recipe> allRecipes = recipeService.getAllRecipes();
        //then
        then(recipeRepository).should().findAll();
        assertThat(allRecipes).hasSize(2);

    }
}