package com.artarkatesoft.converters;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.commands.UnitOfMeasureCommand;
import com.artarkatesoft.domain.Ingredient;
import com.artarkatesoft.domain.UnitOfMeasure;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IngredientToIngredientCommandConverter extends AbstractConverter<Ingredient, IngredientCommand> {

    private final Converter<UnitOfMeasure, UnitOfMeasureCommand> uomConverter;

    @Nullable
    @Synchronized
    @Override
    public IngredientCommand convert(Ingredient ingredient) {
        IngredientCommand ingredientCommand = super.convert(ingredient);
        if (ingredientCommand == null) return null;
        UnitOfMeasureCommand unitOfMeasureCommand = uomConverter.convert(ingredient.getUom());
        ingredientCommand.setUom(unitOfMeasureCommand);
        return ingredientCommand;
    }
}
