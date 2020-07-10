package com.artarkatesoft.converters;

import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.domain.Recipe;
import org.springframework.stereotype.Component;

@Component
public class RecipeToRecipeCommandConverter extends AbstractConverter<Recipe, RecipeCommand> {
}
