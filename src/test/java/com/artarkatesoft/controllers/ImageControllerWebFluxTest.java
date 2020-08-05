package com.artarkatesoft.controllers;

import com.artarkatesoft.services.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@WebFluxTest(controllers = ImageController.class)
class ImageControllerWebFluxTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    ImageService imageService;

    @Test
    void handleImageUploadForm() {
        //given
        byte[] imageBytes = "This is fake image".getBytes();
        ByteArrayResource byteArrayResource = new ByteArrayResource(imageBytes);
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("imagefile", byteArrayResource);
        MultiValueMap<String, HttpEntity<?>> body = multipartBodyBuilder.build();

        given(imageService.saveImageFile(anyString(), any(Mono.class))).willReturn(Mono.empty());

        //when
        webTestClient.post().uri("/recipe/{id}/image", "someId")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .exchange()
                //then
                .expectStatus().is3xxRedirection();
    }

    @Test
    void testShowImageUploadForm() throws Exception {
        //given
        String recipeId = "id123";
        //when
        webTestClient
                .get().uri("/recipe/{id}/image", recipeId)
                .exchange()
                //then
                .expectStatus().isOk();
    }

    @Test
    void renderImageFromDB() {
        //given
        String recipeId = "1L";
        byte[] imageBytes = "This is fake image".getBytes();
        given(imageService.getImageByRecipeId(anyString())).willReturn(Mono.just(imageBytes));

        //when
        byte[] bodyContent = webTestClient.get().uri("/recipe/{id}/recipe_image", recipeId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.IMAGE_JPEG)
                .returnResult(Object.class)
                .getResponseBodyContent();
        //then
        assertThat(bodyContent).isEqualTo(imageBytes);
        then(imageService).should().getImageByRecipeId(eq(recipeId));
    }

}
