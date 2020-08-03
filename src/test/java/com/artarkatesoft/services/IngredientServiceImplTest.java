package com.artarkatesoft.services;

import com.artarkatesoft.commands.IngredientCommand;
import com.artarkatesoft.commands.UnitOfMeasureCommand;
import com.artarkatesoft.converters.IngredientCommandToIngredientConverter;
import com.artarkatesoft.converters.IngredientToIngredientCommandConverter;
import com.artarkatesoft.domain.Ingredient;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.domain.UnitOfMeasure;
import com.artarkatesoft.repositories.reactive.RecipeReactiveRepository;
import com.artarkatesoft.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

    @InjectMocks
    IngredientServiceImpl ingredientService;

    @Mock
    RecipeReactiveRepository recipeRepository;
    @Mock
    UnitOfMeasureReactiveRepository uomRepository;
    @Mock
    IngredientToIngredientCommandConverter toIngredientCommandConverter;
    @Mock
    IngredientCommandToIngredientConverter toIngredientConverter;


    @Captor
    ArgumentCaptor<Ingredient> ingredientCaptor;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        String recipeId = "1";

        recipe = new Recipe();
        LongStream.rangeClosed(1, 5)
                .mapToObj(String::valueOf)
                .map(this::createFakeIngredient)
                .forEach(recipe::addIngredient);
        recipe.setId(recipeId);
    }

    @Test
    void findIngredientCommandByIdAndRecipeId() {
        //given
        String id = "2";
        String recipeId = recipe.getId();

        given(recipeRepository.findById(anyString())).willReturn(Mono.just(recipe));
        given(toIngredientCommandConverter.convert(any(Ingredient.class))).willReturn(new IngredientCommand());
        //when
        IngredientCommand ingredientCommand = ingredientService.findIngredientCommandByIdAndRecipeId(id, recipeId).block();
        //then
        then(recipeRepository).should().findById(eq(recipe.getId()));
        then(toIngredientCommandConverter).should(times(1)).convert(ingredientCaptor.capture());
        assertThat(ingredientCaptor.getValue().getId()).isEqualTo(id);

    }

    private Ingredient createFakeIngredient(String id) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setDescription("Desc" + id);
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId("222");
        uom.setDescription("uom desc");
        ingredient.setUom(uom);
        return ingredient;
    }

    @Test
    public void saveIngredientCommand() {
        //given
        String recipeId = recipe.getId();
        String id = "2";
        Ingredient ingredientRepo = recipe.getIngredients()
                .stream()
                .filter(ingredient -> Objects.equals(ingredient.getId(), id))
                .findAny().get();
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId("12");
        uom.setDescription("BBottle");

        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId("12");
        uomCommand.setDescription("BBottle");

        IngredientCommand commandToSave = new IngredientCommand(id, recipeId, "New Description", BigDecimal.valueOf(333), uomCommand);

        given(recipeRepository.findById(anyString())).willReturn(Mono.just(recipe));
        given(recipeRepository.save(any(Recipe.class))).willReturn(Mono.just(recipe));
        given(uomRepository.findById(anyString())).willReturn(Mono.just(uom));
        given(toIngredientCommandConverter.convert(ingredientRepo)).willReturn(commandToSave);
        given(toIngredientConverter.convert(any())).willReturn(ingredientRepo);
        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(commandToSave).block();
        //then
        then(recipeRepository).should().findById(anyString());
        then(uomRepository).should().findById(anyString());
        then(recipeRepository).should().save(any(Recipe.class));
        then(toIngredientCommandConverter).should().convert(any());

    }

    @Test
    void testDeleteByIdAndRecipeId() {
        //given
        String recipeId = recipe.getId();
        String ingredientId = recipe.getIngredients().iterator().next().getId();
        given(recipeRepository.findById(anyString())).willReturn(Mono.just(recipe));
        given(recipeRepository.save(any(Recipe.class))).willReturn(Mono.just(recipe));
        //when
        Mono<Void> voidMono = ingredientService.deleteByIdAndRecipeId(ingredientId, recipeId);
        //then
        StepVerifier.create(voidMono)
                .expectSubscription()
                .verifyComplete();
        then(recipeRepository).should().findById(eq(recipeId));
        then(recipeRepository).should().save(any(Recipe.class));
    }
}
