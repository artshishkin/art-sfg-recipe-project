package com.artarkatesoft.controllers;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.commands.UnitOfMeasureCommand;
import com.artarkatesoft.services.IngredientService;
import com.artarkatesoft.services.RecipeService;
import com.artarkatesoft.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@WebFluxTest(IngredientController.class)
class IngredientControllerWebFluxTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    RecipeService recipeService;
    @MockBean
    IngredientService ingredientService;
    @MockBean
    UnitOfMeasureService uomService;

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

    @Test
    void testGetListOfIngredients() {
        //given
        given(recipeService.getCommandById(anyString())).willReturn(Mono.just(defaultRecipeCommand));

        //when
        webTestClient.get().uri("/recipe/{recipeId}/ingredients", RECIPE_ID)
                .exchange()
                .expectStatus().isOk();
        //then
        then(recipeService).should(times(1)).getCommandById(eq(RECIPE_ID));
    }

    @Test
    void testShowIngredient() {
        //given
        IngredientCommand ingredientCommand = defaultRecipeCommand.getIngredients().iterator().next();
        given(ingredientService.findIngredientCommandByIdAndRecipeId(anyString(), anyString()))
                .willReturn(Mono.just(ingredientCommand));

        //when
        webTestClient.get().uri("/recipe/1/ingredients/2/show")
                .exchange()
                .expectStatus().isOk();
        //then
        then(ingredientService).should().findIngredientCommandByIdAndRecipeId(eq("2"), eq("1"));
    }

    @Test
    void testShowUpdateForm() {
        //given
        IngredientCommand ingredientCommand = defaultRecipeCommand.getIngredients().iterator().next();
        given(ingredientService.findIngredientCommandByIdAndRecipeId(anyString(), anyString()))
                .willReturn(Mono.just(ingredientCommand));
        given(uomService.listAllUoms()).willReturn(Flux.empty());
        //when
        webTestClient.get().uri("/recipe/1/ingredients/2/update")
                .exchange()
                .expectStatus().isOk();
        //then
        then(ingredientService).should().findIngredientCommandByIdAndRecipeId(eq("2"), eq("1"));
        then(uomService).should().listAllUoms();
    }

    @Test
    void testNewIngredientForm() {
        //given
        given(recipeService.getCommandById(anyString()))
                .willReturn(Mono.just(defaultRecipeCommand));
        given(uomService.listAllUoms()).willReturn(Flux.empty());
        //when
        webTestClient.get().uri("/recipe/{recipeId}/ingredients/new", RECIPE_ID)
                .exchange()
                .expectStatus().isOk();

        //then
        then(recipeService).should().getCommandById(eq(RECIPE_ID));
        then(uomService).should().listAllUoms();
    }

    @Test
    void testNewIngredientForm_notFound() {
        //given
        given(recipeService.getCommandById(anyString()))
                .willReturn(Mono.empty());
        //when
        webTestClient.get().uri("/recipe/{recipeId}/ingredients/new", RECIPE_ID)
                .exchange()
                .expectStatus().isNotFound();

        //then
        then(recipeService).should().getCommandById(eq(RECIPE_ID));
    }

    @Test
    public void testCreateOrUpdateIngredient_success() throws Exception {
        //given
        IngredientCommand someCommand = defaultRecipeCommand.getIngredients().iterator().next();
        MultiValueMap<String, String> commandParams = new LinkedMultiValueMap<>();
        commandParams.add("id", someCommand.getId());
        commandParams.add("recipeId", RECIPE_ID);
        commandParams.add("amount", someCommand.getAmount().toString());
        commandParams.add("description", someCommand.getDescription());
        commandParams.add("uom.id", someCommand.getUom().getId());

        given(ingredientService.saveIngredientCommand(ArgumentMatchers.any(IngredientCommand.class))).willReturn(Mono.just(someCommand));
        //when
        webTestClient.post().uri("/recipe/{recipeId}/ingredients", RECIPE_ID)
                .contentType(APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(commandParams))
                .exchange()
                .expectStatus().is3xxRedirection();

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
    public void testCreateOrUpdateIngredient_bindingErrors() throws Exception {
        //given
        IngredientCommand someCommand = defaultRecipeCommand.getIngredients().iterator().next();
        MultiValueMap<String, String> commandParams = new LinkedMultiValueMap<>();
        commandParams.add("id", someCommand.getId());
        commandParams.add("recipeId", RECIPE_ID);
        commandParams.add("amount", someCommand.getAmount().toString());
        commandParams.add("description", "");
        commandParams.add("uom.id", someCommand.getUom().getId());

        //when
        webTestClient.post().uri("/recipe/{recipeId}/ingredients", RECIPE_ID)
                .contentType(APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(commandParams))
                .exchange()
                .expectStatus().isOk();
        //then
        then(ingredientService).shouldHaveNoInteractions();
    }

    @Test
    public void testCreateOrUpdateIngredient_idDoesNotMatch() throws Exception {
        //given
        IngredientCommand someCommand = defaultRecipeCommand.getIngredients().iterator().next();
        MultiValueMap<String, String> commandParams = new LinkedMultiValueMap<>();
        commandParams.add("id", someCommand.getId());
        commandParams.add("recipeId", "Wrong Recipe ID");
        commandParams.add("amount", someCommand.getAmount().toString());
        commandParams.add("description", someCommand.getDescription());
        commandParams.add("uom.id", someCommand.getUom().getId());

        //when
        webTestClient.post().uri("/recipe/{recipeId}/ingredients", RECIPE_ID)
                .contentType(APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(commandParams))
                .exchange()
                .expectStatus().is5xxServerError();
        //then
        then(ingredientService).shouldHaveNoInteractions();
    }

    @Test
    public void testDeleteIngredient() throws Exception {
        //given
        String recipeId = "100";
        String ingredientId = "123";
        given(ingredientService.deleteByIdAndRecipeId(anyString(), anyString())).willReturn(Mono.empty());
        //when
        webTestClient.get().uri("/recipe/{recipeId}/ingredients/{id}/delete", recipeId, ingredientId)
                .exchange()
                .expectStatus().is3xxRedirection();
        //then
        then(ingredientService).should().deleteByIdAndRecipeId(eq(ingredientId), eq(recipeId));
    }
}
