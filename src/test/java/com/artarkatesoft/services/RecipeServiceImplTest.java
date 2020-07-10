package com.artarkatesoft.services;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

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

    @Test
    void getById() {
        //given
        Long id = 2L;
        Optional<Recipe> recipeOptional = recipes.stream().filter(rec -> id.equals(rec.getId())).findFirst();
        given(recipeRepository.findById(anyLong())).willReturn(recipeOptional);
        //when
        Recipe foundRecipe = recipeService.getById(id);
        //then
        then(recipeRepository).should(times(1)).findById(eq(id));
        then(recipeRepository).should(never()).findAll();
        then(recipeRepository).shouldHaveNoMoreInteractions();
        assertThat(foundRecipe.getId()).isEqualTo(recipeOptional.get().getId());
    }
}
