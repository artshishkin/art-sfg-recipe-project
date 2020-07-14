package com.artarkatesoft.controllers;

import com.artarkatesoft.services.ImageService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @InjectMocks
    ImageController imageController;

    @Mock
    ImageService imageService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    void testShowImageUploadForm() throws Exception {
        //given
        Long recipeId = 1L;
        //when
        mockMvc
                .perform(get("/recipe/{id}/image", recipeId))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/image_upload_form"))
                .andExpect(model().attributeExists("recipe_id"))
                .andExpect(model().attribute("recipe_id", CoreMatchers.equalTo(recipeId)));
        //then

    }

    @Test
    void handleImagePost() throws Exception {
        //given
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txtx", "text/plain", "ArtArKateSoft.com".getBytes());
        Long recipeId = 1L;
        //when
        mockMvc
                .perform(
                        multipart("/recipe/{id}/image", recipeId).file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/" + recipeId + "/show"));
        //then
        then(imageService).should().saveImageFile(eq(recipeId), eq(multipartFile));
    }
}
