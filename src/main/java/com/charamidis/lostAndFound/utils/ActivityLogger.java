package com.charamidis.lostAndFound.utils;

import com.charamidis.lostAndFound.web.WebServerManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class ActivityLogger {
    private static final Logger logger = AppLogger.getLogger();
    
    public static void logActivity(String user, String action, String details) {
        logActivity(user, action, details, null);
    }
    
    public static void logActivity(String user, String action, String details, String ipAddress) {
        try (Connection conn = SqliteDatabaseInitializer.getConnection()) {
            // Create activity_log table if it doesn't exist
            createActivityLogTable(conn);
            
            try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO activity_log (timestamp, user_name, action, details, ip_address) VALUES (?, ?, ?, ?, ?)")) {
                stmt.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                stmt.setString(2, user != null ? user : "System");
                stmt.setString(3, action != null ? action : "Unknown Action");
                stmt.setString(4, details != null ? details : "");
                stmt.setString(5, ipAddress);
                stmt.executeUpdate();
            }
            
            // Broadcast activity to web dashboard
            WebServerManager.broadcastActivity(user, action, details);
            
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error logging activity", e);
        }
    }
    
    public static void logRecordActivity(String user, String action, String recordId, String recordDescription) {
        String details = String.format("Record #%s: %s", recordId, recordDescription);
        logActivity(user, action, details);
    }
    
    public static void logUserActivity(String user, String action, String details) {
        logActivity(user, action, details);
    }
    
    public static void logSystemActivity(String action, String details) {
        logActivity("System", action, details);
    }
    
    public static void logLoginActivity(String user, String ipAddress) {
        logActivity(user, "LOGIN", "User logged in", ipAddress);
    }
    
    public static void logLogoutActivity(String user) {
        logActivity(user, "LOGOUT", "User logged out");
    }
    
    public static void logPasswordChangeActivity(String user) {
        logActivity(user, "PASSWORD_CHANGE", "User changed password");
    }
    
    public static void logRecordAddActivity(String user, String recordId, String recordDescription) {
        logRecordActivity(user, "RECORD_ADD", recordId, recordDescription);
    }
    
    public static void logRecordEditActivity(String user, String recordId, String recordDescription) {
        logRecordActivity(user, "RECORD_EDIT", recordId, recordDescription);
    }
    
    public static void logRecordDeleteActivity(String user, String recordId, String recordDescription) {
        logRecordActivity(user, "RECORD_DELETE", recordId, recordDescription);
    }
    
    public static void logReturnActivity(String user, String recordId, String recordDescription) {
        logRecordActivity(user, "RECORD_RETURN", recordId, recordDescription);
    }
    
    public static void logBackupActivity(String user, String backupType) {
        logActivity(user, "BACKUP_" + backupType.toUpperCase(), "Backup created: " + backupType);
    }
    
    public static void logExportActivity(String user, String exportType) {
        logActivity(user, "EXPORT_" + exportType.toUpperCase(), "Data exported: " + exportType);
    }
    
    public static void logSettingsChangeActivity(String user, String setting, String oldValue, String newValue) {
        String details = String.format("Setting '%s' changed from '%s' to '%s'", setting, oldValue, newValue);
        logActivity(user, "SETTINGS_CHANGE", details);
    }
    
    private static void createActivityLogTable(Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
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
}
