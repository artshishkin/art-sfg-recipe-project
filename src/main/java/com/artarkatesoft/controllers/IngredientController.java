package com.artarkatesoft.controllers;

import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.services.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("recipe")
public class IngredientController {

    private final RecipeService recipeService;

    @GetMapping("{recipeId}/ingredients")
    public String getIngredientsList(@PathVariable("recipeId") Long recipeId, Model model) {
        log.debug("Getting ingredints list of recipe id: {}", recipeId);
        RecipeCommand recipeCommand = recipeService.getCommandById(recipeId);
        model.addAttribute("recipe", recipeCommand);
        return "recipe/ingredient/list";
    }

}
