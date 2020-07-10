package com.artarkatesoft.converters;

import com.artarkatesoft.commands.CategoryCommand;
import com.artarkatesoft.domain.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryCommandToCategoryConverter extends AbstractConverter<CategoryCommand, Category> {
}
