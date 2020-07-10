package com.artarkatesoft.controllers;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @InjectMocks
    RecipeController recipeController;

    @Mock
    RecipeService recipeService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    void showInfo() throws Exception {
        //given
        Long id = 2L;
        Recipe recipe;
        recipe = new Recipe();
        recipe.setId(id);
        recipe.setDescription("Desc1");

        given(recipeService.getById(anyLong())).willReturn(recipe);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/show/{id}", id))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk(),
                        view().name("recipe/show"),
                        model().attribute("recipe", recipe)
                ));
        //then
        then(recipeService).should(times(1)).getById(eq(id));
        then(recipeService).should(never()).getAllRecipes();
        then(recipeService).shouldHaveNoMoreInteractions();
    }
}
