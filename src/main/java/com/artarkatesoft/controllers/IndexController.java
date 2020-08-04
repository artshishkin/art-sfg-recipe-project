package com.artarkatesoft.controllers;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.services.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final RecipeService recipeService;

    @RequestMapping({"/", "index"})
    public String index(Model model) {
        Set<Recipe> allRecipes = recipeService.getAllRecipes().collect(Collectors.toSet()).block();
        log.debug("Enter in index method of IndexController. AllRecipes' size is {}", allRecipes.size());
        model.addAttribute("recipes", allRecipes);
        return "index";
    }
}
