package com.artarkatesoft.bootstrap;

import com.artarkatesoft.domain.*;
import com.artarkatesoft.repositories.CategoryRepository;
import com.artarkatesoft.repositories.RecipeRepository;
import com.artarkatesoft.repositories.UnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.math.BigDecimal.valueOf;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {

        recipeRepository.save(createGuacamoleRecipe());
        recipeRepository.save(createChickenTacosRecipe());

    }

    private Recipe createGuacamoleRecipe() {
        Recipe recipe = new Recipe();
        recipe.setPrepTime(10);
        recipe.setCookTime(0);
        recipe.setDescription("How to Make Perfect Guacamole Recipe");
        recipe.setServings(4);
        recipe.setDifficulty(Difficulty.MEDIUM);
        recipe.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");

        Notes notes = new Notes();
        notes.setNotes("Be careful handling chiles if using." +
                " Wash your hands thoroughly after handling and do not touch" +
                " your eyes or the area near your eyes with your hands for several hours.");

        recipe.setNotes(notes);
        notes.setRecipe(recipe);

        Set<Ingredient> ingredients = new HashSet<>();
        recipe.setIngredients(ingredients);

        ingredients.add(new Ingredient(recipe,"2 ripe avocados", valueOf(2),unitOfMeasureRepository.findByDescription("Unit").get()));
        ingredients.add(new Ingredient(recipe,"1/4 teaspoon of salt, more to taste", valueOf(0.25),unitOfMeasureRepository.findByDescription("Teaspoon").get()));
        ingredients.add(new Ingredient(recipe,"1 tablespoon fresh lime juice or lemon juice", valueOf(1),unitOfMeasureRepository.findByDescription("Tablespoon").get()));
        ingredients.add(new Ingredient(recipe,"2 tablespoons to 1/4 cup of minced red onion or thinly sliced green onion", valueOf(0.25),unitOfMeasureRepository.findByDescription("Cup").get()));
        Category mexican = categoryRepository.findByDescription("Mexican").get();
//        mexican.getRecipes().add(recipe);
        recipe.setCategories(Collections.singleton(mexican));
        return recipe;
    }

    private Recipe createChickenTacosRecipe() {
        Recipe recipe = new Recipe();
        recipe.setPrepTime(20);
        recipe.setCookTime(15);
        recipe.setDescription("Spicy Grilled Chicken Tacos Recipe");
        recipe.setServings(6);
        recipe.setDifficulty(Difficulty.HARD);
        recipe.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");

        Notes notes = new Notes();
        notes.setNotes("Look for ancho chile powder with the Mexican" +
                " ingredients at your grocery store, on buy it online. " +
                "(If you can't find ancho chili powder, you replace the ancho " +
                "chili, the oregano, and the cumin with 2 1/2 tablespoons regular " +
                "chili powder, though the flavor won't be quite the same.)");

        recipe.setNotes(notes);
        notes.setRecipe(recipe);

        Set<Ingredient> ingredients = new HashSet<>();
        recipe.setIngredients(ingredients);

        ingredients.add(new Ingredient(recipe, "2 tablespoons ancho chili powder", valueOf(2),unitOfMeasureRepository.findByDescription("Tablespoon").get()));
        ingredients.add(new Ingredient(recipe, "1 teaspoon dried oregano", valueOf(1),unitOfMeasureRepository.findByDescription("Teaspoon").get()));
        ingredients.add(new Ingredient(recipe, "1 teaspoon dried cumin", valueOf(1),unitOfMeasureRepository.findByDescription("Teaspoon").get()));
        ingredients.add(new Ingredient(recipe, "1 teaspoon sugar", valueOf(1),unitOfMeasureRepository.findByDescription("Teaspoon").get()));

//        mexican.getRecipes().add(recipe);

        recipe.setCategories(new HashSet<>());
        Category mexican = categoryRepository.findByDescription("Mexican").get();
        recipe.getCategories().add(mexican);
        Category fastFood = categoryRepository.findByDescription("Fast Food").get();
        recipe.getCategories().add(fastFood);

        return recipe;
    }
}
