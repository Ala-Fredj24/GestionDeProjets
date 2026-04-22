package com.projetjee.backend.converter;

import java.text.Normalizer;

import com.projetjee.backend.enums.ProjectStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class ProjectStatusConverter implements AttributeConverter<ProjectStatus, String> {

    @Override
    public String convertToDatabaseColumn(ProjectStatus attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public ProjectStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        String normalized = normalize(dbData);

        return switch (normalized) {
            case "programme", "programe" -> ProjectStatus.Programmé;
            case "en_cours", "encours" -> ProjectStatus.En_Cours;
            case "complete", "complet" -> ProjectStatus.Completé;
            case "annule", "annulé" -> ProjectStatus.Annulé;
            default -> ProjectStatus.valueOf(dbData);
        };
    }

    private String normalize(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return normalized.trim().toLowerCase().replace(" ", "_");
    }
}
