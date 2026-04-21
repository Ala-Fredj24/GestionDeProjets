package com.projetjee.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projetjee.backend.repository.EmployeeRepository;
import com.projetjee.backend.entity.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmployeeService {
	private final EmployeeRepository employeeRepository;
	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}
	public Employee getEmployeeById(Long id) {
		return employeeRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee introuvable id " + id));
	}
	
	public Employee createEmployee(Employee employee) {
			validateUniqueEmail(employee.getEmail());
		return employeeRepository.save(employee);
	}

	private void validateUniqueEmail(String email) {
		// TODO Auto-generated method stub
		if (employeeRepository.findByEmail(email).isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email déjà utilisé : " + email);
		}
		
	}
	public Employee updateEmployee(Long id, Employee employeeDetails) {
		Employee existingEmployee = getEmployeeById(id);
		if (!existingEmployee.getEmail().equals(employeeDetails.getEmail())) {
			validateUniqueEmail(employeeDetails.getEmail());
		}
		existingEmployee.setNom(employeeDetails.getNom());
		existingEmployee.setEmail(employeeDetails.getEmail());
		existingEmployee.setRole(employeeDetails.getRole());
		existingEmployee.setEquipe(employeeDetails.getEquipe());
		
		return employeeRepository.save(existingEmployee);
	}
	public void deleteEmployee(Long id) {
		Employee existingEmployee = getEmployeeById(id);
		employeeRepository.delete(existingEmployee);
	}
	
}
