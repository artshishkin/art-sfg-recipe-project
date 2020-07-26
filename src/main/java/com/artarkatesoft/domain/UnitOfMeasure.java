package com.artarkatesoft.domain;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UnitOfMeasure {

    private String id;
    private String description;

    public UnitOfMeasure(String description) {
        this.description = description;
    }
}
