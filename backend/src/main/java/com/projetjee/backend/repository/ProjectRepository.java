package com.projetjee.backend.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projetjee.backend.entity.Project;
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("""
        select distinct p
        from Project p
        left join p.employes e
        where p.chefProjet.id = :employeeId or e.id = :employeeId
    """)
    List<Project> findVisibleProjectsForEmployee(Long employeeId);
}
