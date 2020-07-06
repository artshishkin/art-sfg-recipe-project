package com.artarkatesoft.repositories;

import com.artarkatesoft.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
