package com.charamidis.lostAndFound.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.Time;

public class ReturnTest {
    
    private Return returnRecord;
    
    @BeforeEach
    void setUp() {
        returnRecord = new Return(1, 12345, "Doe", "Jane",
                                 "2023-01-02", "14:30:00", "555-5678", "987654321",
                                 "Jane Sr", "1990-01-01", "456 Oak Ave",
                                 "2", "2023-01-02T14:30:00", "Returned to owner");
    }
    
    @Test
    void testReturnCreation() {
        assertNotNull(returnRecord);
        assertEquals(1, returnRecord.getId());
        assertEquals(12345, returnRecord.getReturnOfficer());
        assertEquals("Doe", returnRecord.getReturnLastName());
        assertEquals("Jane", returnRecord.getReturnFirstName());
        assertEquals("2023-01-02", returnRecord.getReturnDate());
        assertEquals("14:30:00", returnRecord.getReturnTime());
        assertEquals("Returned to owner", returnRecord.getComment());
    }
    
    @Test
    void testSetters() {
        returnRecord.setReturnLastName("Johnson");
        returnRecord.setReturnFirstName("Bob");
        returnRecord.setComment("Verified identity");
        
        assertEquals("Johnson", returnRecord.getReturnLastName());
        assertEquals("Bob", returnRecord.getReturnFirstName());
        assertEquals("Verified identity", returnRecord.getComment());
    }
    
    @Test
    void testToString() {
        String returnString = returnRecord.toString();
        assertTrue(returnString.contains("Doe"));
        assertTrue(returnString.contains("Jane"));
        assertTrue(returnString.contains("Returned to owner"));
    }
}
