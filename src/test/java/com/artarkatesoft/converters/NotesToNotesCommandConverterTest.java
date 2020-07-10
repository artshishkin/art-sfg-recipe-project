package com.artarkatesoft.converters;

import com.artarkatesoft.commands.NotesCommand;
import com.artarkatesoft.domain.Notes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotesToNotesCommandConverterTest {
    private static NotesToNotesCommandConverter converter;
    private static Notes notes;

    @BeforeAll
    static void setUp() {
        converter = new NotesToNotesCommandConverter();
        notes = new Notes();
        notes.setId(100L);
        notes.setNotes("notes");
    }

    @Test
    void testNullSource() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptySource() {
        assertNotNull(converter.convert(new Notes()));
    }

    @Test
    void convert() {
        NotesCommand command = converter.convert(notes);

        assertNotNull(command);
        assertAll(
                () -> assertEquals(notes.getId(), command.getId()),
                () -> assertEquals(notes.getNotes(), command.getNotes())
        );

    }



}
