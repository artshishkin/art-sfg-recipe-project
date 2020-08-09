package com.artarkatesoft.controllers;

import com.artarkatesoft.commands.NotesCommand;
import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.domain.Notes;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.exceptions.NotFoundException;
import com.artarkatesoft.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@WebFluxTest(RecipeController.class)
class RecipeControllerWebFluxTest {


    @Autowired
    WebTestClient webTestClient;

    @MockBean
    RecipeService recipeService;


    @Captor
    ArgumentCaptor<RecipeCommand> recipeCommandCaptor;

    private static final String ID = "2L";
    private static final String DESCRIPTION = "DDeessccrriippttiioonn";
    private static final int COOK_TIME = 12;
    private RecipeCommand recipeCommand;

    @BeforeEach
    void setUp() {
        recipeCommand = new RecipeCommand();
        recipeCommand.setId(ID);
        recipeCommand.setDescription(DESCRIPTION);
        recipeCommand.setCookTime(COOK_TIME);
        NotesCommand notesCommand = new NotesCommand();
        notesCommand.setNotes("notes");
        notesCommand.setId("FooNotes");
        recipeCommand.setNotes(notesCommand);
    }

    @Test
    @DisplayName("when Recipe found should return Status OK")
    void testShowRecipeById() {
        //given
        Recipe recipe;
        recipe = new Recipe();
        recipe.setId(ID);
        recipe.setDescription("Desc1");
        Notes notes = new Notes();
        notes.setNotes("notes");
        notes.setId("FooNotes");
        recipe.setNotes(notes);

        given(recipeService.getById(anyString())).willReturn(Mono.just(recipe));

        //when
        webTestClient.get().uri("/recipe/{id}/show", ID)
                .exchange()
                .expectStatus().isOk();
        //then
        then(recipeService).should(times(1)).getById(eq(ID));
        then(recipeService).should(never()).getAllRecipes();
        then(recipeService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("when Recipe not found should return Status 404")
    void testShowRecipeByIdWhenNotFound() throws Exception {
        //given
        given(recipeService.getById(anyString())).willThrow(NotFoundException.class);

        //when
        webTestClient.get().uri("/recipe/{id}/show", ID)
                .exchange()
                .expectStatus().isNotFound();
        //then
        then(recipeService).should(times(1)).getById(eq(ID));
        then(recipeService).should(never()).getAllRecipes();
        then(recipeService).shouldHaveNoMoreInteractions();
    }

    @Test
    void testGetNewRecipeForm() throws Exception {
        webTestClient.get().uri("/recipe/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testPostNewRecipe() {
        //given
        given(recipeService.saveRecipeCommand(any(RecipeCommand.class))).willReturn(Mono.just(recipeCommand));

        //when
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("description", DESCRIPTION);
        formData.add("cookTime", String.valueOf(COOK_TIME));
        formData.add("directions", "Directions Bla Bla");
        webTestClient.post().uri("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().is3xxRedirection();
        //then
        then(recipeService).should(times(1)).saveRecipeCommand(recipeCommandCaptor.capture());
        RecipeCommand commandCaptorValue = recipeCommandCaptor.getValue();
        assertThat(commandCaptorValue.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(commandCaptorValue.getCookTime()).isEqualTo(COOK_TIME);
    }

    @Test
    void testPostNewRecipeFormValidationFail(){
        //when
        webTestClient.post().uri("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("description", ";)"))
                .exchange()
                .expectStatus().isOk();
        //then
        then(recipeService).shouldHaveNoInteractions();
    }

    @Test
    void testUpdateRecipeForm() {
        //given
        given(recipeService.getCommandById(ID)).willReturn(Mono.just(recipeCommand));

        //when
        webTestClient.get().uri("/recipe/{id}/update", ID)
                .exchange()
                .expectStatus().isOk();

        //then
        then(recipeService).should().getCommandById(eq(ID));
    }

    @Test
    void testDeleteById() {
        //when
        webTestClient.get().uri("/recipe/{id}/delete", ID)
                .exchange()
                .expectStatus().is3xxRedirection();

        //then
        then(recipeService).should(times(1)).deleteById(eq(ID));
    }
}
