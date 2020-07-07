package com.artarkatesoft.bootstrap;

import com.artarkatesoft.domain.*;
import com.artarkatesoft.repositories.CategoryRepository;
import com.artarkatesoft.repositories.RecipeRepository;
import com.artarkatesoft.repositories.UnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import static java.math.BigDecimal.valueOf;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final CategoryRepository categoryRepository;

    private final Supplier<RuntimeException> expectedCategoryNotFound = () -> new RuntimeException("Expected Category not found");
    private final Supplier<RuntimeException> expectedUomNotFound = () -> new RuntimeException("Expected UOM not found");


    private Recipe createGuacamoleRecipe() {
        Recipe recipe = new Recipe();
        recipe.setPrepTime(10);
        recipe.setCookTime(0);
        recipe.setDescription("How to Make Perfect Guacamole Recipe");
        recipe.setServings(4);
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        recipe.setDirections("1 Cut the avocado, remove flesh: Cut the avocados in half. Remove...\n" +
                "2 Mash with a fork: Using a fork, roughly mash the avocado. \n" +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. \n" +
                "4 Serve: Serve immediately, or if making a few hours ahead, place plastic wrap on the " +
                "surface of the guacamole and press down to cover it and to prevent air reaching it.");

        Notes notes = new Notes();
        notes.setNotes("The best guacamole keeps it simple: just ripe avocados, salt, a squeeze of lime, " +
                "onions, chiles, cilantro, and some chopped tomato. Serve it as a dip at your next party " +
                "or spoon it on top of tacos for an easy dinner upgrade.");

        recipe.setNotes(notes);
        notes.setRecipe(recipe);

        Set<Ingredient> ingredients = new HashSet<>();
        recipe.setIngredients(ingredients);

        UnitOfMeasure unitUom = unitOfMeasureRepository.findByDescription("Unit").orElseThrow(expectedUomNotFound);
        UnitOfMeasure teaspoonUom = unitOfMeasureRepository.findByDescription("Teaspoon").orElseThrow(expectedUomNotFound);
        UnitOfMeasure tablespoonUom = unitOfMeasureRepository.findByDescription("Tablespoon").orElseThrow(expectedUomNotFound);
        UnitOfMeasure cupUom = unitOfMeasureRepository.findByDescription("Cup").orElseThrow(expectedUomNotFound);

        ingredients.add(new Ingredient(recipe, "2 ripe avocados", valueOf(2), unitUom));
        ingredients.add(new Ingredient(recipe, "1/4 teaspoon of salt, more to taste", valueOf(0.25), teaspoonUom));
        ingredients.add(new Ingredient(recipe, "1 tablespoon fresh lime juice or lemon juice", valueOf(1), tablespoonUom));
        ingredients.add(new Ingredient(recipe, "2 tablespoons to 1/4 cup of minced red onion or thinly sliced green onion", valueOf(0.25), cupUom));

        Category mexican = categoryRepository.findByDescription("Mexican").orElseThrow(expectedCategoryNotFound);
        Category american = categoryRepository.findByDescription("American").orElseThrow(expectedCategoryNotFound);
        recipe.getCategories().add(mexican);
        recipe.getCategories().add(american);
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
        recipe.setDirections("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                "2 Make the marinade and coat the chicken: In a large bowl, stir together\n" +
                "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, \n" +
                "4 Warm the tortillas: Place each tortilla on the grill or on a \n" +
                "5 Assemble the tacos: Slice the chicken into strips. ");

        Notes notes = new Notes();
        notes.setNotes("Spicy grilled chicken tacos! Quick marinade, then grill. " +
                "Ready in about 30 minutes. Great for a quick weeknight dinner, " +
                "backyard cookouts, and tailgate parties");

        recipe.setNotes(notes);
        notes.setRecipe(recipe);

        Set<Ingredient> ingredients = new HashSet<>();
        recipe.setIngredients(ingredients);

        UnitOfMeasure teaspoonUom = unitOfMeasureRepository.findByDescription("Teaspoon").orElseThrow(expectedUomNotFound);
        UnitOfMeasure tablespoonUom = unitOfMeasureRepository.findByDescription("Tablespoon").orElseThrow(expectedUomNotFound);

        ingredients.add(new Ingredient(recipe, "2 tablespoons ancho chili powder", valueOf(2), tablespoonUom));
        ingredients.add(new Ingredient(recipe, "1 teaspoon dried oregano", valueOf(1), teaspoonUom));
        ingredients.add(new Ingredient(recipe, "1 teaspoon dried cumin", valueOf(1), teaspoonUom));
        ingredients.add(new Ingredient(recipe, "1 teaspoon sugar", valueOf(1), teaspoonUom));


        Category mexican = categoryRepository.findByDescription("Mexican").orElseThrow(expectedCategoryNotFound);
        Category fastFood = categoryRepository.findByDescription("Fast Food").orElseThrow(expectedCategoryNotFound);
        recipe.getCategories().add(mexican);
        recipe.getCategories().add(fastFood);

        return recipe;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        recipeRepository.save(createGuacamoleRecipe());
        recipeRepository.save(createChickenTacosRecipe());
    }
}