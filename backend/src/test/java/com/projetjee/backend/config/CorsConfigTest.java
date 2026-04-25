package com.projetjee.backend.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.cors.CorsConfiguration;

class CorsConfigTest {

    @Test
    void shouldUseConfiguredCorsOrigins() {
        CorsConfig config = new CorsConfig();
        ReflectionTestUtils.setField(
                config,
                "allowedOrigins",
                "http://localhost:30080, http://localhost:4200, ");

        MockHttpServletRequest request = new MockHttpServletRequest("OPTIONS", "/api/projets");
        CorsConfiguration corsConfiguration = config.corsConfigurationSource().getCorsConfiguration(request);

        assertNotNull(corsConfiguration);
        assertEquals(
                java.util.List.of("http://localhost:30080", "http://localhost:4200"),
                corsConfiguration.getAllowedOrigins());
        assertTrue(corsConfiguration.getAllowedMethods().contains("GET"));
        assertTrue(corsConfiguration.getAllowedMethods().contains("OPTIONS"));
        assertEquals(Boolean.TRUE, corsConfiguration.getAllowCredentials());
    }
}
