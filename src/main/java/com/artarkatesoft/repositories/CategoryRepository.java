package com.artarkatesoft.repositories;

import com.artarkatesoft.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
