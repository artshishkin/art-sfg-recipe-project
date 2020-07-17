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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("recipe")
@RequiredArgsConstructor
public class RecipeController {

    static final String RECIPE_RECIPEFORM_URL = "recipe/recipeform";
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
        return RECIPE_RECIPEFORM_URL;
    }

    @PostMapping
    public String createOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand recipeCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult
                    .getAllErrors()
                    .stream()
                    .map(ObjectError::toString)
                    .forEach(log::debug);

            return RECIPE_RECIPEFORM_URL;
        }
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);
        return "redirect:/recipe/" + savedRecipeCommand.getId() + "/show";
    }

    @GetMapping("{id}/update")
    public String updateRecipe(@PathVariable Long id, Model model) {
        RecipeCommand recipeCommand = recipeService.getCommandById(id);
        model.addAttribute("recipe", recipeCommand);
        return RECIPE_RECIPEFORM_URL;
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
}
