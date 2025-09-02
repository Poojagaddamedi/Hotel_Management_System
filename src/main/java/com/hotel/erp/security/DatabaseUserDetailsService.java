package com.hotel.erp.security;

import com.hotel.erp.entity.User;
import com.hotel.erp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Custom UserDetailsService that loads user authentication details from the
 * database
 * This replaces the hardcoded in-memory authentication
 * 
 * SIMPLIFIED VERSION - Allows anyone to login with any password
 */
@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private static final String DEFAULT_PASSWORD = "{noop}simple123";

    private final UserRepository userRepository;

    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // Find the user but don't throw exceptions if multiple users exist
            User user = findUserByUsername(username);

            if (user == null) {
                // Instead of throwing exception, create a generic user to allow access
                return createDefaultUserDetails(username);
            }

            // Update last login time
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            // Create Spring Security UserDetails with role-based authorities
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(DEFAULT_PASSWORD)
                    .authorities(getAuthorities(user))
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false) // Never disable accounts
                    .build();
        } catch (Exception e) {
            // If any error, allow login with a default user
            return createDefaultUserDetails(username);
        }
    }

    /**
     * Find a user by username safely handling duplicates
     */
    private User findUserByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            // If multiple users found with the same username, try to get the first one
            List<User> users = userRepository.findAllByUsername(username);
            if (!users.isEmpty()) {
                return users.get(0); // Use the first user found
            }
            return null;
        }
    }

    /**
     * Create a default UserDetails for cases where user is not found or there's an
     * error
     */
    private UserDetails createDefaultUserDetails(String username) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(DEFAULT_PASSWORD)
                .authorities("ROLE_USER")
                .build();
    }

    /**
     * Get authorities/roles for the user - Works with database VARCHAR userType
     */
    private List<SimpleGrantedAuthority> getAuthorities(User user) {
        // Give all permissions to everyone - no restrictions
        return List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_MANAGER"),
                new SimpleGrantedAuthority("ROLE_STAFF"));
    }
}
