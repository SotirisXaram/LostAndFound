package com.charamidis.lostAndFound.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoBackupManager {
    private static final Logger logger = AppLogger.getLogger();
    private static Timer backupTimer;
    private static int backupIntervalHours = 1; // Default 1 hour
    
    public static void startAutoBackup() {
        if (backupTimer != null) {
            backupTimer.cancel();
        }
        
        backupTimer = new Timer("AutoBackup", true);
        long intervalMs = backupIntervalHours * 60 * 60 * 1000L; // Convert hours to milliseconds
        
        backupTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                performBackup();
            }
        }, intervalMs, intervalMs); // Start after first interval, then repeat
        
        logger.info("Auto backup started with interval: " + backupIntervalHours + " hours");
    }
    
    public static void stopAutoBackup() {
        if (backupTimer != null) {
            backupTimer.cancel();
            backupTimer = null;
            logger.info("Auto backup stopped");
        }
    }
    
    public static void setBackupInterval(int hours) {
        if (hours < 1) {
            hours = 1; // Minimum 1 hour
        }
        backupIntervalHours = hours;
        logger.info("Backup interval set to: " + hours + " hours");
        
        // Restart timer with new interval
        if (backupTimer != null) {
            startAutoBackup();
        }
    }
    
    public static int getBackupInterval() {
        return backupIntervalHours;
    }
    
    private static void performBackup() {
        try {
            File sourceDb = new File("lostandfound.db");
            if (!sourceDb.exists()) {
                logger.warning("Database file not found for backup");
                return;
            }
            
            // Create backup directory if it doesn't exist
            File backupDir = new File("backups");
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }
            
            // Generate backup filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File backupFile = new File(backupDir, "lostandfound_auto_" + timestamp + ".db");
            
            // Copy database file
            Files.copy(sourceDb.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            logger.info("Auto backup created: " + backupFile.getName());
            
            // Clean up old backups (keep only last 10)
            cleanupOldBackups(backupDir);
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error performing auto backup", e);
        }
    }
    
    private static void cleanupOldBackups(File backupDir) {
        File[] backupFiles = backupDir.listFiles((dir, name) -> name.startsWith("lostandfound_auto_") && name.endsWith(".db"));
        if (backupFiles != null && backupFiles.length > 10) {
            // Sort by last modified time (oldest first)
            java.util.Arrays.sort(backupFiles, (a, b) -> Long.compare(a.lastModified(), b.lastModified()));
            
            // Delete oldest files, keeping only 10 most recent
            int filesToDelete = backupFiles.length - 10;
            for (int i = 0; i < filesToDelete; i++) {
                if (backupFiles[i].delete()) {
                    logger.info("Deleted old backup: " + backupFiles[i].getName());
                }
            }
        }
    }
}
