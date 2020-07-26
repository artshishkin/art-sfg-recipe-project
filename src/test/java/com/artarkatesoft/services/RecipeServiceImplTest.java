package com.artarkatesoft.services;

import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.converters.RecipeToRecipeCommandConverter;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.exceptions.NotFoundException;
import com.artarkatesoft.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;
    @Mock
    RecipeToRecipeCommandConverter toRecipeCommandConverter;

    @InjectMocks
    RecipeServiceImpl recipeService;

    Set<Recipe> recipes;

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
        String id = "2L";
        Optional<Recipe> recipeOptional = recipes.stream().filter(rec -> id.equals(rec.getId())).findFirst();
        given(recipeRepository.findById(anyString())).willReturn(recipeOptional);
        //when
        Recipe foundRecipe = recipeService.getById(id);
        //then
        then(recipeRepository).should(times(1)).findById(eq(id));
        then(recipeRepository).should(never()).findAll();
        then(recipeRepository).shouldHaveNoMoreInteractions();
        assertThat(foundRecipe.getId()).isEqualTo(recipeOptional.get().getId());
    }

    @Test
    @DisplayName("when recipe with id does not exist then should throw NotFoundException")
    void getByIdWhenNotFound() {
        //given
        String id = "700L";
        given(recipeRepository.findById(anyString())).willReturn(Optional.empty());
        //when
        Executable findRecipeExecution = () -> recipeService.getById(id);
        //then
        assertThrows(NotFoundException.class, findRecipeExecution);
        then(recipeRepository).should(times(1)).findById(eq(id));
        then(recipeRepository).should(never()).findAll();
        then(recipeRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void testGetRecipeCommandById() {
        //given
        Recipe recipe = recipes.iterator().next();
        String id = recipe.getId();
        given(recipeRepository.findById(id)).willReturn(Optional.of(recipe));
        RecipeCommand recipeCommand = new RecipeCommand();
        BeanUtils.copyProperties(recipe, recipeCommand);
        given(toRecipeCommandConverter.convert(any(Recipe.class))).willReturn(recipeCommand);

        //when
        RecipeCommand foundRecipeCommand = recipeService.getCommandById(id);
        //then
        then(recipeRepository).should().findById(eq(id));
        then(toRecipeCommandConverter).should().convert(any(Recipe.class));
        assertNotNull(foundRecipeCommand);
        assertThat(foundRecipeCommand.getId()).isEqualTo(id);
    }

    @Test
    void testGetRecipeCommandByIdNotFound() {
        //given
        String id = "123L";
        given(recipeRepository.findById(anyString())).willReturn(Optional.empty());

        //when
        Executable executable = () -> recipeService.getCommandById(id);
        //then
        assertThrows(NotFoundException.class, executable);
    }

    @Test
    void testDeleteById() {
        //given
        String id = "1L";
        //when
        recipeService.deleteById(id);
        //then
        then(recipeRepository).should().deleteById(eq(id));
    }
}
