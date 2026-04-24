package com.projetjee.backend.security;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtAuthenticationFilter Tests")
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private ApplicationUserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private UserDetails testUserDetails;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        testUserDetails = User.builder()
                .username("user@example.com")
                .password("password")
                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                .build();
    }

    @Test
    @DisplayName("Should process request with valid JWT token")
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        String token = "valid_jwt_token";
        String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(token)).thenReturn("user@example.com");
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(testUserDetails);
        when(jwtService.isTokenValid(token, "user@example.com")).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(request, times(1)).getHeader("Authorization");
        verify(jwtService, times(1)).extractUsername(token);
        verify(userDetailsService, times(1)).loadUserByUsername("user@example.com");
        verify(jwtService, times(1)).isTokenValid(token, "user@example.com");
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Should skip authentication when no Authorization header")
    void testDoFilterInternal_NoAuthHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(request, times(1)).getHeader("Authorization");
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractUsername(anyString());
    }

    @Test
    @DisplayName("Should skip authentication when Authorization header doesn't start with Bearer")
    void testDoFilterInternal_InvalidAuthHeaderFormat() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNz");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(request, times(1)).getHeader("Authorization");
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractUsername(anyString());
    }

    @Test
    @DisplayName("Should skip authentication when extracted email is null")
    void testDoFilterInternal_NullEmail() throws ServletException, IOException {
        String token = "invalid_token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(request, times(1)).getHeader("Authorization");
        verify(jwtService, times(1)).extractUsername(token);
        verify(filterChain, times(1)).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    @DisplayName("Should skip authentication when token is invalid")
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        String token = "invalid_jwt_token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn("user@example.com");
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(testUserDetails);
        when(jwtService.isTokenValid(token, "user@example.com")).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(request, times(1)).getHeader("Authorization");
        verify(jwtService, times(1)).extractUsername(token);
        verify(userDetailsService, times(1)).loadUserByUsername("user@example.com");
        verify(jwtService, times(1)).isTokenValid(token, "user@example.com");
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Should handle malformed token gracefully")
    void testDoFilterInternal_MalformedToken() throws ServletException, IOException {
        String token = "malformed";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenThrow(new RuntimeException("Invalid token"));

        try {
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        } catch (Exception e) {
            // Expected behavior for malformed token
        }

        verify(request, times(1)).getHeader("Authorization");
    }

    @Test
    @DisplayName("Should continue chain for all requests")
    void testDoFilterInternal_ChainContinuesAlways() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }
}
