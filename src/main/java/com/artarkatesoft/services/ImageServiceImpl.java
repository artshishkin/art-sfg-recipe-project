package com.artarkatesoft.services;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.exceptions.NotFoundException;
import com.artarkatesoft.repositories.reactive.RecipeReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final RecipeReactiveRepository recipeRepository;

    @Override
    public Mono<Void> saveImageFile(String recipeId, Mono<Part> filePart) {
        log.debug("Receive a file");
        Mono<Recipe> recipeMono = recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe with id " + recipeId + " not found")));
        return recipeMono
                .flatMap(recipe -> {
                    Flux<DataBuffer> dataBufferFlux = filePart.flatMapMany(part -> part.content());
                    Mono<DataBuffer> dataBufferMono = DataBufferUtils.join(dataBufferFlux);
                    Mono<byte[]> fileContent = dataBufferMono.map(dataBuffer -> dataBuffer.asByteBuffer().array());
                    return fileContent.map(image -> {
                        recipe.setImage(image);
                        return recipe;
                    });
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
