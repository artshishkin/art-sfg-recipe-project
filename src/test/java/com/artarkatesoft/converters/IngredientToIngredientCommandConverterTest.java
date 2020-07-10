package com.artarkatesoft.converters;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.domain.Ingredient;
import com.artarkatesoft.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientToIngredientCommandConverterTest {
    private static IngredientToIngredientCommandConverter converter;
    private static Ingredient ingredient;

    @BeforeAll
    static void setUp() {
        converter = new IngredientToIngredientCommandConverter(new UnitOfMeasureToUnitOfMeasureCommandConverter());
        ingredient = new Ingredient();
        ingredient.setId(100L);
        ingredient.setDescription("Desc");
        ingredient.setAmount(BigDecimal.valueOf(123));

        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(2L);
        uom.setDescription("Litre");
        ingredient.setUom(uom);
    }

    @Test
    void testNullSource() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptySource() {
        assertNotNull(converter.convert(new Ingredient()));
    }

    @Test
    void convert() {
        IngredientCommand ingredientCommand = converter.convert(ingredient);

        assertNotNull(ingredientCommand);
        assertAll(
                () -> assertEquals(ingredient.getId(), ingredientCommand.getId()),
                () -> assertEquals(ingredient.getDescription(), ingredientCommand.getDescription()),
                () -> assertEquals(ingredient.getAmount(), ingredientCommand.getAmount()),
                () -> assertEquals(ingredient.getUom().getId(), ingredientCommand.getUom().getId()),
                () -> assertEquals(ingredient.getUom().getDescription(), ingredientCommand.getUom().getDescription())

        );
    }
}
