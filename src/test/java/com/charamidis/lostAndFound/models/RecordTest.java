package com.charamidis.lostAndFound.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.Time;

public class RecordTest {
    
    private Record record;
    
    @BeforeEach
    void setUp() {
        record = new Record();
        record.setId(1);
        record.setOfficer_id(12345);
        record.setRecord_date("2023-01-01T10:30:00");
        record.setFounder_last_name("Smith");
        record.setFounder_first_name("John");
        record.setFounder_id_number("123456789");
        record.setFounder_telephone("555-1234");
        record.setFounder_street_address("123 Main St");
        record.setFounder_street_number("1");
        record.setFounder_father_name("John Sr");
        record.setFounder_area_inhabitant("New York");
        record.setFound_date(Date.valueOf("2023-01-01"));
        record.setFound_time(Time.valueOf("10:30:00"));
        record.setFound_location("Central Park");
        record.setItem_description("Lost wallet");
        record.setItem_category("Personal Items");
        record.setItem_brand("Leather");
        record.setItem_color("Brown");
        record.setItem_serial_number("12345");
        record.setItem_model("Wallet");
        record.setStorage_location("Storage Room A");
        record.setPicture_path("images/wallet.jpg");
    }
    
    @Test
    void testRecordCreation() {
        assertNotNull(record);
        assertEquals(1, record.getId());
        assertEquals(12345, record.getOfficer_id());
        assertEquals("2023-01-01T10:30:00", record.getRecord_date());
        assertEquals("Smith", record.getFounder_last_name());
        assertEquals("John", record.getFounder_first_name());
        assertEquals("123456789", record.getFounder_id_number());
        assertEquals("555-1234", record.getFounder_telephone());
        assertEquals("123 Main St", record.getFounder_street_address());
        assertEquals("1", record.getFounder_street_number());
        assertEquals("John Sr", record.getFounder_father_name());
        assertEquals("New York", record.getFounder_area_inhabitant());
        assertEquals("Central Park", record.getFound_location());
        assertEquals("Lost wallet", record.getItem_description());
        assertEquals("Personal Items", record.getItem_category());
        assertEquals("Leather", record.getItem_brand());
        assertEquals("Brown", record.getItem_color());
        assertEquals("12345", record.getItem_serial_number());
        assertEquals("Wallet", record.getItem_model());
        assertEquals("Storage Room A", record.getStorage_location());
        assertEquals("images/wallet.jpg", record.getPicture_path());
    }
    
    @Test
    void testSetters() {
        record.setFounder_last_name("Johnson");
        record.setFounder_first_name("Jane");
        record.setItem_description("Lost phone");
        
        assertEquals("Johnson", record.getFounder_last_name());
        assertEquals("Jane", record.getFounder_first_name());
        assertEquals("Lost phone", record.getItem_description());
    }
    
    @Test
    void testToString() {
        String recordString = record.toString();
        assertTrue(recordString.contains("Smith"));
        assertTrue(recordString.contains("John"));
        assertTrue(recordString.contains("Lost wallet"));
    }
}
