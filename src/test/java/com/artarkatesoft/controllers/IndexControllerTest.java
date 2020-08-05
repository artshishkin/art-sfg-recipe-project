package com.artarkatesoft.controllers;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    @InjectMocks
    IndexController indexController;
    private Set<Recipe> recipes;

    @Captor
    ArgumentCaptor<String> stringCaptor;
    @Captor
    ArgumentCaptor<Object> objectCaptor;
    @Captor
    ArgumentCaptor<Flux<Recipe>> recipeFluxCaptor;


    @BeforeEach
    void setUp() {
        recipes = new HashSet<>();
        Recipe recipe;
        recipe = new Recipe();
        recipe.setId("1L");
        recipe.setDescription("Desc1");
        recipes.add(recipe);

        recipe = new Recipe();
        recipe.setId("2L");
        recipe.setDescription("Desc2");
        recipes.add(recipe);

    }

    @Test
    void index() {
        //given
        Flux<Recipe> recipeFlux = Flux.fromIterable(recipes);
        given(recipeService.getAllRecipes()).willReturn(recipeFlux);
        //when
        String index = indexController.index(model);
        //then
        then(model).should().addAttribute(stringCaptor.capture(), objectCaptor.capture());
        then(recipeService).should().getAllRecipes();
        assertAll(
                () -> assertThat(index).isEqualTo("home"),
                () -> assertThat(stringCaptor.getValue()).isEqualTo("recipes"),
                () -> assertThat(objectCaptor.getValue()).isEqualTo(recipeFlux)
        );
    }

    @Test
    void indexCaptor() {
        //given
        given(recipeService.getAllRecipes()).willReturn(Flux.fromIterable(recipes));
        //when
        String index = indexController.index(model);
        //then
        then(model).should().addAttribute(stringCaptor.capture(), recipeFluxCaptor.capture());
        then(recipeService).should().getAllRecipes();
        assertThat(index).isEqualTo("index");
        assertThat(stringCaptor.getValue()).isEqualTo("recipes");
        assertThat(recipeFluxCaptor.getValue().collectList().block()).hasSize(2);
    }

    @Test
    void indexJohn() {
        //given
        given(recipeService.getAllRecipes()).willReturn(Flux.fromIterable(recipes));
        //when
        String viewName = indexController.index(model);
        //then
        verify(model).addAttribute(eq("recipes"), any(Flux.class));
        verify(recipeService, times(1)).getAllRecipes();
        assertThat(viewName).isEqualTo("index");
    }

    @Test
    void indexJohnBDD() {
        //given
        given(recipeService.getAllRecipes()).willReturn(Flux.fromIterable(recipes));
        //when
        String viewName = indexController.index(model);
        //then
        then(model).should().addAttribute(eq("recipes"), any(Flux.class));
        then(recipeService).should().getAllRecipes();
        assertThat(viewName).isEqualTo("index");
    }

//    @Test
//    void testMockMVC() throws Exception {
//        //given
//        given(recipeService.getAllRecipes()).willReturn(Flux.fromIterable(recipes));
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
//        //when
//        mockMvc.perform(MockMvcRequestBuilders.get("/"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("index"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("recipes"))
//                .andExpect(MockMvcResultMatchers.model().attribute("recipes", Is.isA(Set.class)));
//    }

    @Test
    @Disabled("java.lang.IllegalStateException: Could not resolve view with name 'home'")
    void testWebTestClient() {
        //given
        given(recipeService.getAllRecipes()).willReturn(Flux.fromIterable(recipes));
        WebTestClient webTestClient = WebTestClient.bindToController(indexController).build();
        //when
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }
}
