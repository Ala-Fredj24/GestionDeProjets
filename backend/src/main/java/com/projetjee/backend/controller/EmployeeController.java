package com.projetjee.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.security.access.prepost.PreAuthorize;
import com.projetjee.backend.entity.Employee;
import com.projetjee.backend.service.EmployeeService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employes")
@CrossOrigin(origins = "http://localhost:4200")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> recupererTousLesEmployes() {
        return employeeService.recupererTousLesEmployes();
    }

    @GetMapping("/{id}")
    public Employee recupererEmployeParId(@PathVariable Long id) {
        return employeeService.recupererEmployeParId(id);
    }

    @PostMapping
    public ResponseEntity<Employee> creerEmploye(@Valid @RequestBody Employee employee) {
        Employee createdEmployee = employeeService.creerEmploye(employee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> mettreAJourEmploye(@PathVariable Long id, @Valid @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.mettreAJourEmploye(id, employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerEmploye(@PathVariable Long id) {
        employeeService.supprimerEmploye(id);
        return ResponseEntity.noContent().build();
    }
}