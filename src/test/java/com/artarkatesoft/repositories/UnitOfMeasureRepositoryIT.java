package com.artarkatesoft.repositories;

import com.artarkatesoft.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void findByDescriptionTablespoon() {

        String descriptionToFind = "Tablespoon";
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription(descriptionToFind);
        assertTrue(uomOptional.isPresent());
        UnitOfMeasure uom = uomOptional.get();
        assertThat(uom.getDescription()).isEqualTo(descriptionToFind);
    }
    @Test
    void findByDescriptionTeaspoon() {
        String descriptionToFind = "Teaspoon";
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription(descriptionToFind);
        assertTrue(uomOptional.isPresent());
        assertThat(uomOptional.get().getDescription()).isEqualTo(descriptionToFind);
    }
    @Test
    void findByDescriptionCup() {
        String descriptionToFind = "Cup";
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription(descriptionToFind);
        assertTrue(uomOptional.isPresent());
        assertThat(uomOptional.get().getDescription()).isEqualTo(descriptionToFind);
    }
    @Test
    void findByDescriptionPinch() {
        String descriptionToFind = "Pinch";
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription(descriptionToFind);
        assertTrue(uomOptional.isPresent());
        assertThat(uomOptional.get().getDescription()).isEqualTo(descriptionToFind);
    }
    @Test
    void findByDescriptionOunce() {
        String descriptionToFind = "Ounce";
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription(descriptionToFind);
        assertTrue(uomOptional.isPresent());
        assertThat(uomOptional.get().getDescription()).isEqualTo(descriptionToFind);
    }
    @Test
    void findByDescriptionDash() {
        String descriptionToFind = "Dash";
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription(descriptionToFind);
        assertTrue(uomOptional.isPresent());
        assertThat(uomOptional.get().getDescription()).isEqualTo(descriptionToFind);
    }
}
