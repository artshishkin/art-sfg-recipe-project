package com.artarkatesoft.services;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.repositories.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @InjectMocks
    ImageServiceImpl imageService;

    @Mock
    RecipeRepository recipeRepository;

    @Captor
    ArgumentCaptor<Recipe> recipeArgumentCaptor;

    @Test
    @DisplayName("Store Image to DB when recipe found")
    void saveImageFileWhenRecipeFound() {
        //given
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txtx", "text/plain", "ArtArKateSoft.com".getBytes());
        Long recipeId = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        given(recipeRepository.findById(anyLong())).willReturn(Optional.of(recipe));
        //when
        imageService.saveImageFile(recipeId, multipartFile);
        //then
        then(recipeRepository).should().findById(eq(recipeId));
        then(recipeRepository).should().save(recipeArgumentCaptor.capture());
        then(recipeRepository).shouldHaveNoMoreInteractions();
        Recipe recipeSaved = recipeArgumentCaptor.getValue();
        assertThat(recipeSaved.getImage().length).isEqualTo(multipartFile.getSize());
    }

    @Test
    @DisplayName("Store Image to DB when recipe NOT found")
    void saveImageFileWhenRecipeNotFound() {
        //given
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txtx", "text/plain", "ArtArKateSoft.com".getBytes());
        Long recipeId = 1L;
        given(recipeRepository.findById(anyLong())).willReturn(Optional.empty());
        //when
        Executable storeImageExecutable = () -> imageService.saveImageFile(recipeId, multipartFile);
        //then
        assertThrows(RuntimeException.class, storeImageExecutable);
        then(recipeRepository).should().findById(eq(recipeId));
        then(recipeRepository).should(never()).save(any(Recipe.class));
        then(recipeRepository).shouldHaveNoMoreInteractions();

    }
}
