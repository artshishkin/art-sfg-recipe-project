package com.artarkatesoft.converters;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.domain.Ingredient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientCommandToIngredientConverterTest {
    private static IngredientCommandToIngredientConverter converter;
    private static IngredientCommand ingredientCommand;

    @BeforeAll
    static void setUp() {
        converter = new IngredientCommandToIngredientConverter();
        ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(100L);
        ingredientCommand.setDescription("Desc");
        ingredientCommand.setAmount(BigDecimal.valueOf(123));
    }

    @Test
    void testNullSource() {
        assertNull(converter.convert(null));
    }

    @Test
    void testNotNullSource() {
        assertNotNull(converter.convert(ingredientCommand));
    }

    @Test
    void convert() {
        Ingredient ingredient = converter.convert(ingredientCommand);

        assertNotNull(ingredient);
        assertAll(
                () -> assertEquals(ingredientCommand.getId(), ingredient.getId()),
                () -> assertEquals(ingredientCommand.getDescription(), ingredient.getDescription()),
                () -> assertEquals(ingredientCommand.getAmount(), ingredient.getAmount())
        );
    }
}