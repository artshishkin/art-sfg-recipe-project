package com.artarkatesoft.domain;



import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
    //    private Difficulty difficulty;
    @Lob
    private byte[] image;

    @OneToOne(cascade = CascadeType.ALL)
    private Notes notes;

}
