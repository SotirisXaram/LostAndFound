package com.charamidis.lostAndFound.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SeedDatabase {
    
    public static void main(String[] args) {
        try {
            // Initialize the database first
            System.out.println("🏗️ Initializing database...");
            SqliteDatabaseInitializer.initializeDatabase();
            
            // Connect to the database
            Connection connection = SqliteDatabaseInitializer.getConnection();
            
            System.out.println("🔗 Connected to database successfully!");
            
            // Seed the database
            DataSeeder.seedDatabase(connection);
            
            // Close connection
            connection.close();
            System.out.println("🔒 Database connection closed.");
            
        } catch (SQLException e) {
            System.err.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
