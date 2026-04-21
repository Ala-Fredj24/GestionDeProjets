package com.projetjee.backend.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projetjee.backend.entity.Project;
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
