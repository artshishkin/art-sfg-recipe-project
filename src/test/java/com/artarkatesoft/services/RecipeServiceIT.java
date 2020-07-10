package com.artarkatesoft.services;

import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.converters.RecipeCommandToRecipeConverter;
import com.artarkatesoft.converters.RecipeToRecipeCommandConverter;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.repositories.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@DataJpaTest
@SpringBootTest
class RecipeServiceIT {

    public static final String NEW_DESCRIPTION = "new description";

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeToRecipeCommandConverter recipeToRecipeCommandConverter;

    @Autowired
    RecipeCommandToRecipeConverter recipeCommandToRecipeConverter;

    @Transactional
    @Test
    void testSaveOfDescription() {
        //given
        Recipe testRecipe = recipeService.getAllRecipes().iterator().next();
        RecipeCommand recipeCommand = recipeToRecipeCommandConverter.convert(testRecipe);
        //when
        recipeCommand.setDescription(NEW_DESCRIPTION);
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);
        //then
        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
        assertEquals(testRecipe.getId(), savedRecipeCommand.getId());
        assertThat(savedRecipeCommand.getCategories()).hasSameSizeAs(testRecipe.getCategories());
        assertThat(savedRecipeCommand.getIngredients()).hasSameSizeAs(testRecipe.getIngredients());

    }
}