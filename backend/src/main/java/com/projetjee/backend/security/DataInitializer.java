package com.projetjee.backend.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@admin.com")) {
            User admin = new User();
            admin.setEmail("admin@admin.com");
            admin.setMotDePasse(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setEmploye(null);

            userRepository.save(admin);
        }
    }
}