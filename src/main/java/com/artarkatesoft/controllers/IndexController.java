package com.artarkatesoft.controllers;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.services.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final RecipeService recipeService;

    @RequestMapping({"", "/", "index", "home"})
    public String index(Model model) {
        log.debug("in index controller");
        Flux<Recipe> allRecipes = recipeService.getAllRecipes();
        model.addAttribute("recipes", allRecipes);
        return "index";
    }
}
