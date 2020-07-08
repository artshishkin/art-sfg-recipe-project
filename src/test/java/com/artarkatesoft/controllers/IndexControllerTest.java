package com.artarkatesoft.controllers;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    @InjectMocks
    IndexController indexController;
    private Set<Recipe> recipes;

    @Captor
    ArgumentCaptor<String> stringCaptor;
    @Captor
    ArgumentCaptor<Object> objectCaptor;

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
    void index() {
        //given
        given(recipeService.getAllRecipes()).willReturn(recipes);
        //when
        String index = indexController.index(model);
        //then
        then(model).should().addAttribute(stringCaptor.capture(), objectCaptor.capture());
        then(recipeService).should().getAllRecipes();
        assertThat(index).isEqualTo("index");
        assertThat(stringCaptor.getValue()).isEqualTo("recipes");
        assertThat(objectCaptor.getValue()).isEqualTo(recipes);

    }

    @Test
    void indexJohn() {
        //when
        String viewName = indexController.index(model);
        //then
        verify(model).addAttribute(eq("recipes"), anySet());
        verify(recipeService,times(1)).getAllRecipes();
        assertThat(viewName).isEqualTo("index");
    }
    @Test
    void indexJohnBDD() {
        //when
        String viewName = indexController.index(model);
        //then
        then(model).should().addAttribute(eq("recipes"), anySet());
        then(recipeService).should().getAllRecipes();
        assertThat(viewName).isEqualTo("index");
    }
}