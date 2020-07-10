package com.artarkatesoft.converters;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.commands.UnitOfMeasureCommand;
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
        converter = new IngredientCommandToIngredientConverter(new UnitOfMeasureCommandToUnitOfMeasureConverter());
        ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(100L);
        ingredientCommand.setDescription("Desc");
        ingredientCommand.setAmount(BigDecimal.valueOf(123));
        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId(2L);
        uomCommand.setDescription("Desc3");
        ingredientCommand.setUom(uomCommand);
    }

    @Test
    void testNullSource() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptySource() {
        assertNotNull(converter.convert(new IngredientCommand()));
    }

    @Test
    void convert() {
        Ingredient ingredient = converter.convert(ingredientCommand);

        assertNotNull(ingredient);
        assertAll(
                () -> assertEquals(ingredientCommand.getId(), ingredient.getId()),
                () -> assertEquals(ingredientCommand.getDescription(), ingredient.getDescription()),
                () -> assertEquals(ingredientCommand.getAmount(), ingredient.getAmount()),
                () -> assertNotNull(ingredient.getUom()),
                () -> assertEquals(ingredientCommand.getUom().getId(), ingredient.getUom().getId()),
                () -> assertEquals(ingredientCommand.getUom().getDescription(), ingredient.getUom().getDescription())

        );
    }
}
