package com.artarkatesoft.services;

import com.artarkatesoft.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

public interface UnitOfMeasureService {
    Flux<UnitOfMeasureCommand> listAllUoms();
}
