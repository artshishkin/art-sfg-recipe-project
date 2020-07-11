package com.artarkatesoft.controllers;

import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @RequestMapping("{id}/show")
    public String showById(@PathVariable("id") Long id, Model model) {
        Recipe recipe = recipeService.getById(id);
        model.addAttribute("recipe", recipe);
        return "recipe/show";
    }

    @GetMapping
    public String showForm(Model model) {
        Long id = 1L;
        Recipe recipe = recipeService.getById(id);
        model.addAttribute("recipe", recipe);

        return "recipe/recipeform";
    }

    @RequestMapping("new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/recipeform";
    }

    @PostMapping
    public String createOrUpdate(@ModelAttribute("recipe") RecipeCommand recipeCommand) {
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);
        return "redirect:/recipe/"+savedRecipeCommand.getId()+"/show";
    }


}
