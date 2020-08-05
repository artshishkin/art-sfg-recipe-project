package com.artarkatesoft.controllers;

import com.artarkatesoft.services.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/recipe/{id}/image")
    public String showImageUploadForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("recipe_id", id);
        return "/recipe/image_upload_form";
    }

    @PostMapping(value = "/recipe/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> handleImageUploadForm(@PathVariable("id") String id,
                                              @RequestPart("imagefile") Mono<Part> filePart) {
        return imageService.saveImageFile(id, filePart)
                .then(Mono.just("redirect:/recipe/" + id + "/show"));
    }
//
//    @PostMapping("/recipe/{id}/image")
//    public String handleImageUploadForm(@PathVariable("id") String id,
//                                        @RequestParam("imagefile") MultipartFile file) {
//        imageService.saveImageFile(id, file).block();
//        return "redirect:/recipe/" + id + "/show";
//    }

    @ResponseBody
    @GetMapping(value = "/recipe/{id}/recipe_image", produces = MediaType.IMAGE_JPEG_VALUE)
    public Mono<byte[]> getRecipeImage(@PathVariable("id") String recipeId) {
        return imageService.getImageByRecipeId(recipeId);
    }

}
