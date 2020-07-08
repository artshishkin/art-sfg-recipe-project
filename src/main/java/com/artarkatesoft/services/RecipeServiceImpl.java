package com.artarkatesoft.services;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    @Override
    public Set<Recipe> getAllRecipes() {
        log.debug("I'm in the service");
        Iterable<Recipe> iterable = recipeRepository.findAll();
//        return iterable;
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toSet());
    }

}
