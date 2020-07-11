package com.artarkatesoft.controllers;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.commands.UnitOfMeasureCommand;
import com.artarkatesoft.services.RecipeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

    @InjectMocks
    IngredientController ingredientController;

    @Mock
    RecipeService recipeService;

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
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/{recipeId}/ingredients", RECIPE_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attribute("recipe", CoreMatchers.notNullValue()))
                .andExpect(model().attribute("recipe", CoreMatchers.isA(RecipeCommand.class)))
                .andExpect(view().name("recipe/ingredient/list"));
        //then
        then(recipeService).should(times(1)).getCommandById(eq(RECIPE_ID));
    }

//    @Test
//    void testGetListOfIngredients() throws Exception {
//        //given
//        given(recipeService.getCommandById(anyLong())).willReturn(recipeCommand);
//
//        //when
//        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/{recipeId}/ingredients", RECIPE_ID))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("ingredients"))
//                .andExpect(model().attribute("ingredients", CoreMatchers.notNullValue()))
//                .andExpect(model().attribute("ingredients", hasSize(recipeCommand.getIngredients().size())))
//                .andExpect(view().name("recipe/ingredient/list"));
//        //then
//        then(recipeService).should(times(1)).getCommandById(eq(RECIPE_ID));
//    }
}
