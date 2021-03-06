package com.artarkatesoft.services;

import com.artarkatesoft.commands.UnitOfMeasureCommand;
import com.artarkatesoft.converters.UnitOfMeasureToUnitOfMeasureCommandConverter;
import com.artarkatesoft.domain.UnitOfMeasure;
import com.artarkatesoft.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UnitOfMeasureServiceImplTest {

    @Mock
    UnitOfMeasureReactiveRepository repository;

    private UnitOfMeasureService uomService;
    private static List<UnitOfMeasure> repositoryUomList;

    @BeforeAll
    static void globalSetUp() {
        repositoryUomList = LongStream.rangeClosed(1, 5)
                .mapToObj(String::valueOf)
                .map(UnitOfMeasureServiceImplTest::createFakeUom)
                .collect(Collectors.toList());
    }

    @BeforeEach
    void setUp() {
        uomService = new UnitOfMeasureServiceImpl(repository,
                new UnitOfMeasureToUnitOfMeasureCommandConverter());

    }

    private static UnitOfMeasure createFakeUom(String id) {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(id);
        unitOfMeasure.setDescription("Uom Description " + id);
        return unitOfMeasure;
    }

    @Test
    void listAllUoms() {
        //given
        given(repository.findAll()).willReturn(Flux.fromIterable(repositoryUomList));

        //when
        List<UnitOfMeasureCommand> uomList = uomService.listAllUoms().collectList().block();

        //then
        then(repository).should(times(1)).findAll();
        assertThat(uomList)
                .isNotNull()
                .hasSameSizeAs(repositoryUomList)
                .doesNotContainNull();
    }
}
