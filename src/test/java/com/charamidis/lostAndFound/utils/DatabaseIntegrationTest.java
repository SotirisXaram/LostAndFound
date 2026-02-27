package com.charamidis.lostAndFound.utils;

import com.charamidis.lostAndFound.models.Record;
import com.charamidis.lostAndFound.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseIntegrationTest {

    @TempDir
    Path tempDir;

    private String dbPath;

    @BeforeEach
    void setUp() {
        // Redirect DB_FILE to temp directory for testing
        dbPath = tempDir.resolve("test_lostandfound.db").toString();
        SqliteDatabaseInitializer.setDbFileForTesting(new File(dbPath));
        SqliteDatabaseInitializer.initializeDatabase();
    }

    @AfterEach
    void tearDown() {
        // Clean up or close connections if necessary
    }

    @Test
    void testTableCreation() throws Exception {
        try (Connection conn = SqliteDatabaseInitializer.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Check if tables exist
            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='records'");
            assertTrue(rs.next(), "Records table should exist");
            
            rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='users'");
            assertTrue(rs.next(), "Users table should exist");
            
            rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='returns'");
            assertTrue(rs.next(), "Returns table should exist");
        }
    }

    @Test
    void testUserCrud() throws Exception {
        try (Connection conn = SqliteDatabaseInitializer.getConnection()) {
            // Insert
            String insert = "INSERT INTO users (am, first_name, last_name, role, password) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pst = conn.prepareStatement(insert)) {
                pst.setInt(1, 999);
                pst.setString(2, "Test");
                pst.setString(3, "User");
                pst.setString(4, "admin");
                pst.setString(5, "hashed_pass");
                pst.executeUpdate();
            }
            
            // Query
            try (PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE am = ?")) {
                pst.setInt(1, 999);
                ResultSet rs = pst.executeQuery();
                assertTrue(rs.next());
                assertEquals("Test", rs.getString("first_name"));
            }
        }
    }
}
