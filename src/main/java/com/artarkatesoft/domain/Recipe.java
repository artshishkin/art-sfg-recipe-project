package com.artarkatesoft.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Lob
    private byte[] image;

    @OneToOne(cascade = CascadeType.ALL)
    private Notes notes;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<Ingredient> ingredients;

    @ManyToMany
//    @JoinTable
//    @JoinTable(name = "recipe_category",
//            joinColumns = @JoinColumn(name = "recipe_id"),
//            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

}
