package com.artarkatesoft.converters;

import com.artarkatesoft.commands.NotesCommand;
import com.artarkatesoft.domain.Notes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotesCommandToNotesConverterTest {
    private static NotesCommandToNotesConverter converter;
    private static NotesCommand notesCommand;

    @BeforeAll
    static void setUp() {
        converter = new NotesCommandToNotesConverter();
        notesCommand = new NotesCommand();
        notesCommand.setId(100L);
        notesCommand.setNotes("notes");
    }

    @Test
    void testNullSource() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptySource() {
        assertNotNull(converter.convert(new NotesCommand()));
    }

    @Test
    void convert() {
        Notes notes = converter.convert(notesCommand);

        assertNotNull(notes);
        assertAll(
                () -> assertEquals(notesCommand.getId(), notes.getId()),
                () -> assertEquals(notesCommand.getNotes(), notes.getNotes())
        );

    }



}
