package com.artarkatesoft.controllers;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.services.RecipeService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = IndexController.class)
class IndexControllerMVCTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RecipeService recipeService;

    private Set<Recipe> recipes;

    @BeforeEach
    void setUp() {
        recipes = new HashSet<>();
        Recipe recipe;
        recipe = new Recipe();
        recipe.setId("1L");
        recipe.setDescription("Desc1");
        recipes.add(recipe);

        recipe = new Recipe();
        recipe.setId("2L");
        recipe.setDescription("Desc2");
        recipes.add(recipe);
    }

    @Test
    void index() throws Exception {
        //given
        given(recipeService.getAllRecipes()).willReturn(Flux.fromIterable(recipes));
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipes"))
                .andExpect(MockMvcResultMatchers.model().attribute("recipes", Is.isA(Set.class)));
    }
}
