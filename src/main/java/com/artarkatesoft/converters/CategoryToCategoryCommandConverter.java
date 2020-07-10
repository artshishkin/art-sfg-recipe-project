package com.artarkatesoft.converters;

import com.artarkatesoft.commands.CategoryCommand;
import com.artarkatesoft.domain.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryCommandConverter extends AbstractConverter<Category, CategoryCommand> {
}
