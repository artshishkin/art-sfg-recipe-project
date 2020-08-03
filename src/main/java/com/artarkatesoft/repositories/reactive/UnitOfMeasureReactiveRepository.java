package com.artarkatesoft.repositories.reactive;

import com.artarkatesoft.domain.UnitOfMeasure;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface UnitOfMeasureReactiveRepository extends ReactiveMongoRepository<UnitOfMeasure, String> {
    Flux<UnitOfMeasure> findByDescription(String description);
}
