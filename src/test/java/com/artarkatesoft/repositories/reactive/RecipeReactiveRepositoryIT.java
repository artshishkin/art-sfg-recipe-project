package com.artarkatesoft.repositories.reactive;

import com.artarkatesoft.bootstrap.DataInitializer;
import com.artarkatesoft.domain.Category;
import com.artarkatesoft.domain.Notes;
import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.domain.UnitOfMeasure;
import com.artarkatesoft.repositories.CategoryRepository;
import com.artarkatesoft.repositories.RecipeRepository;
import com.artarkatesoft.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class RecipeReactiveRepositoryIT {

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    @Autowired
    UnitOfMeasureReactiveRepository uomReactiveRepository;


    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        recipeReactiveRepository.deleteAll().block();
        categoryReactiveRepository.deleteAll().block();
        uomReactiveRepository.deleteAll().block();

        DataInitializer dataInitializer = new DataInitializer(recipeRepository, unitOfMeasureRepository, categoryRepository);
        dataInitializer.setRecipeReactiveRepository(recipeReactiveRepository);
        dataInitializer.setUomReactiveRepository(uomReactiveRepository);
        dataInitializer.setCategoryReactiveRepository(categoryReactiveRepository);

        dataInitializer.onApplicationEvent(null);
    }

    @Test
    void addOne_thenFetchAll() {
        //given
        Category category = categoryReactiveRepository.findAll().blockFirst();
        UnitOfMeasure uom = uomReactiveRepository.findAll().blockFirst();

        Recipe recipeToSave = new Recipe();
        recipeToSave.setSource("source");
        Notes notes = new Notes();
        notes.setNotes("Notes many notes");
        notes.setId(UUID.randomUUID().toString());
        recipeToSave.setNotes(notes);
        recipeToSave.setDirections("Directions");
        recipeToSave.setUrl("www.example.com");
        recipeToSave.setServings(3);
        recipeToSave.setCookTime(14);
        recipeToSave.setPrepTime(15);
        recipeToSave.addCategory(category);

        Long initialCount = recipeReactiveRepository.count().block();

        //when
        String recipeId = recipeReactiveRepository.save(recipeToSave).map(Recipe::getId).block();

        //then
        StepVerifier.create(recipeReactiveRepository.count())
                .expectSubscription()
                .expectNext(initialCount + 1)
                .verifyComplete();

        StepVerifier.create(recipeReactiveRepository.findById(recipeId))
                .assertNext(recipe -> assertThat(recipe).isEqualToIgnoringNullFields(recipeToSave))
                .verifyComplete();
    }
}
