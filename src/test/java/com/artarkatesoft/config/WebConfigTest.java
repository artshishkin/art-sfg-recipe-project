package com.artarkatesoft.config;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
class WebConfigTest {

    WebTestClient webTestClient;

    @Mock
    RecipeService recipeService;

    @BeforeEach
    void setUp() {
        WebConfig webConfig = new WebConfig();
        webTestClient = WebTestClient
                .bindToRouterFunction(webConfig.routeApi(recipeService))
                .build();
    }

    @Test
    void routeApi() {
        //given
        given(recipeService.getAllRecipes()).willReturn(Flux.just(new Recipe(), new Recipe()));
        //when
        webTestClient.get().uri("/api/recipes")
                .accept(APPLICATION_JSON)
                .exchange()
                //then
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBodyList(Recipe.class)
                .hasSize(2);
    }
}
