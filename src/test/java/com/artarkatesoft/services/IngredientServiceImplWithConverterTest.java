package com.artarkatesoft.services;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.converters.IngredientCommandToIngredientConverter;
import com.artarkatesoft.converters.IngredientToIngredientCommandConverter;
import com.artarkatesoft.converters.UnitOfMeasureCommandToUnitOfMeasureConverter;
import com.artarkatesoft.converters.UnitOfMeasureToUnitOfMeasureCommandConverter;
import com.artarkatesoft.domain.Ingredient;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.repositories.RecipeRepository;
import com.artarkatesoft.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
    }

    @Test
    void findIngredientCommandByIdAndRecipeId() {
        //given
        Long id = 2L;
        Long recipeId = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);

        LongStream.rangeClosed(1, 5)
                .mapToObj(this::createFakeIngredient)
                .forEach(recipe::addIngredient);

        given(recipeRepository.findById(anyLong())).willReturn(Optional.of(recipe));
        //when
        IngredientCommand ingredientCommand = ingredientService.findIngredientCommandByIdAndRecipeId(id, recipeId);
        //then
        then(recipeRepository).should().findById(eq(recipeId));
        assertThat(ingredientCommand.getId()).isEqualTo(id);
        assertThat(ingredientCommand.getRecipeId()).isEqualTo(recipeId);

    }

    private Ingredient createFakeIngredient(Long id) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setDescription("Desc" + id);
        return ingredient;
    }
}
