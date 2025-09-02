package com.hotel.erp.service;

import com.hotel.erp.entity.User;
import com.hotel.erp.enums.UserRole;
import com.hotel.erp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Initializes the database with a default admin user if none exists
 * This runs automatically when the application starts
 */
@Component
public class DataInitializationService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializationService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("DataInitializationService starting...");

        // Check for existing admin user - handle potential duplicates gracefully
        try {
            var existingAdmin = userRepository.findByUsername("admin");
            if (existingAdmin == null) {
                logger.info("No admin user found, creating default admin...");
                createAdminUser();
            } else {
                logger.info("Admin user already exists: {}", existingAdmin.getUsername());
            }
        } catch (Exception e) {
            logger.warn("Multiple admin users found in database or other error: {}", e.getMessage());
            logger.info("Skipping admin user creation due to existing duplicates");
        }

        // Check for existing test admin user - handle potential duplicates gracefully
        try {
            var existingTestAdmin = userRepository.findByUsername("testadmin");
            if (existingTestAdmin == null) {
                logger.info("No test admin user found, creating test admin...");
                createTestAdminUser();
            } else {
                logger.info("Test admin user already exists: {}", existingTestAdmin.getUsername());
            }
        } catch (Exception e) {
            logger.warn("Multiple testadmin users found in database or other error: {}", e.getMessage());
            logger.info("Skipping test admin user creation due to existing duplicates");
        }
    }

    private void createAdminUser() {
        try {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setShaPassword(passwordEncoder.encode("admin").getBytes());
            admin.setFullName("System Administrator");
            admin.setUserRole(UserRole.ADMIN);
            admin.setUserType("Admin");
            admin.setIsActive(true);
            admin.setCreationDate(Date.valueOf(LocalDate.now()));
            admin.setCreatedAt(LocalDateTime.now());
            admin.setCreatedBy("SYSTEM");

            userRepository.save(admin);
            logger.info("Admin user created successfully!");
            logger.info("Username: admin");
            logger.info("Role: {}", admin.getUserRole());
            logger.info("Type: {}", admin.getUserType());
        } catch (Exception e) {
            logger.error("Error creating admin user: {}", e.getMessage());
        }
    }

    private void createTestAdminUser() {
        try {
            User testAdmin = new User();
            testAdmin.setUsername("testadmin");
            testAdmin.setPassword(passwordEncoder.encode("password"));
            testAdmin.setShaPassword(passwordEncoder.encode("password").getBytes());
            testAdmin.setFullName("Test Administrator");
            testAdmin.setUserRole(UserRole.ADMIN);
            testAdmin.setUserType("Admin");
            testAdmin.setIsActive(true);
            testAdmin.setCreationDate(Date.valueOf(LocalDate.now()));
            testAdmin.setCreatedAt(LocalDateTime.now());
            testAdmin.setCreatedBy("SYSTEM");

            userRepository.save(testAdmin);
            logger.info("Test admin user created successfully!");
            logger.info("Username: testadmin");
            logger.info("Role: {}", testAdmin.getUserRole());
            logger.info("Type: {}", testAdmin.getUserType());
        } catch (Exception e) {
            logger.error("Error creating test admin user: {}", e.getMessage());
        }
    }
}
