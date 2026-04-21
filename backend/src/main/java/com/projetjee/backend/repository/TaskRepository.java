package com.projetjee.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.projetjee.backend.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByProjetId(Long projetId);

}
