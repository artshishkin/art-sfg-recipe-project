package com.artarkatesoft.converters;

import com.artarkatesoft.commands.UnitOfMeasureCommand;
import com.artarkatesoft.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureCommandToUnitOfMeasureConverterTest {

    private static UnitOfMeasureCommandToUnitOfMeasureConverter converter;
    private static UnitOfMeasureCommand uomCommand;

    @BeforeAll
    static void setUp() {
        converter = new UnitOfMeasureCommandToUnitOfMeasureConverter();
        uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId(100L);
        uomCommand.setDescription("Desc");
    }

    @Test
    void testNullSource() {
        assertNull(converter.convert(null));
    }

    @Test
    void testNotNullSource() {
        assertNotNull(converter.convert(uomCommand));
    }

    @Test
    void convert() {
        UnitOfMeasure uom = converter.convert(uomCommand);

        assertNotNull(uom);
        assertAll(
                () -> assertEquals(uomCommand.getId(), uom.getId()),
                () -> assertEquals(uomCommand.getDescription(), uom.getDescription())
        );

    }
}