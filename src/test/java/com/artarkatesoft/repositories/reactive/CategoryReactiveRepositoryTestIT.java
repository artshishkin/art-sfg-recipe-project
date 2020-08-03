package com.artarkatesoft.repositories.reactive;

import com.artarkatesoft.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class CategoryReactiveRepositoryTestIT {

    @Autowired
    CategoryReactiveRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll().block();
    }

    @Test
    void addOne_thenFetchAll() {
        //given
        Category category = new Category("New Category");
        //when
        Flux<Category> categoryFlux = repository.save(category)
                .thenMany(repository.findAll());

        //then
        StepVerifier.create(categoryFlux)
                .expectSubscription()
                .expectNextMatches(uomSaved -> category.getDescription().equals(uomSaved.getDescription()))
                .verifyComplete();
    }
    
    @Test
    void addSome_thenFindByDescription() {
        //given
        Flux<Category> defaultCategoryFlux = Flux.just(
                new Category("Desc Default"),
                new Category("Desc Default"),
                new Category("Desc another"));
        //when
        Flux<Category> categoryDescDefaultFlux = repository.saveAll(defaultCategoryFlux)
                .thenMany(repository.findByDescription("Desc Default"));

        //then
        StepVerifier.create(categoryDescDefaultFlux)
                .expectSubscription()
                .recordWith(ArrayList::new)
                .thenConsumeWhile(
                        uom -> true,
                        uom -> assertThat(uom.getDescription()).isEqualTo("Desc Default")
                )
                .consumeRecordedWith(
                        uomList -> assertThat(uomList).hasSize(2)
                )
                .verifyComplete();
    }
    
    
    
}
