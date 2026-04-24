package com.projetjee.backend.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.projetjee.backend.entity.Employee;

@DisplayName("JwtService Comprehensive Tests")
class JwtServiceTest {

    private JwtService jwtService;
    private final String testSecret = "dGhpc2lzYXZlcnlsb25nYW5kY29tcGxleHNlY3JldGtleWZvcmp3dGVuY3J5cHRpb24=";
    private final long testExpiration = 3600000;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", testSecret);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", testExpiration);
    }

    @Test
    @DisplayName("Should generate valid JWT token")
    void testGenerateToken_Success() {
        Employee employee = new Employee();
        employee.setId(1L);

        User user = new User();
        user.setEmail("test@example.com");
        user.setRole(Role.ADMIN);
        user.setEmploye(employee);

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertTrue(token.contains("."));
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    @DisplayName("Should extract username from valid token")
    void testExtractUsername_Success() {
        Employee employee = new Employee();
        employee.setId(1L);

        User user = new User();
        user.setEmail("test@example.com");
        user.setRole(Role.ADMIN);
        user.setEmploye(employee);

        String token = jwtService.generateToken(user);
        String extractedUsername = jwtService.extractUsername(token);

        assertEquals("test@example.com", extractedUsername);
    }

    @Test
    @DisplayName("Should validate correct token with matching email")
    void testIsTokenValid_ValidToken() {
        Employee employee = new Employee();
        employee.setId(1L);

        User user = new User();
        user.setEmail("test@example.com");
        user.setRole(Role.ADMIN);
        user.setEmploye(employee);

        String token = jwtService.generateToken(user);

        assertTrue(jwtService.isTokenValid(token, "test@example.com"));
    }

    @Test
    @DisplayName("Should reject token with mismatched email")
    void testIsTokenValid_WrongEmail() {
        Employee employee = new Employee();
        employee.setId(1L);

        User user = new User();
        user.setEmail("test@example.com");
        user.setRole(Role.ADMIN);
        user.setEmploye(employee);

        String token = jwtService.generateToken(user);

        assertFalse(jwtService.isTokenValid(token, "wrong@example.com"));
    }

    @Test
    @DisplayName("Should generate token with null employee")
    void testIsTokenValid_NullEmployee() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setRole(Role.ADMIN);
        user.setEmploye(null);

        String token = jwtService.generateToken(user);

        assertTrue(jwtService.isTokenValid(token, "test@example.com"));
    }

    @Test
    @DisplayName("Should handle different user roles")
    void testGenerateToken_DifferentRoles() {
        User userAdmin = new User();
        userAdmin.setEmail("admin@example.com");
        userAdmin.setRole(Role.ADMIN);

        User userChef = new User();
        userChef.setEmail("chef@example.com");
        userChef.setRole(Role.CHEF_PROJET);

        User userEmployee = new User();
        userEmployee.setEmail("employee@example.com");
        userEmployee.setRole(Role.EMPLOYE);

        String tokenAdmin = jwtService.generateToken(userAdmin);
        String tokenChef = jwtService.generateToken(userChef);
        String tokenEmployee = jwtService.generateToken(userEmployee);

        assertNotNull(tokenAdmin);
        assertNotNull(tokenChef);
        assertNotNull(tokenEmployee);
        assertNotEquals(tokenAdmin, tokenChef);
    }

    @Test
    @DisplayName("Should extract expiration from token")
    void testExtractExpiration_Success() {
        Employee employee = new Employee();
        employee.setId(1L);

        User user = new User();
        user.setEmail("test@example.com");
        user.setRole(Role.ADMIN);
        user.setEmploye(employee);

        String token = jwtService.generateToken(user);
        
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, "test@example.com"));
    }

    @Test
    @DisplayName("Should handle special characters in email")
    void testGenerateToken_SpecialEmail() {
        User user = new User();
        user.setEmail("user+test@example.co.uk");
        user.setRole(Role.EMPLOYE);

        String token = jwtService.generateToken(user);
        String extractedEmail = jwtService.extractUsername(token);

        assertEquals("user+test@example.co.uk", extractedEmail);
        assertTrue(jwtService.isTokenValid(token, "user+test@example.co.uk"));
    }
}
