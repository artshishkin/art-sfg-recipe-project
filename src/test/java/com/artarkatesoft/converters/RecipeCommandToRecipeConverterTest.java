package com.artarkatesoft.converters;

import com.artarkatesoft.commands.*;
import com.artarkatesoft.domain.Difficulty;
import com.artarkatesoft.domain.Recipe;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RecipeCommandToRecipeConverterTest {
    private static RecipeCommandToRecipeConverter converter;
    private static RecipeCommand recipeCommand;

    @BeforeAll
    static void setUp() {

        UnitOfMeasureCommandToUnitOfMeasureConverter uomConverter = new UnitOfMeasureCommandToUnitOfMeasureConverter();
        IngredientCommandToIngredientConverter ingredientConverter = new IngredientCommandToIngredientConverter(uomConverter);
        NotesCommandToNotesConverter notesConverter = new NotesCommandToNotesConverter();
        CategoryCommandToCategoryConverter categoryConverter = new CategoryCommandToCategoryConverter();

        converter = new RecipeCommandToRecipeConverter(ingredientConverter, notesConverter, categoryConverter);
        recipeCommand = new RecipeCommand();
        recipeCommand.setId(100L);
        recipeCommand.setDescription("Desc");
        recipeCommand.setCookTime(12);
        recipeCommand.setPrepTime(13);
        recipeCommand.setDifficulty(Difficulty.EASY);
        recipeCommand.setServings(4);
        recipeCommand.setSource("source");

        CategoryCommand category;
        category = new CategoryCommand();
        category.setId(3L);
        category.setDescription("CatDesc3");
        recipeCommand.getCategories().add(category);
        category = new CategoryCommand();
        category.setId(4L);
        category.setDescription("CatDesc4");
        recipeCommand.getCategories().add(category);

        IngredientCommand ingredientCommand;
        ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(8L);
        ingredientCommand.setDescription("ddd");
        ingredientCommand.setAmount(BigDecimal.valueOf(123));
        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId(1L);
        uomCommand.setDescription("qwert");
        ingredientCommand.setUom(uomCommand);
        recipeCommand.getIngredients().add(ingredientCommand);
        ingredientCommand.setId(7L);
        ingredientCommand.setDescription("asd");
        ingredientCommand.setAmount(BigDecimal.valueOf(124));
        uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId(4L);
        uomCommand.setDescription("erty");
        ingredientCommand.setUom(uomCommand);
        recipeCommand.getIngredients().add(ingredientCommand);

        NotesCommand notes = new NotesCommand();
        notes.setId(1L);
        notes.setNotes("notesnotes");
        recipeCommand.setNotes(notes);

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
        assertAll(
                () -> assertThat(recipe.getCategories()).isNotNull().hasSameSizeAs(recipeCommand.getCategories()),
                () -> assertThat(recipe.getIngredients()).isNotNull().hasSameSizeAs(recipeCommand.getIngredients())
        );


        recipeCommand.getCategories()
                .forEach(categoryCommand ->
                        assertTrue(
                                () -> recipe.getCategories()
                                        .stream()
                                        .anyMatch(category -> Objects.equals(category.getId(), categoryCommand.getId()) && Objects.equals(category.getDescription(), categoryCommand.getDescription())))
                );
        recipeCommand.getIngredients()
                .forEach(ingredientCommand ->
                        assertTrue(
                                () -> recipe.getIngredients()
                                        .stream()
                                        .anyMatch(ingredient -> Objects.equals(ingredient.getId(), ingredientCommand.getId()) && Objects.equals(ingredient.getDescription(), ingredientCommand.getDescription())))
                );
    }

}