package com.artarkatesoft.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Document
public class Category {

    @Id
    private String id;
    private String description;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @DBRef
    private Set<Recipe> recipes = new HashSet<>();

    public Category(String description) {
        this.description = description;
    }
}
