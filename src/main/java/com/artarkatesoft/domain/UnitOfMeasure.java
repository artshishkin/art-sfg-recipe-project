package com.artarkatesoft.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document
public class UnitOfMeasure {

    @Id
    private String id;
    private String description;

    public UnitOfMeasure(String description) {
        this.description = description;
    }
}
