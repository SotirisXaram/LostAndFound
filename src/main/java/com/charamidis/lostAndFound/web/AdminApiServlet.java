package com.charamidis.lostAndFound.web;

import com.charamidis.lostAndFound.utils.SqliteDatabaseInitializer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminApiServlet extends HttpServlet {
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
        String path = request.getPathInfo();
        PrintWriter out = response.getWriter();
        
        try {
            switch (path) {
                case "/stats":
                    out.println(getStats());
                    break;
                case "/recent-records":
                    out.println(getRecentRecords());
                    break;
                case "/user-activity":
                    out.println(getUserActivity());
                    break;
                case "/system-status":
                    out.println(getSystemStatus());
                    break;
                case "/records":
                    out.println(getAllRecords());
                    break;
                case "/returns":
                    out.println(getAllReturns());
                    break;
                case "/activity":
                    out.println(getAllActivity());
                    break;
                case "/recent-activity":
                    out.println(getRecentActivity());
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.println("{\"error\": \"Endpoint not found\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
        String path = request.getPathInfo();
        PrintWriter out = response.getWriter();
        
        try {
            switch (path) {
                case "/clear-logs":
                    clearActivityLogs();
                    out.println("{\"success\": true, \"message\": \"Logs cleared successfully\"}");
                    break;
                case "/log-activity":
                    logActivity(request);
                    out.println("{\"success\": true, \"message\": \"Activity logged successfully\"}");
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.println("{\"error\": \"Endpoint not found\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private String getStats() throws SQLException {
        JsonObject stats = new JsonObject();
        
        try (Connection conn = SqliteDatabaseInitializer.getConnection()) {
            // Total records
            try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM records")) {
                ResultSet rs = stmt.executeQuery();
                stats.addProperty("totalRecords", rs.getInt(1));
            }
            
            // Records by status
            try (PreparedStatement stmt = conn.prepareStatement("SELECT 'stored' as status, COUNT(*) FROM records")) {
                ResultSet rs = stmt.executeQuery();
                JsonObject statusCounts = new JsonObject();
                if (rs.next()) {
                    statusCounts.addProperty("stored", rs.getInt(1));
                }
                stats.add("recordsByStatus", statusCounts);
            }
            
            // Records this month
            try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM records WHERE strftime('%Y-%m', found_date) = strftime('%Y-%m', 'now')")) {
                ResultSet rs = stmt.executeQuery();
                stats.addProperty("recordsThisMonth", rs.getInt(1));
            }
            
            // Total returns
            try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM returns")) {
                ResultSet rs = stmt.executeQuery();
                stats.addProperty("totalReturns", rs.getInt(1));
            }
            
            // Returns this month
            try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM returns WHERE strftime('%Y-%m', return_date) = strftime('%Y-%m', 'now')")) {
                ResultSet rs = stmt.executeQuery();
                stats.addProperty("returnsThisMonth", rs.getInt(1));
            }
            
            // Total users
            try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users")) {
                ResultSet rs = stmt.executeQuery();
                stats.addProperty("totalUsers", rs.getInt(1));
            }
        }
        
        stats.addProperty("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return gson.toJson(stats);
    }

    private String getAllRecords() throws SQLException {
        List<JsonObject> records = new ArrayList<>();
        
        try (Connection conn = SqliteDatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT id, record_datetime, founder_first_name, founder_last_name, item_description, " +
                 "found_location, found_date, found_time, officer_id, item_category, item_brand, " +
                 "item_model, item_color, item_serial_number, storage_location, picture_path " +
                 "FROM records ORDER BY id DESC")) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JsonObject record = new JsonObject();
                record.addProperty("id", rs.getInt("id"));
                record.addProperty("record_date", rs.getString("record_datetime"));
                record.addProperty("founder_first_name", rs.getString("founder_first_name") != null ? rs.getString("founder_first_name") : "");
                record.addProperty("founder_last_name", rs.getString("founder_last_name") != null ? rs.getString("founder_last_name") : "");
                record.addProperty("item_description", rs.getString("item_description") != null ? rs.getString("item_description") : "");
                record.addProperty("found_location", rs.getString("found_location"));
                record.addProperty("found_date", rs.getString("found_date"));
                record.addProperty("found_time", rs.getString("found_time"));
                record.addProperty("officer_id", rs.getInt("officer_id"));
                record.addProperty("item_category", rs.getString("item_category"));
                record.addProperty("item_brand", rs.getString("item_brand"));
                record.addProperty("item_model", rs.getString("item_model"));
                record.addProperty("item_color", rs.getString("item_color"));
                record.addProperty("item_serial_number", rs.getString("item_serial_number"));
                record.addProperty("storage_location", rs.getString("storage_location"));
                record.addProperty("picture_path", rs.getString("picture_path"));
                records.add(record);
            }
        }
        
        return gson.toJson(records);
    }

    private String getAllReturns() throws SQLException {
        List<JsonObject> returns = new ArrayList<>();
        
        try (Connection conn = SqliteDatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT r.id, r.return_date, r.return_time, r.return_first_name, r.return_last_name, " +
                 "r.return_telephone, r.return_id_number, r.return_father_name, r.return_date_of_birth, " +
                 "r.return_street_address, r.return_street_number, r.return_timestamp, r.comment, " +
                 "r.return_officer, rec.item_description, u.first_name || ' ' || u.last_name as officer_name " +
                 "FROM returns r " +
                 "LEFT JOIN records rec ON r.record_id = rec.id " +
                 "LEFT JOIN users u ON r.return_officer = u.am " +
                 "ORDER BY r.return_timestamp DESC")) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JsonObject returnItem = new JsonObject();
                returnItem.addProperty("id", rs.getInt("id"));
                returnItem.addProperty("return_date", rs.getString("return_date"));
                returnItem.addProperty("return_time", rs.getString("return_time"));
                returnItem.addProperty("claimant_name", rs.getString("return_first_name") + " " + rs.getString("return_last_name"));
                returnItem.addProperty("claimant_id", rs.getString("return_id_number"));
                returnItem.addProperty("claimant_phone", rs.getString("return_telephone"));
                returnItem.addProperty("item_description", rs.getString("item_description") != null ? rs.getString("item_description") : "");
                returnItem.addProperty("officer_name", rs.getString("officer_name"));
                returnItem.addProperty("return_reason", rs.getString("comment"));
                returnItem.addProperty("notes", rs.getString("comment"));
                returnItem.addProperty("return_timestamp", rs.getString("return_timestamp"));
                returns.add(returnItem);
            }
        } catch (SQLException e) {
            // If returns table doesn't exist, return empty array
            if (e.getMessage().contains("no such table")) {
                return gson.toJson(new ArrayList<>());
            }
            throw e;
        }
        
        return gson.toJson(returns);
    }

    private String getAllActivity() throws SQLException {
        List<JsonObject> activities = new ArrayList<>();
        
        try (Connection conn = SqliteDatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT timestamp, user_name, action, details, ip_address " +
                 "FROM activity_log ORDER BY timestamp DESC LIMIT 1000")) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JsonObject activity = new JsonObject();
                activity.addProperty("timestamp", rs.getString("timestamp"));
                activity.addProperty("user", rs.getString("user_name"));
                activity.addProperty("action", rs.getString("action"));
                activity.addProperty("details", rs.getString("details"));
                activity.addProperty("ip_address", rs.getString("ip_address"));
                activities.add(activity);
            }
        } catch (SQLException e) {
            // If activity_log table doesn't exist, return empty array
            if (e.getMessage().contains("no such table")) {
                return gson.toJson(new ArrayList<>());
            }
            throw e;
        }
        
        return gson.toJson(activities);
    }

    private String getRecentActivity() throws SQLException {
        List<JsonObject> activities = new ArrayList<>();
        
        try (Connection conn = SqliteDatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT timestamp, user_name, action, details " +
                 "FROM activity_log ORDER BY timestamp DESC LIMIT 10")) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JsonObject activity = new JsonObject();
                activity.addProperty("timestamp", rs.getString("timestamp"));
                activity.addProperty("action", rs.getString("action"));
                activity.addProperty("details", rs.getString("details"));
                activities.add(activity);
            }
        } catch (SQLException e) {
            // If activity_log table doesn't exist, return empty array
            if (e.getMessage().contains("no such table")) {
                return gson.toJson(new ArrayList<>());
            }
            throw e;
        }
        
        return gson.toJson(activities);
    }

    private String getRecentRecords() throws SQLException {
        List<JsonObject> records = new ArrayList<>();
        
        try (Connection conn = SqliteDatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT id, item_description, found_date, found_time, founder_first_name, founder_last_name " +
                 "FROM records ORDER BY id DESC LIMIT 10")) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JsonObject record = new JsonObject();
                record.addProperty("id", rs.getInt("id"));
                record.addProperty("description", rs.getString("item_description") != null ? rs.getString("item_description") : "");
                record.addProperty("date", rs.getString("found_date"));
                record.addProperty("time", rs.getString("found_time"));
                record.addProperty("founder", rs.getString("founder_first_name") + " " + rs.getString("founder_last_name"));
                records.add(record);
            }
        }
        
        return gson.toJson(records);
    }

    private String getUserActivity() throws SQLException {
        List<JsonObject> activities = new ArrayList<>();
        
        try (Connection conn = SqliteDatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT u.first_name, u.last_name, u.role, COUNT(r.id) as record_count " +
                 "FROM users u LEFT JOIN records r ON u.am = r.officer_id " +
                 "GROUP BY u.am, u.first_name, u.last_name, u.role " +
                 "ORDER BY record_count DESC")) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JsonObject activity = new JsonObject();
                activity.addProperty("name", rs.getString("first_name") + " " + rs.getString("last_name"));
                activity.addProperty("role", rs.getString("role"));
                activity.addProperty("recordCount", rs.getInt("record_count"));
                activities.add(activity);
            }
        }
        
        return gson.toJson(activities);
    }

    private String getSystemStatus() {
        JsonObject status = new JsonObject();
        status.addProperty("webServerEnabled", WebServerManager.isEnabled());
        status.addProperty("webServerPort", WebServerManager.getPort());
        status.addProperty("databaseConnected", isDatabaseConnected());
        status.addProperty("uptime", getUptime());
        status.addProperty("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        return gson.toJson(status);
    }

    private void clearActivityLogs() throws SQLException {
        try (Connection conn = SqliteDatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM activity_log")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            // If activity_log table doesn't exist, create it
            if (e.getMessage().contains("no such table")) {
                createActivityLogTable();
            } else {
                throw e;
            }
        }
    }

    private void logActivity(HttpServletRequest request) throws SQLException {
        String user = request.getParameter("user");
        String action = request.getParameter("action");
        String details = request.getParameter("details");
        String ipAddress = request.getRemoteAddr();
        
        try (Connection conn = SqliteDatabaseInitializer.getConnection()) {
            // Create activity_log table if it doesn't exist
            createActivityLogTable();
            
            try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO activity_log (timestamp, user_name, action, details, ip_address) VALUES (?, ?, ?, ?, ?)")) {
                stmt.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                stmt.setString(2, user != null ? user : "Unknown");
                stmt.setString(3, action != null ? action : "Unknown Action");
                stmt.setString(4, details != null ? details : "");
                stmt.setString(5, ipAddress);
                stmt.executeUpdate();
            }
        }
    }

    private void createActivityLogTable() throws SQLException {
        try (Connection conn = SqliteDatabaseInitializer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "CREATE TABLE IF NOT EXISTS activity_log (" +
                 "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 "timestamp TEXT NOT NULL, " +
                 "user_name TEXT NOT NULL, " +
                 "action TEXT NOT NULL, " +
                 "details TEXT, " +
                 "ip_address TEXT" +
                 ")")) {
            stmt.executeUpdate();
        }
    }

    private boolean isDatabaseConnected() {
        try (Connection conn = SqliteDatabaseInitializer.getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    private String getUptime() {
        // Simple uptime calculation - in a real app, you'd track start time
        return "Running";
    }
}