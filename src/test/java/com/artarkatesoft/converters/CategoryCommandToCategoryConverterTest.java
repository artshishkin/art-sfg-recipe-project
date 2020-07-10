package com.artarkatesoft.converters;

import com.artarkatesoft.commands.CategoryCommand;
import com.artarkatesoft.domain.Category;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryCommandToCategoryConverterTest {
    private static CategoryCommandToCategoryConverter converter;
    private static CategoryCommand categoryCommand;

    @BeforeAll
    static void setUp() {
        converter = new CategoryCommandToCategoryConverter();
        categoryCommand = new CategoryCommand();
        categoryCommand.setId(100L);
        categoryCommand.setDescription("Desc");
    }

    @Test
    void testNullSource() {
        assertNull(converter.convert(null));
    }

    @Test
    void testNotNullSource() {
        assertNotNull(converter.convert(categoryCommand));
    }

    @Test
    void convert() {
        Category category = converter.convert(categoryCommand);

        assertNotNull(category);
        assertAll(
                () -> assertEquals(categoryCommand.getId(), category.getId()),
                () -> assertEquals(categoryCommand.getDescription(), category.getDescription())
        );

    }

}