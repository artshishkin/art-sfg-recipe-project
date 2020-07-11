package com.artarkatesoft.services;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.converters.IngredientToIngredientCommandConverter;
import com.artarkatesoft.domain.Ingredient;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.repositories.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    IngredientToIngredientCommandConverter toIngredientCommandConverter;
    @Captor
    ArgumentCaptor<Ingredient> ingredientCaptor;

    @Test
    void findIngredientCommandByIdAndRecipeId() {
        //given
        Long id = 2L;
        Long recipeId = 1L;
        Recipe recipe = new Recipe();

        LongStream.rangeClosed(1, 5)
                .mapToObj(this::createFakeIngredient)
                .forEach(recipe::addIngredient);

        given(recipeRepository.findById(anyLong())).willReturn(Optional.of(recipe));
        given(toIngredientCommandConverter.convert(any(Ingredient.class))).willReturn(new IngredientCommand());
        //when
        IngredientCommand ingredientCommand = ingredientService.findIngredientCommandByIdAndRecipeId(id, recipeId);
        //then
        then(recipeRepository).should().findById(eq(recipeId));
        then(toIngredientCommandConverter).should(times(1)).convert(ingredientCaptor.capture());
        assertThat(ingredientCaptor.getValue().getId()).isEqualTo(id);

    }

    private Ingredient createFakeIngredient(Long id) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setDescription("Desc" + id);
        return ingredient;
    }
}
