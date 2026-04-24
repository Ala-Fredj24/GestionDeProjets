package com.projetjee.backend.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AuthResponse Tests")
class AuthResponseTest {

    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        authResponse = new AuthResponse();
    }

    @Test
    @DisplayName("Should create AuthResponse with default constructor")
    void testDefaultConstructor() {
        assertNotNull(authResponse);
    }

    @Test
    @DisplayName("Should set and get token")
    void testToken() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0";
        authResponse.setToken(token);
        assertEquals(token, authResponse.getToken());
    }

    @Test
    @DisplayName("Should set and get email")
    void testEmail() {
        authResponse.setEmail("user@example.com");
        assertEquals("user@example.com", authResponse.getEmail());
    }

    @Test
    @DisplayName("Should set and get role")
    void testRole() {
        authResponse.setRole("ADMIN");
        assertEquals("ADMIN", authResponse.getRole());
    }

    @Test
    @DisplayName("Should set and get employeeId")
    void testEmployeeId() {
        authResponse.setEmployeeId(1L);
        assertEquals(1L, authResponse.getEmployeeId());
    }

    @Test
    @DisplayName("Should work with full constructor")
    void testFullConstructor() {
        String token = "jwt_token_here";
        String email = "user@example.com";
        String role = "ADMIN";
        Long employeeId = 5L;

        AuthResponse response = new AuthResponse(token, email, role, employeeId);

        assertEquals(token, response.getToken());
        assertEquals(email, response.getEmail());
        assertEquals(role, response.getRole());
        assertEquals(employeeId, response.getEmployeeId());
    }

    @Test
    @DisplayName("Should handle null token")
    void testNullToken() {
        authResponse.setToken(null);
        assertNull(authResponse.getToken());
    }

    @Test
    @DisplayName("Should handle null email")
    void testNullEmail() {
        authResponse.setEmail(null);
        assertNull(authResponse.getEmail());
    }

    @Test
    @DisplayName("Should handle null role")
    void testNullRole() {
        authResponse.setRole(null);
        assertNull(authResponse.getRole());
    }

    @Test
    @DisplayName("Should handle null employeeId")
    void testNullEmployeeId() {
        authResponse.setEmployeeId(null);
        assertNull(authResponse.getEmployeeId());
    }

    @Test
    @DisplayName("Should handle different roles")
    void testDifferentRoles() {
        authResponse.setRole("ADMIN");
        assertEquals("ADMIN", authResponse.getRole());

        authResponse.setRole("CHEF_PROJET");
        assertEquals("CHEF_PROJET", authResponse.getRole());

        authResponse.setRole("EMPLOYE");
        assertEquals("EMPLOYE", authResponse.getRole());
    }

    @Test
    @DisplayName("Should handle large JWT token")
    void testLargeJwtToken() {
        String longToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ." +
                "SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        authResponse.setToken(longToken);
        assertEquals(longToken, authResponse.getToken());
    }

    @Test
    @DisplayName("Should handle zero employeeId")
    void testZeroEmployeeId() {
        authResponse.setEmployeeId(0L);
        assertEquals(0L, authResponse.getEmployeeId());
    }

    @Test
    @DisplayName("Should handle large employeeId")
    void testLargeEmployeeId() {
        Long largeId = 9999999L;
        authResponse.setEmployeeId(largeId);
        assertEquals(largeId, authResponse.getEmployeeId());
    }

    @Test
    @DisplayName("Should support multiple instances independently")
    void testMultipleInstances() {
        AuthResponse response1 = new AuthResponse("token1", "user1@example.com", "ADMIN", 1L);
        AuthResponse response2 = new AuthResponse("token2", "user2@example.com", "EMPLOYE", 2L);

        assertEquals("token1", response1.getToken());
        assertEquals("user1@example.com", response1.getEmail());
        assertEquals("ADMIN", response1.getRole());
        assertEquals(1L, response1.getEmployeeId());

        assertEquals("token2", response2.getToken());
        assertEquals("user2@example.com", response2.getEmail());
        assertEquals("EMPLOYE", response2.getRole());
        assertEquals(2L, response2.getEmployeeId());
    }

    @Test
    @DisplayName("Should update all fields")
    void testUpdateAllFields() {
        authResponse.setToken("token1");
        authResponse.setEmail("email1@example.com");
        authResponse.setRole("ADMIN");
        authResponse.setEmployeeId(1L);

        assertEquals("token1", authResponse.getToken());
        assertEquals("email1@example.com", authResponse.getEmail());
        assertEquals("ADMIN", authResponse.getRole());
        assertEquals(1L, authResponse.getEmployeeId());

        authResponse.setToken("token2");
        authResponse.setEmail("email2@example.com");
        authResponse.setRole("EMPLOYE");
        authResponse.setEmployeeId(2L);

        assertEquals("token2", authResponse.getToken());
        assertEquals("email2@example.com", authResponse.getEmail());
        assertEquals("EMPLOYE", authResponse.getRole());
        assertEquals(2L, authResponse.getEmployeeId());
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        authResponse.setToken("");
        authResponse.setEmail("");
        authResponse.setRole("");

        assertEquals("", authResponse.getToken());
        assertEquals("", authResponse.getEmail());
        assertEquals("", authResponse.getRole());
    }
}
