package com.charamidis.lostAndFound.utils;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class TestLogin {
    public static void main(String[] args) {
        try {
            // Connect to database
            Connection conn = SqliteDatabaseInitializer.getConnection();
            
            // Test admin login
            String username = "287874";
            String password = "287874Sotiris!";
            
            System.out.println("Testing login for user: " + username);
            
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM users WHERE am=?");
            stm.setInt(1, Integer.parseInt(username));
            ResultSet resultSet = stm.executeQuery();
            
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                System.out.println("Found user: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
                System.out.println("Stored hash: " + hashedPassword);
                
                if (BCrypt.checkpw(password, hashedPassword)) {
                    System.out.println("✅ Login successful!");
                } else {
                    System.out.println("❌ Password incorrect!");
                }
            } else {
                System.out.println("❌ User not found!");
            }
            
            resultSet.close();
            stm.close();
            conn.close();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
