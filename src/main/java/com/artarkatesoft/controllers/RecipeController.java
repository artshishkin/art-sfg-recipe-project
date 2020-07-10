package com.artarkatesoft.controllers;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @RequestMapping("/show/{id}")
    public String showInfo(@PathVariable("id") Long id, Model model) {
        Recipe recipe = recipeService.getById(id);
        model.addAttribute("recipe", recipe);
        return "recipe/show";
    }
}
