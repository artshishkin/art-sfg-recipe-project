package com.artarkatesoft.services;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.commands.UnitOfMeasureCommand;
import com.artarkatesoft.converters.IngredientCommandToIngredientConverter;
import com.artarkatesoft.converters.IngredientToIngredientCommandConverter;
import com.artarkatesoft.converters.UnitOfMeasureCommandToUnitOfMeasureConverter;
import com.artarkatesoft.converters.UnitOfMeasureToUnitOfMeasureCommandConverter;
import com.artarkatesoft.domain.Ingredient;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.domain.UnitOfMeasure;
import com.artarkatesoft.repositories.RecipeRepository;
import com.artarkatesoft.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplWithConverterTest {

    IngredientServiceImpl ingredientService;

    @Mock
    RecipeRepository recipeRepository;
    @Mock
    UnitOfMeasureRepository uomRepository;

    IngredientToIngredientCommandConverter toIngredientCommandConverter;
    IngredientCommandToIngredientConverter toIngredientConverter;

    private Recipe recipe;

    @BeforeEach
    void setUp() {
        toIngredientCommandConverter = new IngredientToIngredientCommandConverter(
                new UnitOfMeasureToUnitOfMeasureCommandConverter()
        );
        toIngredientConverter = new IngredientCommandToIngredientConverter(
                new UnitOfMeasureCommandToUnitOfMeasureConverter()
        );
        ingredientService = new IngredientServiceImpl(recipeRepository,
                uomRepository, toIngredientCommandConverter, toIngredientConverter);
        String recipeId = "1";

        recipe = new Recipe();
        LongStream.rangeClosed(1, 5)
                .mapToObj(String::valueOf)
                .map(this::createFakeIngredient)
                .forEach(recipe::addIngredient);
        recipe.setId(recipeId);

    }

    @Test
    void findIngredientCommandByIdAndRecipeId() {
        //given
        String id = "2";
        String recipeId = "100";
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);

        LongStream.rangeClosed(1, 5)
                .mapToObj(String::valueOf)
                .map(this::createFakeIngredient)
                .forEach(recipe::addIngredient);

        given(recipeRepository.findById(anyString())).willReturn(Optional.of(recipe));
        //when
        IngredientCommand ingredientCommand = ingredientService.findIngredientCommandByIdAndRecipeId(id, recipeId);
        //then
        then(recipeRepository).should().findById(eq(recipeId));
        assertThat(ingredientCommand.getId()).isEqualTo(id);
        assertThat(ingredientCommand.getRecipeId()).isEqualTo(recipeId);

    }

    private Ingredient createFakeIngredient(String id) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setDescription("Desc" + id);
        return ingredient;
    }

    @Test
    public void saveIngredientCommand() {
        //given
        String recipeId = recipe.getId();
        String id = "2";
        Ingredient ingredientRepo = recipe.getIngredients()
                .stream()
                .filter(ingredient -> Objects.equals(ingredient.getId(), id))
                .findAny().get();
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId("12L");
        uom.setDescription("BBottle");

        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId("12L");
        uomCommand.setDescription("BBottle");

        IngredientCommand commandToSave = new IngredientCommand(id, recipeId, "New Description", BigDecimal.valueOf(333), uomCommand);

        given(recipeRepository.findById(anyString())).willReturn(Optional.of(recipe));
        given(recipeRepository.save(any(Recipe.class))).willReturn(recipe);
        given(uomRepository.findById(anyString())).willReturn(Optional.of(uom));
        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(commandToSave);
        //then
        then(recipeRepository).should().findById(anyString());
        then(uomRepository).should().findById(anyString());
        then(recipeRepository).should().save(any(Recipe.class));
        assertAll(
                () -> assertThat(savedCommand.getId()).isEqualTo(ingredientRepo.getId()),
                () -> assertThat(savedCommand.getRecipeId()).isEqualTo(recipeId),
                () -> assertThat(savedCommand.getDescription()).isEqualTo(ingredientRepo.getDescription()),
                () -> assertThat(savedCommand.getUom().getId()).isEqualTo(ingredientRepo.getUom().getId())
        );
    }
}
