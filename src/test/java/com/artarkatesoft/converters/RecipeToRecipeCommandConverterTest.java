package com.artarkatesoft.converters;

import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.domain.Difficulty;
import com.artarkatesoft.domain.Recipe;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeToRecipeCommandConverterTest {
    private static RecipeToRecipeCommandConverter converter;
    private static Recipe recipe;

    @BeforeAll
    static void setUp() {
        converter = new RecipeToRecipeCommandConverter();
        recipe = new Recipe();
        recipe.setId(100L);
        recipe.setDescription("Desc");
        recipe.setCookTime(12);
        recipe.setPrepTime(13);
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setServings(4);
        recipe.setSource("source");
    }

    @Test
    void testNullSource() {
        assertNull(converter.convert(null));
    }

    @Test
    void testNotNullSource() {
        assertNotNull(converter.convert(recipe));
    }

    @Test
    void convert() {
        RecipeCommand recipeCommand = converter.convert(recipe);

        assertNotNull(recipeCommand);
        assertAll(
                () -> assertEquals(recipe.getId(), recipeCommand.getId()),
                () -> assertEquals(recipe.getDescription(), recipeCommand.getDescription()),
                () -> assertEquals(recipe.getDirections(), recipeCommand.getDirections()),
                () -> assertEquals(recipe.getDifficulty(), recipeCommand.getDifficulty()),
                () -> assertEquals(recipe.getCookTime(), recipeCommand.getCookTime()),
                () -> assertEquals(recipe.getPrepTime(), recipeCommand.getPrepTime()),
                () -> assertEquals(recipe.getServings(), recipeCommand.getServings()),
                () -> assertEquals(recipe.getUrl(), recipeCommand.getUrl()),
                () -> assertEquals(recipe.getSource(), recipeCommand.getSource())

        );

    }


}