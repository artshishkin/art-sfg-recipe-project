package com.artarkatesoft.services;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    @SneakyThrows
    @Override
    public void saveImageFile(String recipeId, MultipartFile file) {
        log.debug("Receive a file");
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe with id " + recipeId + " not found"));
        recipe.setImage(file.getBytes());
        recipeRepository.save(recipe);
    }

    @Override
    public byte[] getImageByRecipeId(String recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe with id " + recipeId + "not found"));
        return recipe.getImage();
    }
}
