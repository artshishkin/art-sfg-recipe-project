package com.artarkatesoft.controllers;

import com.artarkatesoft.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/recipe/{id}/image")
    public String showImageUploadForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("recipe_id", id);
        return "/recipe/image_upload_form";
    }

    @PostMapping("/recipe/{id}/image")
    public String handleImageUploadForm(@PathVariable("id") String id,
                                        @RequestParam("imagefile") MultipartFile file) {
        imageService.saveImageFile(id, file).block();
        return "redirect:/recipe/" + id + "/show";
    }

    @ResponseBody
    @GetMapping(value = "/recipe/{id}/recipe_image", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getRecipeImage(@PathVariable("id") String recipeId) {
        return imageService.getImageByRecipeId(recipeId).block();
    }

}
