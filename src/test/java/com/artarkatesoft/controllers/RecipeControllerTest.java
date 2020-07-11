package com.artarkatesoft.controllers;

import com.artarkatesoft.commands.RecipeCommand;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @InjectMocks
    RecipeController recipeController;

    @Mock
    RecipeService recipeService;

    private MockMvc mockMvc;

    @Captor
    ArgumentCaptor<RecipeCommand> recipeCommandCaptor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    void testShowRecipeById() throws Exception {
        //given
        Long id = 2L;
        Recipe recipe;
        recipe = new Recipe();
        recipe.setId(id);
        recipe.setDescription("Desc1");

        given(recipeService.getById(anyLong())).willReturn(recipe);

        //when
        mockMvc.perform(get("/recipe/{id}/show", id))
                .andExpect(matchAll(
                        status().isOk(),
                        view().name("recipe/show"),
                        model().attribute("recipe", recipe)
                ));
        //then
        then(recipeService).should(times(1)).getById(eq(id));
        then(recipeService).should(never()).getAllRecipes();
        then(recipeService).shouldHaveNoMoreInteractions();
    }

    @Test
    void testGetNewRecipeForm() throws Exception {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(matchAll(
                        status().isOk(),
                        view().name("recipe/recipeform"),
                        model().attributeExists("recipe"),
                        model().attribute("recipe", notNull())
                ));
    }

    @Test
    void testPostNewRecipeForm() throws Exception {
        //given
        final long id = 2L;
        final String DESCRIPTION = "DDeessccrriippttiioonn";
        final int COOK_TIME = 12;

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(id);
        recipeCommand.setDescription(DESCRIPTION);
        recipeCommand.setCookTime(COOK_TIME);
        given(recipeService.saveRecipeCommand(any(RecipeCommand.class))).willReturn(recipeCommand);
        //when
        mockMvc
                .perform(
                        post("/recipe")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("description", DESCRIPTION)
                                .param("cookTime", String.valueOf(COOK_TIME))
                )
                .andExpect(
                        matchAll(
                                status().is3xxRedirection(),
                                redirectedUrlTemplate("/recipe/{id}/show", id)
                        )
                );
        //then
        then(recipeService).should(times(1)).saveRecipeCommand(recipeCommandCaptor.capture());
        RecipeCommand commandCaptorValue = recipeCommandCaptor.getValue();
        assertThat(commandCaptorValue.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(commandCaptorValue.getCookTime()).isEqualTo(COOK_TIME);
    }
}
