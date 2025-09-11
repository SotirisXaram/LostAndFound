package com.charamidis.lostAndFound.utils;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private static final Logger logger = AppLogger.getLogger();

    public static void backupDatabase(String databaseName) {
        try {
            FileChooser fileChooser = new FileChooser();

            // Set initial directory and extension filters
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")+ File.separator + "Desktop"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite Database Files", "*.db"));
            fileChooser.setInitialFileName("lostandfound_backup");

            // Show save dialog
            File destinationFile = fileChooser.showSaveDialog(null);

            // If user cancels the dialog, return
            if (destinationFile == null) {
                return;
            }

            // Source database file
            File sourceFile = new File("lostandfound.db");
            
            if (!sourceFile.exists()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Backup Error");
                alert.setHeaderText(null);
                alert.setContentText("Database file 'lostandfound.db' not found!");
                alert.showAndWait();
                return;
            }

            // Copy the database file
            try (java.io.FileInputStream fis = new java.io.FileInputStream(sourceFile);
                 java.io.FileOutputStream fos = new java.io.FileOutputStream(destinationFile)) {
                
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            }

            // Backup successful
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Backup Successful");
            alert.setHeaderText(null);
            alert.setContentText("Database backup created successfully at:\n" + destinationFile.getAbsolutePath());
            alert.showAndWait();
            logger.info("SQLite database backup successful: " + destinationFile.getAbsolutePath());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during database backup", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Backup Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred during backup: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
