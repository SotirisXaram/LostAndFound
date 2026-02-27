package com.charamidis.lostAndFound.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserConstructorAndGetters() {
        User user = new User(12345, "John", "Doe", "1990-01-01", "2026-02-23", "22:00:00", "admin");
        
        assertEquals(12345, user.getAm());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("1990-01-01", user.getBirthday());
        assertEquals("2026-02-23", user.getDateLoggedIn());
        assertEquals("22:00:00", user.getTimeLoggedIn());
        assertEquals("admin", user.getRole());
    }

    @Test
    void testUserSetters() {
        User user = new User();
        user.setAm(54321);
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setBirthday("1995-05-05");
        user.setDateLoggedIn("2026-02-24");
        user.setTimeLoggedIn("09:00:00");
        user.setRole("user");
        
        assertEquals(54321, user.getAm());
        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("1995-05-05", user.getBirthday());
        assertEquals("2026-02-24", user.getDateLoggedIn());
        assertEquals("09:00:00", user.getTimeLoggedIn());
        assertEquals("user", user.getRole());
    }

    @Test
    void testClear() {
        User user = new User(12345, "John", "Doe", "1990-01-01", "2026-02-23", "22:00:00", "admin");
        assertTrue(user.clear());
        
        assertEquals(-1, user.getAm());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getBirthday());
        assertNull(user.getDateLoggedIn());
        assertNull(user.getTimeLoggedIn());
        assertNull(user.getRole());
    }

    @Test
    void testToString() {
        User user = new User(12345, "John", "Doe", "1990-01-01", "2026-02-23", "22:00:00", "admin");
        String toString = user.toString();
        
        assertTrue(toString.contains("am=12345"));
        assertTrue(toString.contains("firstName='John'"));
        assertTrue(toString.contains("lastName='Doe'"));
        assertTrue(toString.contains("role='admin'"));
    }
}
