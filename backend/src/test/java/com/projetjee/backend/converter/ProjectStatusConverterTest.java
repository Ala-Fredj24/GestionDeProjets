package com.projetjee.backend.converter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.projetjee.backend.enums.ProjectStatus;

@DisplayName("ProjectStatusConverter Tests")
class ProjectStatusConverterTest {

    private final ProjectStatusConverter converter = new ProjectStatusConverter();

    // ===== convertToDatabaseColumn =====
    @Test
    @DisplayName("Should convert ProjectStatus to database string")
    void testConvertToDatabaseColumn_Success() {
        String result = converter.convertToDatabaseColumn(ProjectStatus.En_Cours);
        assertEquals("En_Cours", result);
    }

    @Test
    @DisplayName("Should convert all ProjectStatus enum values to database")
    void testConvertToDatabaseColumn_AllValues() {
        assertEquals("Programmé", converter.convertToDatabaseColumn(ProjectStatus.Programmé));
        assertEquals("En_Cours", converter.convertToDatabaseColumn(ProjectStatus.En_Cours));
        assertEquals("Completé", converter.convertToDatabaseColumn(ProjectStatus.Completé));
        assertEquals("Annulé", converter.convertToDatabaseColumn(ProjectStatus.Annulé));
    }

    @Test
    @DisplayName("Should convert null to null")
    void testConvertToDatabaseColumn_Null() {
        String result = converter.convertToDatabaseColumn(null);
        assertNull(result);
    }

    // ===== convertToEntityAttribute =====
    @Test
    @DisplayName("Should convert database string to ProjectStatus")
    void testConvertToEntityAttribute_ExactMatch() {
        ProjectStatus result = converter.convertToEntityAttribute("En_Cours");
        assertEquals(ProjectStatus.En_Cours, result);
    }

    @Test
    @DisplayName("Should convert all ProjectStatus enum values from database")
    void testConvertToEntityAttribute_AllValues() {
        assertEquals(ProjectStatus.Programmé, converter.convertToEntityAttribute("Programmé"));
        assertEquals(ProjectStatus.En_Cours, converter.convertToEntityAttribute("En_Cours"));
        assertEquals(ProjectStatus.Completé, converter.convertToEntityAttribute("Completé"));
        assertEquals(ProjectStatus.Annulé, converter.convertToEntityAttribute("Annulé"));
    }

    @Test
    @DisplayName("Should normalize and convert variations of project status")
    void testConvertToEntityAttribute_Normalized() {
        assertEquals(ProjectStatus.Programmé, converter.convertToEntityAttribute("programme"));
        assertEquals(ProjectStatus.Programmé, converter.convertToEntityAttribute("programe"));
        assertEquals(ProjectStatus.En_Cours, converter.convertToEntityAttribute("en_cours"));
        assertEquals(ProjectStatus.En_Cours, converter.convertToEntityAttribute("encours"));
        assertEquals(ProjectStatus.Completé, converter.convertToEntityAttribute("complete"));
        assertEquals(ProjectStatus.Completé, converter.convertToEntityAttribute("complet"));
        assertEquals(ProjectStatus.Annulé, converter.convertToEntityAttribute("annule"));
        assertEquals(ProjectStatus.Annulé, converter.convertToEntityAttribute("annulé"));
    }

    @Test
    @DisplayName("Should handle lowercase versions")
    void testConvertToEntityAttribute_Lowercase() {
        assertEquals(ProjectStatus.Programmé, converter.convertToEntityAttribute("programmé"));
        assertEquals(ProjectStatus.En_Cours, converter.convertToEntityAttribute("en_cours"));
    }

    @Test
    @DisplayName("Should convert null to null")
    void testConvertToEntityAttribute_Null() {
        ProjectStatus result = converter.convertToEntityAttribute(null);
        assertNull(result);
    }
}
