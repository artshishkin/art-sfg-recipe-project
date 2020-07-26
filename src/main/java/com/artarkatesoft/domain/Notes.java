package com.artarkatesoft.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Notes {

    @Id
    private String id;
    private String notes;
}
