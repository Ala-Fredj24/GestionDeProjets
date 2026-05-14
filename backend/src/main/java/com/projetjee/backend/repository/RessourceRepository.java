package com.projetjee.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projetjee.backend.entity.Ressource;

public interface RessourceRepository extends JpaRepository<Ressource, Long> {
}
