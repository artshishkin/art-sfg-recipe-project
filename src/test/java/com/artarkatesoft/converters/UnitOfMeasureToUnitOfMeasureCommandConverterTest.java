package com.artarkatesoft.converters;

import com.artarkatesoft.commands.UnitOfMeasureCommand;
import com.artarkatesoft.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureToUnitOfMeasureCommandConverterTest {

    private static UnitOfMeasureToUnitOfMeasureCommandConverter converter;
    private static UnitOfMeasure uom;

    @BeforeAll
    static void setUp() {
        converter = new UnitOfMeasureToUnitOfMeasureCommandConverter();
        uom = new UnitOfMeasure();
        uom.setId(100L);
        uom.setDescription("Desc");
    }

    @Test
    void testNullSource() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptySource() {
        assertNotNull(converter.convert(new UnitOfMeasure()));
    }

    @Test
    void convert() {
        UnitOfMeasureCommand unitOfMeasureCommand = converter.convert(uom);

        assertNotNull(unitOfMeasureCommand);
        assertAll(
                () -> assertEquals(uom.getId(), unitOfMeasureCommand.getId()),
                () -> assertEquals(uom.getDescription(), unitOfMeasureCommand.getDescription())
        );

    }
}
