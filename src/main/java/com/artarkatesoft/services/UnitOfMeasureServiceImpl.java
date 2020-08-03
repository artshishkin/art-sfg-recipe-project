package com.artarkatesoft.services;

import com.artarkatesoft.commands.UnitOfMeasureCommand;
import com.artarkatesoft.converters.UnitOfMeasureToUnitOfMeasureCommandConverter;
import com.artarkatesoft.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureReactiveRepository uomReactiveRepository;
    private final UnitOfMeasureToUnitOfMeasureCommandConverter converter;

    @Override
    public Flux<UnitOfMeasureCommand> listAllUoms() {
        return uomReactiveRepository.findAll()
                .map(converter::convert);
    }
}
