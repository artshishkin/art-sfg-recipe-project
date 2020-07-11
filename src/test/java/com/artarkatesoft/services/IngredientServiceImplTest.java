package com.artarkatesoft.services;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.commands.RecipeCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

    @InjectMocks
    IngredientServiceImpl ingredientService;

    @Mock
    RecipeService recipeService;

    @Test
    void findIngredientCommandByIdAndRecipeId() {
        //given
        Long id = 2L;
        Long recipeId = 1L;
        RecipeCommand recipeCommand = new RecipeCommand();
        Set<IngredientCommand> ingredientCommands =
                LongStream
                        .rangeClosed(1, 5)
                        .mapToObj(i -> new IngredientCommand(i, "desc" + i, null, null))
                        .collect(Collectors.toSet());
        recipeCommand.setIngredients(ingredientCommands);
        given(recipeService.getCommandById(anyLong())).willReturn(recipeCommand);
        //when
        IngredientCommand ingredientCommand = ingredientService.findIngredientCommandByIdAndRecipeId(id, recipeId);
        //then
        then(recipeService).should().getCommandById(eq(recipeId));
        assertThat(ingredientCommand.getId()).isEqualTo(id);

    }
}
