package com.artarkatesoft.controllers;

import com.artarkatesoft.services.RecipeService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

@WebMvcTest(controllers = IndexController.class)
class IndexControllerMVCTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RecipeService recipeService;


    @Test
    void index() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipes"))
                .andExpect(MockMvcResultMatchers.model().attribute("recipes", Is.isA(Set.class)));
    }
}
