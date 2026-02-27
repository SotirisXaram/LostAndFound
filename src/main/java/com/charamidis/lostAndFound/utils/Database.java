package com.charamidis.lostAndFound.utils;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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

            // Source database file from the centralized initializer
            File sourceFile = new File(SqliteDatabaseInitializer.DB_FILE);
            
            if (!sourceFile.exists()) {
                logger.warning("Database source file not found at: " + SqliteDatabaseInitializer.DB_FILE);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Σφάλμα αντιγράφου");
                alert.setHeaderText(null);
                alert.setContentText("Το αρχείο της βάσης δεδομένων δεν βρέθηκε!");
                alert.showAndWait();
                return;
            }

            // Copy the database file using modern NIO for atomicity and reliability
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

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
