package com.artarkatesoft.converters;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.domain.Ingredient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientToIngredientCommandConverterTest {
    private static IngredientToIngredientCommandConverter converter;
    private static Ingredient ingredient;

    @BeforeAll
    static void setUp() {
        converter = new IngredientToIngredientCommandConverter();
        ingredient = new Ingredient();
        ingredient.setId(100L);
        ingredient.setDescription("Desc");
        ingredient.setAmount(BigDecimal.valueOf(123));
    }

    @Test
    void testNullSource() {
        assertNull(converter.convert(null));
    }

    @Test
    void testNotNullSource() {
        assertNotNull(converter.convert(ingredient));
    }

    @Test
    void convert() {
        IngredientCommand command = converter.convert(ingredient);

        assertNotNull(command);
        assertAll(
                () -> assertEquals(ingredient.getId(), command.getId()),
                () -> assertEquals(ingredient.getDescription(), command.getDescription()),
                () -> assertEquals(ingredient.getAmount(), command.getAmount())
        );
    }
}