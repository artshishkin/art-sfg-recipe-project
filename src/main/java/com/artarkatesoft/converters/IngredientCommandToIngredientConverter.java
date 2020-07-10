package com.artarkatesoft.converters;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.commands.UnitOfMeasureCommand;
import com.artarkatesoft.domain.Ingredient;
import com.artarkatesoft.domain.UnitOfMeasure;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IngredientCommandToIngredientConverter extends AbstractConverter<IngredientCommand, Ingredient> {

    private final Converter<UnitOfMeasureCommand, UnitOfMeasure> uomConverter;

    public IngredientCommandToIngredientConverter(Converter<UnitOfMeasureCommand, UnitOfMeasure> uomConverter) {
        this.uomConverter = uomConverter;
    }

    @Override
    public Ingredient convert(IngredientCommand ingredientCommand) {
        Ingredient ingredient = super.convert(ingredientCommand);
        if (ingredient == null) return null;
        UnitOfMeasure unitOfMeasure = uomConverter.convert(ingredientCommand.getUom());
        ingredient.setUom(unitOfMeasure);
        return ingredient;
    }
}
