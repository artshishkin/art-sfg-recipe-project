package com.artarkatesoft.controllers;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final RecipeService recipeService;

    @RequestMapping({"/", "index"})
    public String index(Model model) {
        Iterable<Recipe> allRecipes = recipeService.getAllRecipes();
        model.addAttribute("recipes", allRecipes);
        return "index";
    }
}
