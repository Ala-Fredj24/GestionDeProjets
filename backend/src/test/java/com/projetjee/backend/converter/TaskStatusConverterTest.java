package com.projetjee.backend.converter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.projetjee.backend.enums.TaskStatus;

@DisplayName("TaskStatusConverter Tests")
class TaskStatusConverterTest {

    private final TaskStatusConverter converter = new TaskStatusConverter();

    // ===== convertToDatabaseColumn =====
    @Test
    @DisplayName("Should convert TaskStatus to database string")
    void testConvertToDatabaseColumn_Success() {
        String result = converter.convertToDatabaseColumn(TaskStatus.En_Cours);
        assertEquals("En_Cours", result);
    }

    @Test
    @DisplayName("Should convert all TaskStatus enum values to database")
    void testConvertToDatabaseColumn_AllValues() {
        assertEquals("À_Faire", converter.convertToDatabaseColumn(TaskStatus.À_Faire));
        assertEquals("En_Cours", converter.convertToDatabaseColumn(TaskStatus.En_Cours));
        assertEquals("Complété", converter.convertToDatabaseColumn(TaskStatus.Complété));
        assertEquals("Annulé", converter.convertToDatabaseColumn(TaskStatus.Annulé));
    }

    @Test
    @DisplayName("Should convert null to null")
    void testConvertToDatabaseColumn_Null() {
        String result = converter.convertToDatabaseColumn(null);
        assertNull(result);
    }

    // ===== convertToEntityAttribute =====
    @Test
    @DisplayName("Should convert database string to TaskStatus")
    void testConvertToEntityAttribute_ExactMatch() {
        TaskStatus result = converter.convertToEntityAttribute("En_Cours");
        assertEquals(TaskStatus.En_Cours, result);
    }

    @Test
    @DisplayName("Should convert all TaskStatus enum values from database")
    void testConvertToEntityAttribute_AllValues() {
        assertEquals(TaskStatus.À_Faire, converter.convertToEntityAttribute("À_Faire"));
        assertEquals(TaskStatus.En_Cours, converter.convertToEntityAttribute("En_Cours"));
        assertEquals(TaskStatus.Complété, converter.convertToEntityAttribute("Complété"));
        assertEquals(TaskStatus.Annulé, converter.convertToEntityAttribute("Annulé"));
    }

    @Test
    @DisplayName("Should normalize and convert variations of task status")
    void testConvertToEntityAttribute_Normalized() {
        assertEquals(TaskStatus.À_Faire, converter.convertToEntityAttribute("a_faire"));
        assertEquals(TaskStatus.À_Faire, converter.convertToEntityAttribute("afaire"));
        assertEquals(TaskStatus.En_Cours, converter.convertToEntityAttribute("en_cours"));
        assertEquals(TaskStatus.En_Cours, converter.convertToEntityAttribute("encours"));
        assertEquals(TaskStatus.Complété, converter.convertToEntityAttribute("complete"));
        assertEquals(TaskStatus.Complété, converter.convertToEntityAttribute("complet"));
        assertEquals(TaskStatus.Annulé, converter.convertToEntityAttribute("annule"));
        assertEquals(TaskStatus.Annulé, converter.convertToEntityAttribute("annulé"));
    }

    @Test
    @DisplayName("Should handle lowercase versions")
    void testConvertToEntityAttribute_Lowercase() {
        assertEquals(TaskStatus.À_Faire, converter.convertToEntityAttribute("à_faire"));
        assertEquals(TaskStatus.En_Cours, converter.convertToEntityAttribute("en_cours"));
    }

    @Test
    @DisplayName("Should convert null to null")
    void testConvertToEntityAttribute_Null() {
        TaskStatus result = converter.convertToEntityAttribute(null);
        assertNull(result);
    }
}
