package com.projetjee.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="employees")
public class Employee {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message="Le nom est obligatoire")
	@Column(nullable=false)
	private String nom;
	
	@NotBlank(message="L'email est obligatoire")
	@Email(message="L'email doit etre valide")
	@Column(nullable=false, unique=true)
	private String email;
	
	@NotBlank(message="Le role est obligatoire")
	@Column(nullable=false)
	private String role;
	
	@NotBlank(message="L'equipe est obligatoire")
	@Column(nullable=false)
	private String equipe;
	 
	public Employee() {
		// TODO Auto-generated constructor stub
	}
	public Employee(String nom, String email, String role, String equipe) {
		this.nom = nom;
		this.email = email;
		this.role = role;
		this.equipe = equipe;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getEquipe() {
		return equipe;
	}
	public void setEquipe(String equipe) {
		this.equipe = equipe;
	}
	
	

}
