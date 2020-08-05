package com.artarkatesoft.controllers;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@WebFluxTest(controllers = IndexController.class)
@Import({ThymeleafAutoConfiguration.class})
class IndexControllerWebFluxTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    RecipeService recipeService;

    private Set<Recipe> recipes;

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
    void index_justStatus() {
        //given
        given(recipeService.getAllRecipes()).willReturn(Flux.fromIterable(recipes));
        //when
        webTestClient.get().uri("/")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk();
        //then
        then(recipeService).should().getAllRecipes();
    }

    @Test
    void index() {
        //given
        given(recipeService.getAllRecipes()).willReturn(Flux.fromIterable(recipes));
//        mockMvc.perform(MockMvcRequestBuilders.get("/"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("index"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("recipes"))
//                .andExpect(MockMvcResultMatchers.model().attribute("recipes", Is.isA(Set.class)));
        //when
        FluxExchangeResult<String> stringFluxExchangeResult = webTestClient.get().uri("/")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class);
        //then
        String fullHtml = stringFluxExchangeResult.getResponseBody().reduce((a, b) -> a + b).block();
        then(recipeService).should().getAllRecipes();
        assertThat(fullHtml).contains("<title>Recipes Home</title>");
    }
}
