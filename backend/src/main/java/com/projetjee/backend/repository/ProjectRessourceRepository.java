package com.projetjee.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projetjee.backend.entity.ProjectRessource;

public interface ProjectRessourceRepository extends JpaRepository<ProjectRessource, Long> {
    List<ProjectRessource> findByProjetId(Long projetId);

    Optional<ProjectRessource> findByIdAndProjetId(Long id, Long projetId);

    boolean existsByProjetIdAndRessourceId(Long projetId, Long ressourceId);

}
