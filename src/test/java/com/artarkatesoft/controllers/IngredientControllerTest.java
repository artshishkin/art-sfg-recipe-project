package com.artarkatesoft.controllers;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.commands.UnitOfMeasureCommand;
import com.artarkatesoft.services.IngredientService;
import com.artarkatesoft.services.RecipeService;
import com.artarkatesoft.services.UnitOfMeasureService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

    @InjectMocks
    IngredientController ingredientController;

    @Mock
    RecipeService recipeService;
    @Mock
    IngredientService ingredientService;
    @Mock
    UnitOfMeasureService uomService;

    MockMvc mockMvc;
    private RecipeCommand defaultRecipeCommand;
    public static final Long RECIPE_ID = 1L;

    @Captor
    ArgumentCaptor<IngredientCommand> commandCaptor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();

        defaultRecipeCommand = new RecipeCommand();
        defaultRecipeCommand.setId(RECIPE_ID);
        UnitOfMeasureCommand uom = new UnitOfMeasureCommand();
        uom.setId(1L);
        uom.setDescription("UomDesc");

        Set<IngredientCommand> ingredients = LongStream
                .rangeClosed(1, 5)
                .mapToObj(i -> new IngredientCommand(i, RECIPE_ID, "desc" + i, BigDecimal.valueOf(i), uom))
                .collect(Collectors.toSet());

        defaultRecipeCommand.setIngredients(ingredients);
    }

    @Test
    void testGetListOfIngredients() throws Exception {
        //given
        given(recipeService.getCommandById(anyLong())).willReturn(defaultRecipeCommand);

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
        IngredientCommand ingredientCommand = defaultRecipeCommand.getIngredients().iterator().next();
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

    @Test
    void testShowUpdateForm() throws Exception {
        //given
        IngredientCommand ingredientCommand = defaultRecipeCommand.getIngredients().iterator().next();
        given(ingredientService.findIngredientCommandByIdAndRecipeId(anyLong(), anyLong()))
                .willReturn(ingredientCommand);
        given(uomService.listAllUoms()).willReturn(Collections.emptyList());
        //when
        mockMvc.perform(get("/recipe/1/ingredients/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredient_form"))
                .andExpect(model().attributeExists("ingredient", "uomList"))
                .andExpect(model().attribute("uomList", notNullValue()))
                .andExpect(model().attribute("ingredient", notNullValue(IngredientCommand.class)));

        //then
        then(ingredientService).should().findIngredientCommandByIdAndRecipeId(eq(2L), eq(1L));
        then(uomService).should().listAllUoms();
    }

    @Test
    void testNewIngredientForm() throws Exception {
        //given
        given(recipeService.getCommandById(anyLong()))
                .willReturn(defaultRecipeCommand);
        given(uomService.listAllUoms()).willReturn(Collections.emptyList());
        //when
        mockMvc.perform(get("/recipe/{recipeId}/ingredients/new", RECIPE_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredient_form"))
                .andExpect(model().attributeExists("ingredient", "uomList"))
                .andExpect(model().attribute("uomList", notNullValue()))
                .andExpect(model().attribute("ingredient", notNullValue(IngredientCommand.class)));

        //then
        then(recipeService).should().getCommandById(eq(RECIPE_ID));
        then(uomService).should().listAllUoms();
    }

    @Test
    public void testCreateOrUpdateIngredient() throws Exception {
        //given
        IngredientCommand someCommand = defaultRecipeCommand.getIngredients().iterator().next();
        MultiValueMap<String, String> commandParams = new LinkedMultiValueMap<>();
        commandParams.add("id", someCommand.getId().toString());
        commandParams.add("recipeId", RECIPE_ID.toString());
        commandParams.add("amount", someCommand.getAmount().toString());
        commandParams.add("description", someCommand.getDescription());
        commandParams.add("uom.id", someCommand.getUom().getId().toString());

        //when
        mockMvc.perform(
                post("/recipe/{recipeId}/ingredients", RECIPE_ID)
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .params(commandParams))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipe/1/ingredients"));
        //then
        then(ingredientService).should().saveIngredientCommand(commandCaptor.capture());
        IngredientCommand captorValue = commandCaptor.getValue();
        assertAll(
                () -> assertThat(captorValue.getId()).isEqualTo(someCommand.getId()),
                () -> assertThat(captorValue.getRecipeId()).isEqualTo(someCommand.getRecipeId()),
                () -> assertThat(captorValue.getUom().getId()).isEqualTo(someCommand.getUom().getId()),
                () -> assertThat(captorValue.getDescription()).isEqualTo(someCommand.getDescription()),
                () -> assertThat(captorValue.getAmount()).isEqualTo(someCommand.getAmount())
        );
    }

    @Test
    public void testDeleteIngredient() throws Exception {
        //given
        Long recipeId = 100L;
        Long ingredientId = 123L;
        //when
        mockMvc.perform(get("/recipe/{recipeId}/ingredients/{id}/delete", recipeId, ingredientId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/recipe/{recipeId}/ingredients", recipeId));
        //then
        then(ingredientService).should().deleteByIdAndRecipeId(eq(ingredientId), eq(recipeId));
    }
}
