package com.charamidis.lostAndFound.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class UserTest {
    
    private User user;
    
    @BeforeEach
    void setUp() {
        user = new User(12345, "John", "Doe", "1990-01-01", 
                       "2023-01-01", "10:30:00", "admin");
    }
    
    @Test
    void testUserCreation() {
        assertNotNull(user);
        assertEquals(12345, user.getAm());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("1990-01-01", user.getBirthday());
        assertEquals("2023-01-01", user.getDateLoggedIn());
        assertEquals("10:30:00", user.getTimeLoggedIn());
        assertEquals("admin", user.getRole());
    }
    
    @Test
    void testSetters() {
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setRole("user");
        
        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("user", user.getRole());
    }
    
    @Test
    void testToString() {
        String userString = user.toString();
        assertTrue(userString.contains("John"));
        assertTrue(userString.contains("Doe"));
        assertTrue(userString.contains("12345"));
    }
}
