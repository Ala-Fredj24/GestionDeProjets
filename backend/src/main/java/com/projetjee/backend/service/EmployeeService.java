package com.projetjee.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projetjee.backend.entity.Employee;
import com.projetjee.backend.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> recupererTousLesEmployes() {
        return employeeRepository.findAll();
    }

    public Employee recupererEmployeParId(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Employé introuvable avec l'id : " + id
                ));
    }

    public Employee creerEmploye(Employee employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Un employé avec cet email existe déjà."
            );
        }

        return employeeRepository.save(employee);
    }

    public Employee mettreAJourEmploye(Long id, Employee employeeDetails) {
        Employee existingEmployee = recupererEmployeParId(id);

        if (!existingEmployee.getEmail().equals(employeeDetails.getEmail())
                && employeeRepository.existsByEmail(employeeDetails.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Un employé avec cet email existe déjà."
            );
        }

        existingEmployee.setNom(employeeDetails.getNom());
        existingEmployee.setEmail(employeeDetails.getEmail());
        existingEmployee.setRole(employeeDetails.getRole());
        existingEmployee.setEquipe(employeeDetails.getEquipe());

        return employeeRepository.save(existingEmployee);
    }

    public void supprimerEmploye(Long id) {
        Employee existingEmployee = recupererEmployeParId(id);
        employeeRepository.delete(existingEmployee);
    }
}