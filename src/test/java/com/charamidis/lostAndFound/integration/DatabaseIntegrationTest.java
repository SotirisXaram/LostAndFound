package com.charamidis.lostAndFound.integration;

import com.charamidis.lostAndFound.utils.SqliteDatabaseInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseIntegrationTest {
    
    private Connection connection;
    
    @BeforeEach
    void setUp() throws SQLException {
        // Initialize the database
        SqliteDatabaseInitializer.initializeDatabase();
        connection = SqliteDatabaseInitializer.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }
    
    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    
    @Test
    void testDatabaseConnection() throws SQLException {
        assertNotNull(connection);
        assertFalse(connection.isClosed());
        
        // Test basic query
        String query = "SELECT 1 as test";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            assertTrue(rs.next());
            assertEquals(1, rs.getInt("test"));
        }
    }
    
    @Test
    void testUsersTableExists() throws SQLException {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='users'";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            assertTrue(rs.next());
            assertEquals("users", rs.getString("name"));
        }
    }
    
    @Test
    void testRecordsTableExists() throws SQLException {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='records'";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            assertTrue(rs.next());
            assertEquals("records", rs.getString("name"));
        }
    }
    
    @Test
    void testReturnsTableExists() throws SQLException {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='returns'";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            assertTrue(rs.next());
            assertEquals("returns", rs.getString("name"));
        }
    }
    
    @Test
    void testActivitiesTableExists() throws SQLException {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='activities'";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            assertTrue(rs.next());
            assertEquals("activities", rs.getString("name"));
        }
    }
    
    @Test
    void testInsertAndRetrieveUser() throws SQLException {
        // Insert a test user
        String insertQuery = "INSERT INTO users (am, first_name, last_name, date_of_birth, password, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setInt(1, 99999);
            stmt.setString(2, "Test");
            stmt.setString(3, "User");
            stmt.setString(4, "1990-01-01");
            stmt.setString(5, "hashed_password");
            stmt.setString(6, "user");
            
            int rowsAffected = stmt.executeUpdate();
            assertEquals(1, rowsAffected);
        }
        
        // Retrieve the user
        String selectQuery = "SELECT * FROM users WHERE am = ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
            stmt.setInt(1, 99999);
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(99999, rs.getInt("am"));
                assertEquals("Test", rs.getString("first_name"));
                assertEquals("User", rs.getString("last_name"));
                assertEquals("1990-01-01", rs.getString("date_of_birth"));
                assertEquals("hashed_password", rs.getString("password"));
                assertEquals("user", rs.getString("role"));
            }
        }
        
        // Clean up
        String deleteQuery = "DELETE FROM users WHERE am = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
            stmt.setInt(1, 99999);
            int rowsAffected = stmt.executeUpdate();
            assertEquals(1, rowsAffected);
        }
    }
    
    @Test
    void testInsertAndRetrieveRecord() throws SQLException {
        // Insert a test record
        String insertQuery = "INSERT INTO records (id, officer_id, record_datetime, founder_last_name, founder_first_name, " +
                           "founder_id_number, founder_telephone, founder_street_address, founder_street_number, " +
                           "founder_father_name, founder_area_inhabitant, found_date, found_time, found_location, " +
                           "item_description, item_category, item_brand, item_model, item_color, item_serial_number, " +
                           "storage_location, picture_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setInt(1, 99999);
            stmt.setInt(2, 12345);
            stmt.setString(3, "2023-01-01T10:30:00");
            stmt.setString(4, "Test");
            stmt.setString(5, "Record");
            stmt.setString(6, "123456789");
            stmt.setString(7, "555-1234");
            stmt.setString(8, "123 Test St");
            stmt.setString(9, "1");
            stmt.setString(10, "Test Sr");
            stmt.setString(11, "Test City");
            stmt.setString(12, "2023-01-01");
            stmt.setString(13, "10:30:00");
            stmt.setString(14, "Test Location");
            stmt.setString(15, "Test Item");
            stmt.setString(16, "Test Category");
            stmt.setString(17, "Test Brand");
            stmt.setString(18, "Test Model");
            stmt.setString(19, "Test Color");
            stmt.setString(20, "Test Serial");
            stmt.setString(21, "Test Storage");
            stmt.setString(22, "test.jpg");
            
            int rowsAffected = stmt.executeUpdate();
            assertEquals(1, rowsAffected);
        }
        
        // Retrieve the record
        String selectQuery = "SELECT * FROM records WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
            stmt.setInt(1, 99999);
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(99999, rs.getInt("id"));
                assertEquals(12345, rs.getInt("officer_id"));
                assertEquals("Test", rs.getString("founder_last_name"));
                assertEquals("Record", rs.getString("founder_first_name"));
                assertEquals("Test Item", rs.getString("item_description"));
            }
        }
        
        // Clean up
        String deleteQuery = "DELETE FROM records WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
            stmt.setInt(1, 99999);
            int rowsAffected = stmt.executeUpdate();
            assertEquals(1, rowsAffected);
        }
    }
}
