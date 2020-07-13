package com.artarkatesoft.services;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.commands.UnitOfMeasureCommand;
import com.artarkatesoft.converters.IngredientToIngredientCommandConverter;
import com.artarkatesoft.domain.Ingredient;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.domain.UnitOfMeasure;
import com.artarkatesoft.repositories.RecipeRepository;
import com.artarkatesoft.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

    @InjectMocks
    IngredientServiceImpl ingredientService;

    @Mock
    RecipeRepository recipeRepository;
    @Mock
    UnitOfMeasureRepository uomRepository;
    @Mock
    IngredientToIngredientCommandConverter toIngredientCommandConverter;
    @Captor
    ArgumentCaptor<Ingredient> ingredientCaptor;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        Long recipeId = 1L;

        recipe = new Recipe();
        LongStream.rangeClosed(1, 5)
                .mapToObj(this::createFakeIngredient)
                .forEach(recipe::addIngredient);
        recipe.setId(recipeId);
    }

    @Test
    void findIngredientCommandByIdAndRecipeId() {
        //given
        Long id = 2L;
        Long recipeId = recipe.getId();

        given(recipeRepository.findById(anyLong())).willReturn(Optional.of(recipe));
        given(toIngredientCommandConverter.convert(any(Ingredient.class))).willReturn(new IngredientCommand());
        //when
        IngredientCommand ingredientCommand = ingredientService.findIngredientCommandByIdAndRecipeId(id, recipeId);
        //then
        then(recipeRepository).should().findById(eq(recipe.getId()));
        then(toIngredientCommandConverter).should(times(1)).convert(ingredientCaptor.capture());
        assertThat(ingredientCaptor.getValue().getId()).isEqualTo(id);

    }

    private Ingredient createFakeIngredient(Long id) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setDescription("Desc" + id);
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(222L);
        uom.setDescription("uom desc");
        ingredient.setUom(uom);
        return ingredient;
    }

    @Test
    public void saveIngredientCommand() {
        //given
        Long recipeId = recipe.getId();
        Long id = 2L;
        Ingredient ingredientRepo = recipe.getIngredients()
                .stream()
                .filter(ingredient -> Objects.equals(ingredient.getId(), id))
                .findAny().get();
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(12L);
        uom.setDescription("BBottle");

        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId(12L);
        uomCommand.setDescription("BBottle");

        IngredientCommand commandToSave = new IngredientCommand(id, recipeId, "New Description", BigDecimal.valueOf(333), uomCommand);

        given(recipeRepository.findById(anyLong())).willReturn(Optional.of(recipe));
        given(recipeRepository.save(any(Recipe.class))).willReturn(recipe);
        given(uomRepository.findById(anyLong())).willReturn(Optional.of(uom));
        given(toIngredientCommandConverter.convert(ingredientRepo)).willReturn(commandToSave);
        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(commandToSave);
        //then
        then(recipeRepository).should().findById(anyLong());
        then(uomRepository).should().findById(anyLong());
        then(recipeRepository).should().save(any(Recipe.class));
        then(toIngredientCommandConverter).should().convert(any());

    }
}
