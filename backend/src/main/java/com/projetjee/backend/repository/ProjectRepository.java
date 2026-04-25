package com.projetjee.backend.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projetjee.backend.entity.Project;
import com.projetjee.backend.enums.ProjectStatus;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("""
        select distinct p
        from Project p
        left join p.employes e
        where p.chefProjet.id = :employeeId or e.id = :employeeId
    """)
    List<Project> findVisibleProjectsForEmployee(Long employeeId);

    long countByStatut(ProjectStatus statut);

    @Query("select coalesce(sum(p.budget), 0) from Project p")
    BigDecimal sumBudget();
}
