package com.artarkatesoft.services;

import com.artarkatesoft.domain.Recipe;
import com.artarkatesoft.exceptions.NotFoundException;
import com.artarkatesoft.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @InjectMocks
    ImageServiceImpl imageService;

    @Mock
    RecipeReactiveRepository recipeRepository;

    @Captor
    ArgumentCaptor<Recipe> recipeArgumentCaptor;

    @Test
    @DisplayName("Store Image to DB when recipe found")
    void saveImageFileWhenRecipeFound() {
        //given
        byte[] fakeImage = "Fake Image here".getBytes();

        Part filePart = mock(Part.class);
        Mono<Part> partMono = Mono.just(filePart);
        DefaultDataBuffer buffer = new DefaultDataBufferFactory().allocateBuffer();
        buffer.write(fakeImage);
        given(filePart.content()).willReturn(Flux.just(buffer));

        String recipeId = "1L";
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        given(recipeRepository.findById(anyString())).willReturn(Mono.just(recipe));
        given(recipeRepository.save(any(Recipe.class))).willReturn(Mono.just(recipe));
        //when
        Mono<Void> saveImageFileMono = imageService.saveImageFile(recipeId, partMono);
        //then
        StepVerifier.create(saveImageFileMono)
                .expectSubscription()
                .verifyComplete();

        then(recipeRepository).should().findById(eq(recipeId));
        then(recipeRepository).should().save(recipeArgumentCaptor.capture());
        then(recipeRepository).shouldHaveNoMoreInteractions();
        Recipe recipeSaved = recipeArgumentCaptor.getValue();
        assertThat(recipeSaved.getImage()).hasSize(fakeImage.length);
    }

    @Test
    @DisplayName("Store Image to DB when recipe NOT found")
    void saveImageFileWhenRecipeNotFound() {
        //given
        Part filePart = mock(Part.class);
        Mono<Part> partMono = Mono.just(filePart);
        String recipeId = "1L";
        given(recipeRepository.findById(anyString())).willReturn(Mono.empty());
        //when
        Mono<Void> saveImageFileMono = imageService.saveImageFile(recipeId, partMono);
        //then
        StepVerifier.create(saveImageFileMono)
                .expectSubscription()
                .verifyError(NotFoundException.class);

        then(recipeRepository).should().findById(eq(recipeId));
        then(recipeRepository).should(never()).save(any(Recipe.class));
        then(recipeRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void testGetImageByRecipeId() {
        //given
        String recipeId = "1L";
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        byte[] fakeImage = "This is fake image".getBytes();
        recipe.setImage(fakeImage);
        given(recipeRepository.findById(anyString())).willReturn(Mono.just(recipe));

        //when
        byte[] retrievedImage = imageService.getImageByRecipeId(recipeId).block();
        //then
        then(recipeRepository).should(times(1)).findById(eq(recipeId));
        assertThat(retrievedImage).isEqualTo(fakeImage);
    }
}
