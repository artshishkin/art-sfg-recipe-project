package com.artarkatesoft.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
public class Category {


    private String id;
    private String description;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Recipe> recipes = new HashSet<>();

    public Category(String description) {
        this.description = description;
    }
}
