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

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecipeToRecipeCommandConverter extends AbstractConverter<Recipe, RecipeCommand> {

    private final Converter<Ingredient, IngredientCommand> ingredientConverter;
    private final Converter<Notes, NotesCommand> notesConverter;
    private final Converter<Category, CategoryCommand> categoryConverter;

    @Nullable
    @Synchronized
    @Override
    public RecipeCommand convert(Recipe recipe) {
        RecipeCommand recipeCommand = super.convert(recipe);
        if (recipeCommand == null) return null;
        Set<Ingredient> ingredients = recipe.getIngredients();
        if (ingredients != null) {
            Set<IngredientCommand> ingredientCommandSet = ingredients.stream()
                    .map(ingredientConverter::convert)
                    .collect(Collectors.toSet());
            recipeCommand.setIngredients(ingredientCommandSet);
        }
        recipeCommand.setNotes(notesConverter.convert(recipe.getNotes()));

        Set<Category> categories = recipe.getCategories();
        if (categories != null) {
            Set<CategoryCommand> categoryCommandSet = categories.stream()
                    .map(categoryConverter::convert)
                    .collect(Collectors.toSet());
            recipeCommand.setCategories(categoryCommandSet);
        }
        return recipeCommand;
    }
}

