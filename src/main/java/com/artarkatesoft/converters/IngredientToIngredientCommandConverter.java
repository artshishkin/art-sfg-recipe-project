package com.artarkatesoft.converters;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.domain.Ingredient;
import org.springframework.stereotype.Component;

@Component
public class IngredientToIngredientCommandConverter extends AbstractConverter<Ingredient, IngredientCommand> {
}
