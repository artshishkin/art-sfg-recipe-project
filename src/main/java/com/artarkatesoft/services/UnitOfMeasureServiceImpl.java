package com.artarkatesoft.services;

import com.artarkatesoft.commands.UnitOfMeasureCommand;
import com.artarkatesoft.converters.UnitOfMeasureToUnitOfMeasureCommandConverter;
import com.artarkatesoft.repositories.UnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommandConverter converter;

    @Override
    public List<UnitOfMeasureCommand> listAllUoms() {
        return StreamSupport
                .stream(unitOfMeasureRepository.findAll().spliterator(), false)
                .map(converter::convert)
                .collect(Collectors.toList());
    }
}
