package com.projetjee.backend.security.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AuthLoginRequest Tests")
class AuthLoginRequestTest {

    private AuthLoginRequest authRequest;

    @BeforeEach
    void setUp() {
        authRequest = new AuthLoginRequest();
    }

    @Test
    @DisplayName("Should create AuthLoginRequest with default constructor")
    void testDefaultConstructor() {
        assertNotNull(authRequest);
    }

    @Test
    @DisplayName("Should set and get email")
    void testEmail() {
        authRequest.setEmail("user@example.com");
        assertEquals("user@example.com", authRequest.getEmail());
    }

    @Test
    @DisplayName("Should set and get motDePasse")
    void testMotDePasse() {
        authRequest.setMotDePasse("password123");
        assertEquals("password123", authRequest.getMotDePasse());
    }

    @Test
    @DisplayName("Should handle null email")
    void testNullEmail() {
        authRequest.setEmail(null);
        assertNull(authRequest.getEmail());
    }

    @Test
    @DisplayName("Should handle null password")
    void testNullMotDePasse() {
        authRequest.setMotDePasse(null);
        assertNull(authRequest.getMotDePasse());
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        authRequest.setEmail("");
        authRequest.setMotDePasse("");
        
        assertEquals("", authRequest.getEmail());
        assertEquals("", authRequest.getMotDePasse());
    }

    @Test
    @DisplayName("Should handle special characters in email")
    void testSpecialEmailCharacters() {
        String specialEmail = "user+test@example.co.uk";
        authRequest.setEmail(specialEmail);
        assertEquals(specialEmail, authRequest.getEmail());
    }

    @Test
    @DisplayName("Should handle special characters in password")
    void testSpecialPasswordCharacters() {
        String specialPassword = "p@ssw0rd!#$%&*()_+-=[]{}|;:',.<>?";
        authRequest.setMotDePasse(specialPassword);
        assertEquals(specialPassword, authRequest.getMotDePasse());
    }

    @Test
    @DisplayName("Should handle long email")
    void testLongEmail() {
        String longEmail = "very.long.email.address.with.many.characters@example.subdomain.co.uk";
        authRequest.setEmail(longEmail);
        assertEquals(longEmail, authRequest.getEmail());
    }

    @Test
    @DisplayName("Should handle long password")
    void testLongPassword() {
        String longPassword = "thisIsAVeryLongPasswordWithManyCharactersThatShouldBeHandledProperly123!@#";
        authRequest.setMotDePasse(longPassword);
        assertEquals(longPassword, authRequest.getMotDePasse());
    }

    @Test
    @DisplayName("Should update email multiple times")
    void testEmailUpdate() {
        authRequest.setEmail("first@example.com");
        assertEquals("first@example.com", authRequest.getEmail());

        authRequest.setEmail("second@example.com");
        assertEquals("second@example.com", authRequest.getEmail());

        authRequest.setEmail("third@example.com");
        assertEquals("third@example.com", authRequest.getEmail());
    }

    @Test
    @DisplayName("Should update password multiple times")
    void testPasswordUpdate() {
        authRequest.setMotDePasse("password1");
        assertEquals("password1", authRequest.getMotDePasse());

        authRequest.setMotDePasse("password2");
        assertEquals("password2", authRequest.getMotDePasse());

        authRequest.setMotDePasse("password3");
        assertEquals("password3", authRequest.getMotDePasse());
    }

    @Test
    @DisplayName("Should support multiple instances independently")
    void testMultipleInstances() {
        AuthLoginRequest request1 = new AuthLoginRequest();
        AuthLoginRequest request2 = new AuthLoginRequest();

        request1.setEmail("user1@example.com");
        request1.setMotDePasse("password1");

        request2.setEmail("user2@example.com");
        request2.setMotDePasse("password2");

        assertEquals("user1@example.com", request1.getEmail());
        assertEquals("password1", request1.getMotDePasse());

        assertEquals("user2@example.com", request2.getEmail());
        assertEquals("password2", request2.getMotDePasse());
    }

    @Test
    @DisplayName("Should handle whitespace in email")
    void testEmailWithWhitespace() {
        String emailWithSpace = "user name@example.com";
        authRequest.setEmail(emailWithSpace);
        assertEquals(emailWithSpace, authRequest.getEmail());
    }

    @Test
    @DisplayName("Should handle unicode characters in password")
    void testUnicodePassword() {
        String unicodePassword = "pässwörd123";
        authRequest.setMotDePasse(unicodePassword);
        assertEquals(unicodePassword, authRequest.getMotDePasse());
    }
}
