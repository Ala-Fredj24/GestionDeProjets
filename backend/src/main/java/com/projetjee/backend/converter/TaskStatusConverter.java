package com.projetjee.backend.converter;

import java.text.Normalizer;

import com.projetjee.backend.enums.TaskStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TaskStatusConverter implements AttributeConverter<TaskStatus, String> {

    @Override
    public String convertToDatabaseColumn(TaskStatus attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public TaskStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        String normalized = normalize(dbData);

        return switch (normalized) {
            case "a_faire", "afaire" -> TaskStatus.À_Faire;
            case "en_cours", "encours" -> TaskStatus.En_Cours;
            case "complete", "complet" -> TaskStatus.Complété;
            case "annule", "annulé" -> TaskStatus.Annulé;
            default -> TaskStatus.valueOf(dbData);
        };
    }

    private String normalize(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return normalized.trim().toLowerCase().replace(" ", "_");
    }
}
