package com.artarkatesoft.controllers;

import com.artarkatesoft.domain.Category;
import com.artarkatesoft.domain.UnitOfMeasure;
import com.artarkatesoft.repositories.CategoryRepository;
import com.artarkatesoft.repositories.UnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @RequestMapping({"/", "index"})
    public String index() {

        Optional<Category> optionalCategory = categoryRepository.findByDescription("American");
        Optional<UnitOfMeasure> optionalUnitOfMeasure = unitOfMeasureRepository.findByDescription("Teaspoon");

        System.out.println(optionalCategory.orElse(null));
        System.out.println(optionalUnitOfMeasure.orElse(null));

        return "index";
    }


}
