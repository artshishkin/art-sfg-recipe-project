package com.artarkatesoft.controllers;

import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.exceptions.NotFoundException;
import com.artarkatesoft.services.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
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

    @RequestMapping("new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/recipeform";
    }

    @PostMapping
    public String createOrUpdate(@ModelAttribute("recipe") RecipeCommand recipeCommand) {
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);
        return "redirect:/recipe/" + savedRecipeCommand.getId() + "/show";
    }

    @GetMapping("{id}/update")
    public String updateRecipe(@PathVariable Long id, Model model) {
        RecipeCommand recipeCommand = recipeService.getCommandById(id);
        model.addAttribute("recipe", recipeCommand);
        return "recipe/recipeform";
    }

    @GetMapping("{id}/delete")
    public String deleteRecipe(@PathVariable Long id) {
        log.debug("Deleting recipe with id {}", id);
        recipeService.deleteById(id);
        return "redirect:/";
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound(Exception exception) {
        log.error("Handling not found exception." + exception.getMessage());
        ModelAndView modelAndView = new ModelAndView("404error");
        modelAndView.addObject("exception", exception);
        return modelAndView;
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleNumberFormatException(Exception exception) {
        log.error("Handling NumberFormatException." + exception.getMessage());
        ModelAndView modelAndView = new ModelAndView("400error");
        modelAndView.addObject("exception", exception);
        return modelAndView;
    }


}
