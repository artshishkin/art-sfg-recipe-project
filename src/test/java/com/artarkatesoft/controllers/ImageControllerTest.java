package com.artarkatesoft.controllers;

import com.artarkatesoft.services.ImageService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        mockMvc = MockMvcBuilders
                .standaloneSetup(imageController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void testShowImageUploadForm() throws Exception {
        //given
        String recipeId = "id123";
        //when
        mockMvc
                .perform(get("/recipe/{id}/image", recipeId))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/image_upload_form"))
                .andExpect(model().attributeExists("recipe_id"))
                .andExpect(model().attribute("recipe_id", CoreMatchers.equalTo(recipeId)));
        //then

    }

//    @Test
//    void handleImagePost() throws Exception {
//        //given
//        MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txtx", "text/plain", "ArtArKateSoft.com".getBytes());
//        String recipeId = "1L";
//        //when
//        mockMvc
//                .perform(
//                        multipart("/recipe/{id}/image", recipeId).file(multipartFile))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(header().string("Location", "/recipe/" + recipeId + "/show"));
//        //then
//        then(imageService).should().saveImageFile(eq(recipeId), eq(multipartFile));
//    }

//    @Test
//    void renderImageFromDB() throws Exception {
//        //given
//        String recipeId = "1L";
//        byte[] imageBytes = "This is fake image".getBytes();
//        given(imageService.getImageByRecipeId(anyString())).willReturn(imageBytes);
//
//        //when
//        MvcResult mvcResult = mockMvc.perform(get("/recipe/{id}/recipe_image", recipeId))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(IMAGE_JPEG_VALUE))
//                .andReturn();
//        //then
//        then(imageService).should().getImageByRecipeId(eq(recipeId));
//        MockHttpServletResponse response = mvcResult.getResponse();
//        byte[] receivedImage = response.getContentAsByteArray();
//        assertThat(receivedImage).isEqualTo(imageBytes);
//    }

    @Test
    @Disabled("now conversion from String to Long is not required so no need to throw exception")
    @DisplayName("when get Image of Recipe by ID with wrong String value should return Status 400")
    void renderImageFromDBByIdWhenWrongFormat() throws Exception {
        //when
        mockMvc.perform(get("/recipe/{id}/recipe_image", "BlaBla"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(view().name("400error"))
                .andExpect(model().attributeExists("exception"));
        //then
        then(imageService).shouldHaveNoInteractions();
    }
}
