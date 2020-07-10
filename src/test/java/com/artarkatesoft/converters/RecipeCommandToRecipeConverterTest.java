package com.artarkatesoft.converters;

import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.domain.Difficulty;
import com.artarkatesoft.domain.Recipe;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeCommandToRecipeConverterTest {
    private static RecipeCommandToRecipeConverter converter;
    private static RecipeCommand recipeCommand;

    @BeforeAll
    static void setUp() {
        converter = new RecipeCommandToRecipeConverter();
        recipeCommand = new RecipeCommand();
        recipeCommand.setId(100L);
        recipeCommand.setDescription("Desc");
        recipeCommand.setCookTime(12);
        recipeCommand.setPrepTime(13);
        recipeCommand.setDifficulty(Difficulty.EASY);
        recipeCommand.setServings(4);
        recipeCommand.setSource("source");

    }

    @Test
    void testNullSource() {
        assertNull(converter.convert(null));
    }

    @Test
    void testNotNullSource() {
        assertNotNull(converter.convert(recipeCommand));
    }

    @Test
    void convert() {
        Recipe recipe = converter.convert(recipeCommand);

        assertNotNull(recipe);
        assertAll(
                () -> assertEquals(recipeCommand.getId(), recipe.getId()),
                () -> assertEquals(recipeCommand.getDescription(), recipe.getDescription()),
                () -> assertEquals(recipeCommand.getDirections(), recipe.getDirections()),
                () -> assertEquals(recipeCommand.getDifficulty(), recipe.getDifficulty()),
                () -> assertEquals(recipeCommand.getCookTime(), recipe.getCookTime()),
                () -> assertEquals(recipeCommand.getPrepTime(), recipe.getPrepTime()),
                () -> assertEquals(recipeCommand.getServings(), recipe.getServings()),
                () -> assertEquals(recipeCommand.getUrl(), recipe.getUrl()),
                () -> assertEquals(recipeCommand.getSource(), recipe.getSource())

        );

    }

}