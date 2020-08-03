package com.artarkatesoft.repositories.reactive;

import com.artarkatesoft.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CategoryReactiveRepository extends ReactiveMongoRepository<Category, String> {
    Flux<Category> findByDescription(String description);
}
