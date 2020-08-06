package com.artarkatesoft.config;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.services.RecipeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class WebConfig {

    @Bean
    public RouterFunction<ServerResponse> routeApi(RecipeService recipeService) {
        return RouterFunctions.route(
                GET("/api/recipes"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(recipeService.getAllRecipes(), Recipe.class));
    }
}
