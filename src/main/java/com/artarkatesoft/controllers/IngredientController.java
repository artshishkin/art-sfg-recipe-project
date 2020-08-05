package com.artarkatesoft.controllers;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.commands.RecipeCommand;
import com.artarkatesoft.commands.UnitOfMeasureCommand;
import com.artarkatesoft.services.IngredientService;
import com.artarkatesoft.services.RecipeService;
import com.artarkatesoft.services.UnitOfMeasureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("recipe")
public class IngredientController {

    public static final String RECIPE_INGREDIENT_FORM = "recipe/ingredient/ingredient_form";
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService uomService;
    private WebDataBinder webDataBinder;

    @InitBinder("ingredient")
    private void initBinder(WebDataBinder webDataBinder) {
        this.webDataBinder = webDataBinder;
    }

    @GetMapping("{recipeId}/ingredients")
    public String getIngredientsList(@PathVariable("recipeId") String recipeId, Model model) {
        log.debug("Getting ingredients list of recipe id: {}", recipeId);
        Mono<RecipeCommand> recipeCommand = recipeService.getCommandById(recipeId);
        model.addAttribute("recipe", recipeCommand);
        return "recipe/ingredient/list";
    }

    @GetMapping("{recipeId}/ingredients/{id}/show")
    public String getIngredientByRecipeIdAndIngredientId(@PathVariable("recipeId") String recipeId,
                                                         @PathVariable("id") String id,
                                                         Model model) {
        Mono<IngredientCommand> ingredient = ingredientService.findIngredientCommandByIdAndRecipeId(id, recipeId);
        model.addAttribute("ingredient", ingredient);
        return "recipe/ingredient/show";
    }

    @GetMapping("{recipeId}/ingredients/{id}/update")
    public String showUpdateForm(@PathVariable("recipeId") String recipeId,
                                 @PathVariable("id") String id,
                                 Model model) {
        Mono<IngredientCommand> command = ingredientService.findIngredientCommandByIdAndRecipeId(id, recipeId);
        model.addAttribute("ingredient", command);
        return RECIPE_INGREDIENT_FORM;
    }

    @GetMapping("{recipeId}/ingredients/{id}/delete")
    public Mono<String> deleteIngredient(@PathVariable("recipeId") String recipeId,
                                         @PathVariable("id") String id) {
        return ingredientService
                .deleteByIdAndRecipeId(id, recipeId)
                .then(Mono.just("redirect:/recipe/" + recipeId + "/ingredients"));
    }

    @GetMapping("{recipeId}/ingredients/new")
    public String showNewIngredientForm(@PathVariable("recipeId") String recipeId,
                                        Model model) {
        RecipeCommand recipeCommand = recipeService.getCommandById(recipeId).block();
        // TODO: 13.07.2020 Raise Exception if null
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        model.addAttribute("ingredient", ingredientCommand);
        return RECIPE_INGREDIENT_FORM;
    }

    @PostMapping("{recipeId}/ingredients")
    public Mono<String> createOrUpdateIngredient(@ModelAttribute("ingredient") IngredientCommand ingredientCommand, @PathVariable("recipeId") String recipeId, Model model) {
        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            bindingResult
                    .getAllErrors()
                    .stream()
                    .map(ObjectError::toString)
                    .forEach(log::debug);

            return Mono.just(RECIPE_INGREDIENT_FORM);
        }
        if (!Objects.equals(recipeId, ingredientCommand.getRecipeId()))
            throw new RuntimeException("ID of recipe does not match");
        return ingredientService
                .saveIngredientCommand(ingredientCommand)
                .log("createOrUpdateIngredient")
                .then(Mono.just("redirect:/recipe/" + recipeId + "/ingredients"));
    }

    @ModelAttribute("uomList")
    private Flux<UnitOfMeasureCommand> populateUomList() {
        return uomService.listAllUoms();
    }

}
