package com.artarkatesoft.controllers;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.commands.UnitOfMeasureCommand;
import com.artarkatesoft.services.IngredientService;
import com.artarkatesoft.services.RecipeService;
import com.artarkatesoft.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;


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
    @Mock
    Model model;

    private RecipeCommand defaultRecipeCommand;
    public static final String RECIPE_ID = "1";

    @Captor
    ArgumentCaptor<IngredientCommand> commandCaptor;

    @BeforeEach
    void setUp() {
        defaultRecipeCommand = new RecipeCommand();
        defaultRecipeCommand.setId(RECIPE_ID);
        UnitOfMeasureCommand uom = new UnitOfMeasureCommand();
        uom.setId("1");
        uom.setDescription("UomDesc");

        List<IngredientCommand> ingredients = LongStream
                .rangeClosed(1, 5)
                .mapToObj(i -> new IngredientCommand(String.valueOf(i), RECIPE_ID, "desc" + i, BigDecimal.valueOf(i), uom))
                .collect(Collectors.toList());

        defaultRecipeCommand.setIngredients(ingredients);
    }

    private void executeModelAttributeMethod() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method populateUomList = IngredientController.class.getDeclaredMethod("populateUomList");
        populateUomList.setAccessible(true);
        Object invoke = populateUomList.invoke(ingredientController);
        Flux<UnitOfMeasureCommand> uomFlux = (Flux<UnitOfMeasureCommand>) invoke;
        model.addAttribute("uomList", uomFlux);
    }

    @Test
    void testGetListOfIngredients() {
        //given
        String recipeId = RECIPE_ID;
        Mono<RecipeCommand> commandMono = Mono.just(defaultRecipeCommand);
        given(recipeService.getCommandById(anyString())).willReturn(commandMono);

        //when
        String view = ingredientController.getIngredientsList(recipeId, model);
        //then
        then(recipeService).should(times(1)).getCommandById(eq(RECIPE_ID));
        then(model).should().addAttribute(eq("recipe"), eq(commandMono));

        assertThat(view).isEqualTo("recipe/ingredient/list");
    }

    @Test
    void testShowIngredient() {
        //given
        String recipeId = RECIPE_ID;
        String ingredientId = "2Lwer";
        IngredientCommand ingredientCommand = defaultRecipeCommand.getIngredients().iterator().next();
        Mono<IngredientCommand> ingredientCommandMono = Mono.just(ingredientCommand);
        given(ingredientService.findIngredientCommandByIdAndRecipeId(anyString(), anyString()))
                .willReturn(ingredientCommandMono);

        //when
        String view = ingredientController.getIngredientByRecipeIdAndIngredientId(recipeId, ingredientId, model);
        //then
        then(ingredientService).should().findIngredientCommandByIdAndRecipeId(eq(ingredientId), eq(recipeId));
        then(model).should().addAttribute(eq("ingredient"), eq(ingredientCommandMono));
        assertThat(view).isEqualTo("recipe/ingredient/show");
    }

    @Test
    void testShowUpdateForm() throws Exception {
        //given
        IngredientCommand ingredientCommand = defaultRecipeCommand.getIngredients().iterator().next();
        Mono<IngredientCommand> ingredientCommandMono = Mono.just(ingredientCommand);
        given(ingredientService.findIngredientCommandByIdAndRecipeId(anyString(), anyString()))
                .willReturn(ingredientCommandMono);
        given(uomService.listAllUoms()).willReturn(Flux.empty());
        //when
        executeModelAttributeMethod();
        String view = ingredientController.showUpdateForm("1", "2", model);
        //then
        then(ingredientService).should().findIngredientCommandByIdAndRecipeId(eq("2"), eq("1"));
        then(uomService).should().listAllUoms();
        then(model).should().addAttribute(eq("uomList"), any(Flux.class));
        then(model).should().addAttribute(eq("ingredient"), eq(ingredientCommandMono));

        assertThat(view).isEqualTo("recipe/ingredient/ingredient_form");
    }


    @Test
    void testNewIngredientForm() throws Exception {
        //given
        given(recipeService.getCommandById(anyString()))
                .willReturn(Mono.just(defaultRecipeCommand));
        given(uomService.listAllUoms()).willReturn(Flux.empty());
        //when
        executeModelAttributeMethod();
        Mono<String> view = ingredientController.showNewIngredientForm(RECIPE_ID, model);

        //then
        StepVerifier.create(view)
                .expectNext(IngredientController.RECIPE_INGREDIENT_FORM)
                .verifyComplete();
        then(recipeService).should().getCommandById(eq(RECIPE_ID));
        then(uomService).should().listAllUoms();
        then(model).should().addAttribute(eq("uomList"), any(Flux.class));
        then(model).should().addAttribute(eq("ingredient"), any(IngredientCommand.class));
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
        given(ingredientService.saveIngredientCommand(ArgumentMatchers.any(IngredientCommand.class))).willReturn(Mono.just(someCommand));

        //when
        initWebDataBinderNoErrors();
        Mono<String> viewMono = ingredientController.createOrUpdateIngredient(someCommand, RECIPE_ID, model);

        //then
        StepVerifier.create(viewMono)
                .expectNext("redirect:/recipe/1/ingredients")
                .verifyComplete();
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

    private void initWebDataBinderNoErrors() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        WebDataBinder webDataBinder = mock(WebDataBinder.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(webDataBinder.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(false);
        Method initBinder = IngredientController.class.getDeclaredMethod("initBinder", WebDataBinder.class);
        initBinder.setAccessible(true);
        initBinder.invoke(ingredientController, webDataBinder);
    }

    @Test
    public void testDeleteIngredient() {
        //given
        String recipeId = "100";
        String ingredientId = "123";
        given(ingredientService.deleteByIdAndRecipeId(anyString(), anyString())).willReturn(Mono.empty());
        //when
        Mono<String> view = ingredientController.deleteIngredient(recipeId, ingredientId);
        //then
        StepVerifier.create(view)
                .expectNext("redirect:/recipe/" + recipeId + "/ingredients")
                .verifyComplete();
        then(ingredientService).should().deleteByIdAndRecipeId(eq(ingredientId), eq(recipeId));
    }
}
