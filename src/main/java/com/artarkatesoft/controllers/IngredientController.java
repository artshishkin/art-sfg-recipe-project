package com.artarkatesoft.controllers;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.services.IngredientService;
import com.artarkatesoft.services.RecipeService;
import com.artarkatesoft.services.UnitOfMeasureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("recipe")
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService uomService;

    @GetMapping("{recipeId}/ingredients")
    public String getIngredientsList(@PathVariable("recipeId") Long recipeId, Model model) {
        log.debug("Getting ingredients list of recipe id: {}", recipeId);
        RecipeCommand recipeCommand = recipeService.getCommandById(recipeId);
        model.addAttribute("recipe", recipeCommand);
        return "recipe/ingredient/list";
    }

    @GetMapping("{recipeId}/ingredients/{id}/show")
    public String getIngredientByRecipeIdAndIngredientId(@PathVariable("recipeId") Long recipeId,
                                                         @PathVariable("id") Long id,
                                                         Model model) {
        IngredientCommand ingredient = ingredientService.findIngredientCommandByIdAndRecipeId(id, recipeId);
        model.addAttribute("ingredient", ingredient);
        return "recipe/ingredient/show";
    }

    @GetMapping("{recipeId}/ingredients/{id}/update")
    public String showUpdateForm(@PathVariable("recipeId") Long recipeId,
                                 @PathVariable("id") Long id,
                                 Model model) {
        IngredientCommand command = ingredientService.findIngredientCommandByIdAndRecipeId(id, recipeId);
        model.addAttribute("ingredient", command);
        model.addAttribute("uomList", uomService.listAllUoms());
        return "recipe/ingredient/ingredient_form";
    }

    @GetMapping("{recipeId}/ingredients/new")
    public String showNewIngredientForm(@PathVariable("recipeId") Long recipeId,
                                 Model model) {
        RecipeCommand recipeCommand = recipeService.getCommandById(recipeId);
        // TODO: 13.07.2020 Raise Exception if null
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", uomService.listAllUoms());
        return "recipe/ingredient/ingredient_form";
    }

    @PostMapping("{recipeId}/ingredients")
    public String createOrUpdateIngredient(@ModelAttribute("ingredient") IngredientCommand ingredientCommand, @PathVariable("recipeId") Long recipeId) {
        if (!Objects.equals(recipeId, ingredientCommand.getRecipeId()))
            throw new RuntimeException("ID of recipe does not match");
        ingredientService.saveIngredientCommand(ingredientCommand);
        return "redirect:/recipe/" + recipeId + "/ingredients";

    }

}