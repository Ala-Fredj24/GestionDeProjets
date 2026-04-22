package com.projetjee.backend.converter;

import java.text.Normalizer;

import com.projetjee.backend.enums.TaskPriority;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TaskPriorityConverter implements AttributeConverter<TaskPriority, String> {

    @Override
    public String convertToDatabaseColumn(TaskPriority attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public TaskPriority convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        String normalized = normalize(dbData);

        return switch (normalized) {
            case "faible" -> TaskPriority.Faible;
            case "moyenne" -> TaskPriority.Moyenne;
            case "eleve", "elevé", "éleve", "élevé" -> TaskPriority.Elevé;
            default -> TaskPriority.valueOf(dbData);
        };
    }

    private String normalize(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return normalized.trim().toLowerCase().replace(" ", "_");
    }
}
