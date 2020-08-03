package com.artarkatesoft.repositories.reactive;

import com.artarkatesoft.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class UnitOfMeasureReactiveRepositoryTestIT {

    @Autowired
    UnitOfMeasureReactiveRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll().block();
    }

    @Test
    void addOne_thenFetchAll() {
        //given
        UnitOfMeasure uom = new UnitOfMeasure("New Unit Of Measure");
        //when
        Flux<UnitOfMeasure> uomFlux = repository.save(uom)
                .thenMany(repository.findAll());

        //then
        StepVerifier.create(uomFlux)
                .expectSubscription()
                .expectNextMatches(uomSaved -> uom.getDescription().equals(uomSaved.getDescription()))
                .verifyComplete();
    }

    @Test
    void addSome_thenFindByDescription() {
        //given
        Flux<UnitOfMeasure> defaultUomFlux = Flux.just(
                new UnitOfMeasure("Desc Default"),
                new UnitOfMeasure("Desc Default"),
                new UnitOfMeasure("Desc another"));
        //when
        Flux<UnitOfMeasure> uomDescDefaultFlux = repository.saveAll(defaultUomFlux)
                .thenMany(repository.findByDescription("Desc Default"));

        //then
        StepVerifier.create(uomDescDefaultFlux)
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
