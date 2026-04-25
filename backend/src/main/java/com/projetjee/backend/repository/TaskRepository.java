package com.projetjee.backend.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projetjee.backend.entity.Task;
import com.projetjee.backend.enums.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByProjetId(Long projetId);

	long countByStatut(TaskStatus statut);

	@Query("select coalesce(sum(t.coutPrevu), 0) from Task t")
	BigDecimal sumCoutPrevu();

	@Query("select coalesce(sum(t.coutReel), 0) from Task t")
	BigDecimal sumCoutReel();
}
