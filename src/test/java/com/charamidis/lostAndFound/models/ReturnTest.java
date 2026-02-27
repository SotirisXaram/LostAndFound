package com.charamidis.lostAndFound.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReturnTest {

    @Test
    void testReturnConstructorAndGetters() {
        Return returnObj = new Return(1, 12345, "Smith", "Jane", "2026-02-23", "11:00:00", "2107654321", "ID987654", "Tom", "1995-05-15", "Second St", "20", "2026-02-23 11:05:00", "Handed over to owner");
        
        assertEquals(1, returnObj.getId());
        assertEquals(12345, returnObj.getReturnOfficer());
        assertEquals("Smith", returnObj.getReturnLastName());
        assertEquals("Jane", returnObj.getReturnFirstName());
        assertEquals("2026-02-23", returnObj.getReturnDate());
        assertEquals("Handed over to owner", returnObj.getComment());
    }

    @Test
    void testReturnSetters() {
        Return returnObj = new Return();
        returnObj.setId(5);
        returnObj.setReturnOfficer(54321);
        returnObj.setReturnLastName("Brown");
        returnObj.setComment("Verified with ID");
        
        assertEquals(5, returnObj.getId());
        assertEquals(54321, returnObj.getReturnOfficer());
        assertEquals("Brown", returnObj.getReturnLastName());
        assertEquals("Verified with ID", returnObj.getComment());
    }
}
