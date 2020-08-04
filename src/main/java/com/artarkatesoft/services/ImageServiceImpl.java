package com.artarkatesoft.services;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.exceptions.NotFoundException;
import com.artarkatesoft.repositories.reactive.RecipeReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final RecipeReactiveRepository recipeRepository;

    @Override
    public Mono<Void> saveImageFile(String recipeId, MultipartFile file) {
        log.debug("Receive a file");
        Mono<Recipe> recipeMono = recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe with id " + recipeId + " not found")));
        return recipeMono
                .map(recipe -> {

                    byte[] fileBytes = new byte[0];
                    try {
                        fileBytes = file.getBytes();
                        recipe.setImage(fileBytes);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to read read byte array from multipart file");
                    }
                    return recipe;
                })
                .flatMap(recipeRepository::save)
                .then();
    }

    @Override
    public Mono<byte[]> getImageByRecipeId(String recipeId) {
        Mono<Recipe> recipeMono = recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe with id " + recipeId + " not found")));
        return recipeMono.flatMap(recipe -> Mono.justOrEmpty(recipe.getImage()));
    }
}
