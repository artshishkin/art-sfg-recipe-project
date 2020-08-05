package com.artarkatesoft.services;

import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Mono;

public interface ImageService {
    Mono<Void> saveImageFile(String recipeId, Mono<Part> filePart);

    Mono<byte[]> getImageByRecipeId(String recipeId);
}
