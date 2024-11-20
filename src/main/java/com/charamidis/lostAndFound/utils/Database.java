package com.charamidis.lostAndFound.utils;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private static final Logger logger = AppLogger.getLogger();

    public static void backupDatabase(String databasePath) {
        // Check if the database file exists
        File databaseFile = new File(databasePath);
        if (!databaseFile.exists()) {
            // Show an error alert if the database file is missing
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Not Found");
            alert.setHeaderText(null);
            alert.setContentText("The database file '" + databasePath + "' was not found. Please ensure it exists and try again.");
            alert.showAndWait();
            logger.log(Level.SEVERE, "Database file not found: " + databasePath);
            return;
        }

        FileChooser fileChooser = new FileChooser();

        // Set initial directory and extension filters (optional)
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + File.separator + "Desktop"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite Database Files", "*.db"));
        fileChooser.setInitialFileName("backup");

        // Show save dialog
        File file = fileChooser.showSaveDialog(null);

        // If user cancels the dialog, return
        if (file == null) {
            return;
        }

        // Get the chosen file path
        String backupFilePath = file.getAbsolutePath();

        // Perform the backup by copying the SQLite database file
        try (FileInputStream inputStream = new FileInputStream(databaseFile);
             FileOutputStream outputStream = new FileOutputStream(backupFilePath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            // Copy the database file to the backup location
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Backup successful
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Backup Successful");
            alert.setHeaderText(null);
            alert.setContentText("Backup of the SQLite database was successful.");
            alert.showAndWait();
            logger.info("Backup successful: " + backupFilePath);

        } catch (IOException e) {
            // Log and show error message if backup fails
            logger.log(Level.SEVERE, "Failed to backup database", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Backup Failed");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred during the database backup. Please try again.");
            alert.showAndWait();
        }
    }
}
