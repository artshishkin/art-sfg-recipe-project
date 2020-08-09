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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @InjectMocks
    RecipeController recipeController;

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    @Captor
    ArgumentCaptor<RecipeCommand> recipeCommandCaptor;

    private static final String ID = "2L";
    private static final String DESCRIPTION = "DDeessccrriippttiioonn";
    private static final int COOK_TIME = 12;
    private RecipeCommand recipeCommand;

    @BeforeEach
    void setUp() {
        recipeCommand = new RecipeCommand();
        recipeCommand.setId(ID);
        recipeCommand.setDescription(DESCRIPTION);
        recipeCommand.setCookTime(COOK_TIME);
    }

    @Test
    @DisplayName("when Recipe found should be OK")
    void testShowRecipeById() throws Exception {
        //given
        Recipe recipe;
        recipe = new Recipe();
        recipe.setId(ID);
        recipe.setDescription("Desc1");

        Mono<Recipe> recipeMono = Mono.just(recipe);
        given(recipeService.getById(anyString())).willReturn(recipeMono);

        //when
        String view = recipeController.showById(ID, model);

        //then
        then(recipeService).should(times(1)).getById(eq(ID));
        then(recipeService).should(never()).getAllRecipes();
        then(recipeService).shouldHaveNoMoreInteractions();
        assertThat(view).isEqualTo("recipe/show");
        then(model).should().addAttribute(eq("recipe"), eq(recipeMono));
    }

    @Test
    @DisplayName("when Recipe not found should return view 404error")
    void testShowRecipeByIdWhenNotFound() throws Exception {
        //given
        given(recipeService.getById(anyString())).willThrow(NotFoundException.class);

        //when
        String view;
        try {
            view = recipeController.showById(ID, model);
        } catch (NotFoundException ex) {
            view = recipeController.handleNotFound(ex, model);
        }

        //then
        then(recipeService).should(times(1)).getById(eq(ID));
        then(recipeService).should(never()).getAllRecipes();
        then(recipeService).shouldHaveNoMoreInteractions();
        then(model).should().addAttribute(eq("exception"), any(NotFoundException.class));
        assertThat(view).isEqualTo("404error");
    }


    @Test
    void testGetNewRecipeForm() throws Exception {
        //given
        //when
        String view = recipeController.newRecipe(model);
        //then
        then(model).should().addAttribute(eq("recipe"), any(RecipeCommand.class));
        assertThat(view).isEqualTo(RecipeController.RECIPE_RECIPEFORM_URL);
    }

    @Test
    void testPostNewRecipe() throws Exception {
        //given
        given(recipeService.saveRecipeCommand(any(RecipeCommand.class))).willReturn(Mono.just(recipeCommand));

        //when
        initWebDataBinder(false);
        String view = recipeController.createOrUpdate(recipeCommand);

        //then
        then(recipeService).should(times(1)).saveRecipeCommand(recipeCommandCaptor.capture());
        RecipeCommand commandCaptorValue = recipeCommandCaptor.getValue();
        assertThat(commandCaptorValue.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(commandCaptorValue.getCookTime()).isEqualTo(COOK_TIME);
        assertThat(view).isEqualTo("redirect:/recipe/" + ID + "/show");
    }

    @Test
    void testPostNewRecipeFormValidationFail() throws Exception {
        //when
        initWebDataBinder(true);
        String view = recipeController.createOrUpdate(recipeCommand);

        //then
        then(recipeService).shouldHaveNoInteractions();
        assertThat(view).isEqualTo(RecipeController.RECIPE_RECIPEFORM_URL);
    }


    private void initWebDataBinder(boolean hasErrors) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        WebDataBinder webDataBinder = mock(WebDataBinder.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(webDataBinder.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(hasErrors);
        Method initBinder = recipeController.getClass().getDeclaredMethod("initBinder", WebDataBinder.class);
        initBinder.setAccessible(true);
        initBinder.invoke(recipeController, webDataBinder);
    }

    @Test
    void testUpdateRecipeForm() throws Exception {
        //given
        Mono<RecipeCommand> recipeCommandMono = Mono.just(recipeCommand);
        given(recipeService.getCommandById(ID)).willReturn(recipeCommandMono);

        //when
        String view = recipeController.updateRecipe(ID, model);

        //then
        then(recipeService).should().getCommandById(eq(ID));
        then(model).should().addAttribute(eq("recipe"),eq(recipeCommandMono));
        assertThat(view).isEqualTo(RecipeController.RECIPE_RECIPEFORM_URL);
    }

    @Test
    void testDeleteById() throws Exception {
        //when
        String view = recipeController.deleteRecipe(ID);

        //then
        then(recipeService).should(times(1)).deleteById(eq(ID));
        assertThat(view).isEqualTo("redirect:/");
    }
}
