package com.artarkatesoft.controllers;

import com.artarkatesoft.services.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.codec.multipart.Part;
import org.springframework.ui.Model;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @InjectMocks
    ImageController imageController;

    @Mock
    ImageService imageService;

    @Mock
    Model model;

    @Test
    void testShowImageUploadForm() throws Exception {
        //given
        String recipeId = "id123";
        //when
        String view = imageController.showImageUploadForm(recipeId, model);
        //then
        then(model).should().addAttribute(eq("recipe_id"),eq(recipeId));
        assertThat(view).isEqualTo("/recipe/image_upload_form");
    }

    @Test
    void handleImagePost() throws Exception {
        //given
        String recipeId = "1L";
        Part filePart = mock(Part.class);
        Mono<Part> partMono = Mono.just(filePart);
        given(imageService.saveImageFile(anyString(), any(Mono.class))).willReturn(Mono.empty());

        //when
        Mono<String> viewMono = imageController.handleImageUploadForm(recipeId, partMono);
        //then
        StepVerifier.create(viewMono)
                .expectSubscription()
                .expectNext("redirect:/recipe/" + recipeId + "/show")
                .verifyComplete();
        then(imageService).should().saveImageFile(eq(recipeId), eq(partMono));
    }

    @Test
    void renderImageFromDB() throws Exception {
        //given
        String recipeId = "1L";
        byte[] imageBytes = "This is fake image".getBytes();
        given(imageService.getImageByRecipeId(anyString())).willReturn(Mono.just(imageBytes));

        //when
        Mono<byte[]> recipeImage = imageController.getRecipeImage(recipeId);
        //then
        StepVerifier.create(recipeImage)
                .expectSubscription()
                .expectNext(imageBytes)
                .verifyComplete();
        then(imageService).should().getImageByRecipeId(eq(recipeId));
    }
}
