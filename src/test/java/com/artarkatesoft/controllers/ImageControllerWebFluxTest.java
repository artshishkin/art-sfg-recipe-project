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
        ByteArrayResource byteArrayResource = new FileByteArrayResource(imageBytes);
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("imagefile", byteArrayResource)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
        ;
        MultiValueMap<String, HttpEntity<?>> body = multipartBodyBuilder.build();

        //when
        webTestClient.post().uri("/recipe/{id}/image", "someId")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    static class FileByteArrayResource extends ByteArrayResource {

        public FileByteArrayResource(byte[] byteArray) {
            super(byteArray);
        }

        @Override
        public String getFilename() {
            return "artFileName";
        }
    }
}
