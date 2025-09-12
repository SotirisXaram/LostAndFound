import java.sql.*;

public class TestDatabase {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:lostandfound.db");
            System.out.println("✅ Database connected successfully!");
            
            // Test records table
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM records");
            if (rs.next()) {
                System.out.println("📊 Records count: " + rs.getInt(1));
            }
            
            // Test a sample record
            rs = stmt.executeQuery("SELECT id, founder_first_name, item_description FROM records LIMIT 1");
            if (rs.next()) {
                System.out.println("📝 Sample record: ID=" + rs.getInt(1) + 
                                 ", Name=" + rs.getString(2) + 
                                 ", Item=" + rs.getString(3));
            }
            
            conn.close();
            System.out.println("✅ Test completed successfully!");
            
        } catch (SQLException e) {
            System.err.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
