package com.charamidis.lostAndFound.business;

import com.charamidis.lostAndFound.models.Record;
import com.charamidis.lostAndFound.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.Time;

public class RecordBusinessLogicTest {
    
    private User testUser;
    private Record testRecord;
    
    @BeforeEach
    void setUp() {
        testUser = new User(12345, "John", "Doe", "1990-01-01", 
                           "2023-01-01", "10:30:00", "admin");
        
        testRecord = new Record();
        testRecord.setId(1);
        testRecord.setOfficer_id(12345);
        testRecord.setRecord_date("2023-01-01T10:30:00");
        testRecord.setFounder_last_name("Smith");
        testRecord.setFounder_first_name("Jane");
        testRecord.setFounder_id_number("123456789");
        testRecord.setFounder_telephone("555-1234");
        testRecord.setFounder_street_address("123 Main St");
        testRecord.setFounder_street_number("1");
        testRecord.setFounder_father_name("Jane Sr");
        testRecord.setFounder_area_inhabitant("New York");
        testRecord.setFound_date(Date.valueOf("2023-01-01"));
        testRecord.setFound_time(Time.valueOf("10:30:00"));
        testRecord.setFound_location("Central Park");
        testRecord.setItem_description("Lost wallet");
        testRecord.setItem_category("Personal Items");
        testRecord.setItem_brand("Leather");
        testRecord.setItem_color("Brown");
        testRecord.setItem_serial_number("12345");
        testRecord.setItem_model("Wallet");
        testRecord.setStorage_location("Storage Room A");
        testRecord.setPicture_path("images/wallet.jpg");
    }
    
    @Test
    void testRecordValidation() {
        // Test valid record
        assertTrue(isValidRecord(testRecord));
        
        // Test record with missing required fields
        Record invalidRecord = new Record();
        invalidRecord.setId(2);
        invalidRecord.setOfficer_id(12345);
        invalidRecord.setRecord_date("2023-01-01T10:30:00");
        invalidRecord.setFounder_last_name(""); // Empty required field
        invalidRecord.setFounder_first_name("Jane");
        invalidRecord.setFounder_id_number("123456789");
        invalidRecord.setFounder_telephone("555-1234");
        invalidRecord.setFounder_street_address("123 Main St");
        invalidRecord.setFounder_street_number("1");
        invalidRecord.setFounder_father_name("Jane Sr");
        invalidRecord.setFounder_area_inhabitant("New York");
        invalidRecord.setFound_date(Date.valueOf("2023-01-01"));
        invalidRecord.setFound_time(Time.valueOf("10:30:00"));
        invalidRecord.setFound_location("Central Park");
        invalidRecord.setItem_description(""); // Empty required field
        invalidRecord.setItem_category("Personal Items");
        invalidRecord.setItem_brand("Leather");
        invalidRecord.setItem_color("Brown");
        invalidRecord.setItem_serial_number("12345");
        invalidRecord.setItem_model("Wallet");
        invalidRecord.setStorage_location("Storage Room A");
        invalidRecord.setPicture_path("images/wallet.jpg");
        
        assertFalse(isValidRecord(invalidRecord));
    }
    
    @Test
    void testUserRoleValidation() {
        // Test admin user
        assertTrue(isAdminUser(testUser));
        
        // Test regular user
        User regularUser = new User(12346, "Jane", "Smith", "1990-01-01", 
                                   "2023-01-01", "10:30:00", "user");
        assertFalse(isAdminUser(regularUser));
    }
    
    @Test
    void testRecordOwnership() {
        // Test record belongs to user
        assertTrue(doesRecordBelongToUser(testRecord, testUser));
        
        // Test record doesn't belong to different user
        User differentUser = new User(54321, "Bob", "Johnson", "1990-01-01", 
                                     "2023-01-01", "10:30:00", "user");
        assertFalse(doesRecordBelongToUser(testRecord, differentUser));
    }
    
    @Test
    void testRecordSearchCriteria() {
        // Test search by item description
        assertTrue(matchesSearchCriteria(testRecord, "wallet"));
        assertTrue(matchesSearchCriteria(testRecord, "Wallet"));
        assertFalse(matchesSearchCriteria(testRecord, "phone"));
        
        // Test search by founder name
        assertTrue(matchesSearchCriteria(testRecord, "Smith"));
        assertTrue(matchesSearchCriteria(testRecord, "Jane"));
        assertFalse(matchesSearchCriteria(testRecord, "Johnson"));
        
        // Test search by category
        assertTrue(matchesSearchCriteria(testRecord, "Personal"));
        assertFalse(matchesSearchCriteria(testRecord, "Electronics"));
    }
    
    @Test
    void testRecordDateValidation() {
        // Test valid date
        assertTrue(isValidDate("2023-01-01"));
        assertTrue(isValidDate("2023-12-31"));
        
        // Test invalid date
        assertFalse(isValidDate("2023-13-01")); // Invalid month
        assertFalse(isValidDate("2023-01-32")); // Invalid day
        assertFalse(isValidDate("invalid-date"));
    }
    
    @Test
    void testRecordTimeValidation() {
        // Test valid time
        assertTrue(isValidTime("10:30:00"));
        assertTrue(isValidTime("23:59:59"));
        assertTrue(isValidTime("00:00:00"));
        
        // Test invalid time
        assertFalse(isValidTime("24:00:00")); // Invalid hour
        assertFalse(isValidTime("10:60:00")); // Invalid minute
        assertFalse(isValidTime("10:30:60")); // Invalid second
        assertFalse(isValidTime("invalid-time"));
    }
    
    // Helper methods for business logic validation
    private boolean isValidRecord(Record record) {
        return record != null &&
               record.getFounder_last_name() != null && !record.getFounder_last_name().trim().isEmpty() &&
               record.getFounder_first_name() != null && !record.getFounder_first_name().trim().isEmpty() &&
               record.getItem_description() != null && !record.getItem_description().trim().isEmpty() &&
               record.getFound_location() != null && !record.getFound_location().trim().isEmpty();
    }
    
    private boolean isAdminUser(User user) {
        return user != null && "admin".equals(user.getRole());
    }
    
    private boolean doesRecordBelongToUser(Record record, User user) {
        return record != null && user != null && record.getOfficer_id() == user.getAm();
    }
    
    private boolean matchesSearchCriteria(Record record, String searchTerm) {
        if (record == null || searchTerm == null || searchTerm.trim().isEmpty()) {
            return false;
        }
        
        String lowerSearchTerm = searchTerm.toLowerCase();
        return (record.getItem_description() != null && record.getItem_description().toLowerCase().contains(lowerSearchTerm)) ||
               (record.getFounder_last_name() != null && record.getFounder_last_name().toLowerCase().contains(lowerSearchTerm)) ||
               (record.getFounder_first_name() != null && record.getFounder_first_name().toLowerCase().contains(lowerSearchTerm)) ||
               (record.getItem_category() != null && record.getItem_category().toLowerCase().contains(lowerSearchTerm));
    }
    
    private boolean isValidDate(String dateString) {
        try {
            Date.valueOf(dateString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    private boolean isValidTime(String timeString) {
        try {
            Time.valueOf(timeString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
