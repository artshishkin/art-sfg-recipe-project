package com.artarkatesoft.converters;

import com.artarkatesoft.commands.CategoryCommand;
import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.commands.NotesCommand;
import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.domain.Category;
import com.artarkatesoft.domain.Ingredient;
import com.artarkatesoft.domain.Notes;
import com.artarkatesoft.domain.Recipe;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecipeCommandToRecipeConverter extends AbstractConverter<RecipeCommand, Recipe> {

    private final Converter<IngredientCommand, Ingredient> ingredientConverter;
    private final Converter<NotesCommand, Notes> notesConverter;
    private final Converter<CategoryCommand, Category> categoryConverter;

    @Nullable
    @Synchronized
    @Override
    public Recipe convert(RecipeCommand recipeCommand) {
        Recipe recipe = super.convert(recipeCommand);
        if (recipe == null) return null;

        List<IngredientCommand> ingredients = recipeCommand.getIngredients();
        if (ingredients != null) {
            ingredients.stream()
                    .map(ingredientConverter::convert)
                    .forEach(recipe::addIngredient);
        }
        recipe.setNotes(notesConverter.convert(recipeCommand.getNotes()));

        List<CategoryCommand> categories = recipeCommand.getCategories();
        if (categories != null) {
            categories.stream()
                    .map(categoryConverter::convert)
                    .forEach(recipe::addCategory);
        }
        return recipe;
    }
}
