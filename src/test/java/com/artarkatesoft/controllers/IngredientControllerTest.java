package com.artarkatesoft.controllers;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.commands.UnitOfMeasureCommand;
import com.artarkatesoft.services.IngredientService;
import com.artarkatesoft.services.RecipeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

    @InjectMocks
    IngredientController ingredientController;

    @Mock
    RecipeService recipeService;
    @Mock
    IngredientService ingredientService;

    MockMvc mockMvc;
    private RecipeCommand recipeCommand;
    public static final Long RECIPE_ID = 1L;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();

        recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        UnitOfMeasureCommand uom = new UnitOfMeasureCommand();
        uom.setId(1L);
        uom.setDescription("UomDesc");

        Set<IngredientCommand> ingredients = LongStream
                .rangeClosed(1, 5)
                .mapToObj(i -> new IngredientCommand(i, "desc" + i, BigDecimal.valueOf(i), uom))
                .collect(Collectors.toSet());

        recipeCommand.setIngredients(ingredients);
    }

    @Test
    void testGetListOfIngredients() throws Exception {
        //given
        given(recipeService.getCommandById(anyLong())).willReturn(recipeCommand);

        //when
        mockMvc.perform(get("/recipe/{recipeId}/ingredients", RECIPE_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attribute("recipe", notNullValue()))
                .andExpect(model().attribute("recipe", CoreMatchers.isA(RecipeCommand.class)))
                .andExpect(view().name("recipe/ingredient/list"));
        //then
        then(recipeService).should(times(1)).getCommandById(eq(RECIPE_ID));
    }

    @Test
    void testShowIngredient() throws Exception {
        //given
        IngredientCommand ingredientCommand = recipeCommand.getIngredients().iterator().next();
        given(ingredientService.findIngredientCommandByIdAndRecipeId(anyLong(), anyLong()))
                .willReturn(ingredientCommand);

        //when
        mockMvc.perform(get("/recipe/1/ingredients/2/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attribute("ingredient", notNullValue()));
        //then
        then(ingredientService).should().findIngredientCommandByIdAndRecipeId(eq(2L), eq(1L));
    }
}
