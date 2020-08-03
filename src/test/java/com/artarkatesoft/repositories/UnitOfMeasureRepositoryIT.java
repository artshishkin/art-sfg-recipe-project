package com.artarkatesoft.repositories;

import com.artarkatesoft.bootstrap.DataInitializer;
import com.artarkatesoft.domain.UnitOfMeasure;
import com.artarkatesoft.repositories.reactive.CategoryReactiveRepository;
import com.artarkatesoft.repositories.reactive.RecipeReactiveRepository;
import com.artarkatesoft.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UnitOfMeasureReactiveRepository uomReactiveRepository;
    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;
    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    @BeforeEach
    void setUp() {
        categoryReactiveRepository.deleteAll().block();
        uomReactiveRepository.deleteAll().block();
        recipeReactiveRepository.deleteAll().block();

        DataInitializer dataInitializer = new DataInitializer(recipeRepository, unitOfMeasureRepository, categoryRepository);
        dataInitializer.setCategoryReactiveRepository(categoryReactiveRepository);
        dataInitializer.setUomReactiveRepository(uomReactiveRepository);
        dataInitializer.setRecipeReactiveRepository(recipeReactiveRepository);
        dataInitializer.onApplicationEvent(null);
    }

    @Test
    void findByDescriptionTablespoon() {

        String descriptionToFind = "Tablespoon";
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription(descriptionToFind);
        assertTrue(uomOptional.isPresent());
        UnitOfMeasure uom = uomOptional.get();
        assertThat(uom.getDescription()).isEqualTo(descriptionToFind);
    }

    @Test
    void findByDescriptionTeaspoon() {
        String descriptionToFind = "Teaspoon";
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription(descriptionToFind);
        assertTrue(uomOptional.isPresent());
        assertThat(uomOptional.get().getDescription()).isEqualTo(descriptionToFind);
    }

    @Test
    void findByDescriptionCup() {
        String descriptionToFind = "Cup";
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription(descriptionToFind);
        assertTrue(uomOptional.isPresent());
        assertThat(uomOptional.get().getDescription()).isEqualTo(descriptionToFind);
    }

    @Test
    void findByDescriptionPinch() {
        String descriptionToFind = "Pinch";
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription(descriptionToFind);
        assertTrue(uomOptional.isPresent());
        assertThat(uomOptional.get().getDescription()).isEqualTo(descriptionToFind);
    }

    @Test
    void findByDescriptionOunce() {
        String descriptionToFind = "Ounce";
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription(descriptionToFind);
        assertTrue(uomOptional.isPresent());
        assertThat(uomOptional.get().getDescription()).isEqualTo(descriptionToFind);
    }

    @Test
    void findByDescriptionDash() {
        String descriptionToFind = "Dash";
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription(descriptionToFind);
        assertTrue(uomOptional.isPresent());
        assertThat(uomOptional.get().getDescription()).isEqualTo(descriptionToFind);
    }
}
