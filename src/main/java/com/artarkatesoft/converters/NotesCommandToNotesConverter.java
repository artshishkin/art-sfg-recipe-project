package com.artarkatesoft.converters;

import com.artarkatesoft.commands.NotesCommand;
import com.artarkatesoft.domain.Notes;
import org.springframework.stereotype.Component;

@Component
public class NotesCommandToNotesConverter extends AbstractConverter<NotesCommand, Notes> {
}
