package com.artarkatesoft.controllers;

import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.exceptions.NotFoundException;
import com.artarkatesoft.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;
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

    private static final long ID = 2L;
    private static final String DESCRIPTION = "DDeessccrriippttiioonn";
    private static final int COOK_TIME = 12;
    private RecipeCommand recipeCommand;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();

        recipeCommand = new RecipeCommand();
        recipeCommand.setId(ID);
        recipeCommand.setDescription(DESCRIPTION);
        recipeCommand.setCookTime(COOK_TIME);
    }

    @Test
    void testShowRecipeById() throws Exception {
        //given
        Recipe recipe;
        recipe = new Recipe();
        recipe.setId(ID);
        recipe.setDescription("Desc1");

        given(recipeService.getById(anyLong())).willReturn(recipe);

        //when
        mockMvc.perform(get("/recipe/{id}/show", ID))
                .andExpect(matchAll(
                        status().isOk(),
                        view().name("recipe/show"),
                        model().attribute("recipe", recipe)
                ));
        //then
        then(recipeService).should(times(1)).getById(eq(ID));
        then(recipeService).should(never()).getAllRecipes();
        then(recipeService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("when Recipe not found should return Status 404")
    void testShowRecipeByIdWhenNotFound() throws Exception {
        //given
        given(recipeService.getById(anyLong())).willThrow(NotFoundException.class);

        //when
        mockMvc.perform(get("/recipe/{id}/show", ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(view().name("404error"));
        //then
        then(recipeService).should(times(1)).getById(eq(ID));
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
                        model().attribute("recipe", notNullValue(RecipeCommand.class))
                ));
    }

    @Test
    void testPostNewRecipeForm() throws Exception {
        //given


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
                                redirectedUrlTemplate("/recipe/{id}/show", ID)
                        )
                );
        //then
        then(recipeService).should(times(1)).saveRecipeCommand(recipeCommandCaptor.capture());
        RecipeCommand commandCaptorValue = recipeCommandCaptor.getValue();
        assertThat(commandCaptorValue.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(commandCaptorValue.getCookTime()).isEqualTo(COOK_TIME);
    }

    @Test
    void testUpdateRecipeForm() throws Exception {
        //given
        given(recipeService.getCommandById(ID)).willReturn(recipeCommand);

        //when
        mockMvc.perform(get("/recipe/{id}/update", ID))
                .andExpect(matchAll(
                        status().isOk(),
                        view().name("recipe/recipeform"),
                        model().attributeExists("recipe"),
                        model().attribute("recipe", notNullValue(RecipeCommand.class))
                ));
        //then
        then(recipeService).should().getCommandById(eq(ID));
    }

    @Test
    void testDeleteById() throws Exception {
        //given
        //when
        mockMvc.perform(get("/recipe/{id}/delete", ID))
                .andExpect(matchAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/")
                ));
        //then
        then(recipeService).should(times(1)).deleteById(eq(ID));
    }
}
