package com.projetjee.backend.converter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.projetjee.backend.enums.TaskPriority;

@DisplayName("TaskPriorityConverter Tests")
class TaskPriorityConverterTest {

    private final TaskPriorityConverter converter = new TaskPriorityConverter();

    // ===== convertToDatabaseColumn =====
    @Test
    @DisplayName("Should convert TaskPriority to database string")
    void testConvertToDatabaseColumn_Success() {
        String result = converter.convertToDatabaseColumn(TaskPriority.Moyenne);
        assertEquals("Moyenne", result);
    }

    @Test
    @DisplayName("Should convert all TaskPriority enum values to database")
    void testConvertToDatabaseColumn_AllValues() {
        assertEquals("Faible", converter.convertToDatabaseColumn(TaskPriority.Faible));
        assertEquals("Moyenne", converter.convertToDatabaseColumn(TaskPriority.Moyenne));
        assertEquals("Elevé", converter.convertToDatabaseColumn(TaskPriority.Elevé));
    }

    @Test
    @DisplayName("Should convert null to null")
    void testConvertToDatabaseColumn_Null() {
        String result = converter.convertToDatabaseColumn(null);
        assertNull(result);
    }

    // ===== convertToEntityAttribute =====
    @Test
    @DisplayName("Should convert database string to TaskPriority")
    void testConvertToEntityAttribute_ExactMatch() {
        TaskPriority result = converter.convertToEntityAttribute("Moyenne");
        assertEquals(TaskPriority.Moyenne, result);
    }

    @Test
    @DisplayName("Should convert all TaskPriority enum values from database")
    void testConvertToEntityAttribute_AllValues() {
        assertEquals(TaskPriority.Faible, converter.convertToEntityAttribute("Faible"));
        assertEquals(TaskPriority.Moyenne, converter.convertToEntityAttribute("Moyenne"));
        assertEquals(TaskPriority.Elevé, converter.convertToEntityAttribute("Elevé"));
    }

    @Test
    @DisplayName("Should normalize and convert variations of task priority")
    void testConvertToEntityAttribute_Normalized() {
        assertEquals(TaskPriority.Faible, converter.convertToEntityAttribute("faible"));
        assertEquals(TaskPriority.Moyenne, converter.convertToEntityAttribute("moyenne"));
        assertEquals(TaskPriority.Elevé, converter.convertToEntityAttribute("eleve"));
        assertEquals(TaskPriority.Elevé, converter.convertToEntityAttribute("elevé"));
        assertEquals(TaskPriority.Elevé, converter.convertToEntityAttribute("éleve"));
        assertEquals(TaskPriority.Elevé, converter.convertToEntityAttribute("élevé"));
    }

    @Test
    @DisplayName("Should handle case insensitivity")
    void testConvertToEntityAttribute_CaseInsensitive() {
        assertEquals(TaskPriority.Faible, converter.convertToEntityAttribute("FAIBLE"));
        assertEquals(TaskPriority.Moyenne, converter.convertToEntityAttribute("MOYENNE"));
        assertEquals(TaskPriority.Elevé, converter.convertToEntityAttribute("ELEVÉ"));
    }

    @Test
    @DisplayName("Should convert null to null")
    void testConvertToEntityAttribute_Null() {
        TaskPriority result = converter.convertToEntityAttribute(null);
        assertNull(result);
    }
}
