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
            List<IngredientCommand> ingredientCommandList = ingredients.stream()
                    .map(ingredientConverter::convert)
                    .collect(Collectors.toList());
            recipeCommand.setIngredients(ingredientCommandList);
        }
        recipeCommand.setNotes(notesConverter.convert(recipe.getNotes()));

        Set<Category> categories = recipe.getCategories();
        if (categories != null) {
            List<CategoryCommand> categoryCommandList = categories.stream()
                    .map(categoryConverter::convert)
                    .collect(Collectors.toList());
            recipeCommand.setCategories(categoryCommandList);
        }
        return recipeCommand;
    }
}

