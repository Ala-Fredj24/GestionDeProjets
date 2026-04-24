package com.projetjee.backend.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.projetjee.backend.security.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApplicationUserDetailsService Tests")
class ApplicationUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApplicationUserDetailsService userDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("user@example.com");
        testUser.setMotDePasse("hashedPassword123");
        testUser.setRole(Role.ADMIN);
    }

    @Test
    @DisplayName("Should load user details by email successfully")
    void testLoadUserByUsername_Success() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));

        UserDetails result = userDetailsService.loadUserByUsername("user@example.com");

        assertNotNull(result);
        assertEquals("user@example.com", result.getUsername());
        assertEquals("hashedPassword123", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        verify(userRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void testLoadUserByUsername_NotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("notfound@example.com");
        });

        verify(userRepository, times(1)).findByEmail("notfound@example.com");
    }

    @Test
    @DisplayName("Should load user with CHEF_PROJET role")
    void testLoadUserByUsername_ChefRole() {
        User chefUser = new User();
        chefUser.setEmail("chef@example.com");
        chefUser.setMotDePasse("hashedPassword");
        chefUser.setRole(Role.CHEF_PROJET);

        when(userRepository.findByEmail("chef@example.com")).thenReturn(Optional.of(chefUser));

        UserDetails result = userDetailsService.loadUserByUsername("chef@example.com");

        assertNotNull(result);
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CHEF_PROJET")));

        verify(userRepository, times(1)).findByEmail("chef@example.com");
    }

    @Test
    @DisplayName("Should load user with EMPLOYE role")
    void testLoadUserByUsername_EmployeRole() {
        User employeeUser = new User();
        employeeUser.setEmail("employee@example.com");
        employeeUser.setMotDePasse("hashedPassword");
        employeeUser.setRole(Role.EMPLOYE);

        when(userRepository.findByEmail("employee@example.com")).thenReturn(Optional.of(employeeUser));

        UserDetails result = userDetailsService.loadUserByUsername("employee@example.com");

        assertNotNull(result);
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYE")));

        verify(userRepository, times(1)).findByEmail("employee@example.com");
    }

    @Test
    @DisplayName("Should handle case-sensitive email lookup")
    void testLoadUserByUsername_CaseSensitive() {
        when(userRepository.findByEmail("User@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("User@example.com");
        });

        verify(userRepository, times(1)).findByEmail("User@example.com");
    }

    @Test
    @DisplayName("Should load user with valid authorities collection")
    void testLoadUserByUsername_Authorities() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));

        UserDetails result = userDetailsService.loadUserByUsername("user@example.com");

        assertNotNull(result.getAuthorities());
        assertEquals(1, result.getAuthorities().size());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a instanceof SimpleGrantedAuthority));

        verify(userRepository, times(1)).findByEmail("user@example.com");
    }
}
