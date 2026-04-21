package com.projetjee.backend.entity;
import java.time.LocalDate;

import com.projetjee.backend.enums.TaskPriority;
import  com.projetjee.backend.enums.TaskStatus;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="taches")
public class Task {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message="Le projet est obligatoire")
	@ManyToOne
	@JoinColumn(name="projet_id", nullable=false)
	private Project projet;
	
	@NotBlank(message="Le responsable est obligatoire")
	@Column( nullable=false)
	private String responsable;
	
	@NotBlank(message="La description est obligatoire")
	@Column(nullable=false)
	private String description;
	
	@NotNull(message="L'etat est obligatoire")
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private TaskStatus statut;
	
	
	@NotNull(message="la priorite est obligatoire")
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private TaskPriority priorite;
	
	@NotNull(message="la date limite est obligatoire")
	@FutureOrPresent(message="la date limite doit etre dans le futur ou aujourd'hui")
	@Column(name="date_limite",nullable=false)
	private LocalDate dateLimite;
	
	public Task() {
		// TODO Auto-generated constructor stub
	}
	public Task(Project projet, String responsable, String description, TaskStatus statut, LocalDate dateLimite, TaskPriority priorite) {
		this.projet = projet;
		this.responsable = responsable;
		this.description = description;
		this.statut = statut;
		this.dateLimite = dateLimite;
		this.priorite = priorite;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Project getProjet() {
		return projet;
	}
	public void setProjet(Project projet) {
		this.projet = projet;
	}
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public TaskStatus getStatus() {
		return statut;
	}
	public void setStatus(TaskStatus status) {
		this.statut = status;
	}
	public TaskPriority getPriorite() {
		return priorite;
	}
	public void setPriorite(TaskPriority priorite) {
		this.priorite = priorite;
	}
	public LocalDate getDateLimite() {
		return dateLimite;
	}
	public void setDateLimite(LocalDate dateLimite) {
		this.dateLimite = dateLimite;
	}
	
	
	


}
