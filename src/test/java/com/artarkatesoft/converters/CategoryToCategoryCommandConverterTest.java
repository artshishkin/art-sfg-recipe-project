package com.artarkatesoft.converters;

import com.artarkatesoft.commands.CategoryCommand;
import com.artarkatesoft.domain.Category;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryToCategoryCommandConverterTest {
    private static CategoryToCategoryCommandConverter converter;
    private static Category category;

    @BeforeAll
    static void setUp() {
        converter = new CategoryToCategoryCommandConverter();
        category = new Category();
        category.setId(100L);
        category.setDescription("Desc");
    }

    @Test
    void testNullSource() {
        assertNull(converter.convert(null));
    }

    @Test
    void testNotNullSource() {
        assertNotNull(converter.convert(category));
    }

    @Test
    void convert() {
        CategoryCommand categoryCommand = converter.convert(category);

        assertNotNull(categoryCommand);
        assertAll(
                () -> assertEquals(category.getId(), categoryCommand.getId()),
                () -> assertEquals(category.getDescription(), categoryCommand.getDescription())
        );

    }

}