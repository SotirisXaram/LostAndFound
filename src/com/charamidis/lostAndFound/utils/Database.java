package com.charamidis.lostAndFound.utils;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private static final Logger logger = AppLogger.getLogger();

    public static void backupDatabase(String databaseName) throws IOException, InterruptedException {

        FileChooser fileChooser = new FileChooser();

        // Set initial directory and extension filters (optional)
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")+ File.separator + "Desktop"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQL Files", "*.sql"));
        fileChooser.setInitialFileName("backup.sql");

        // Show save dialog
        File file = fileChooser.showSaveDialog(null);

        // If user cancels the dialog, return
        if (file == null) {

            return;
        }

        // Get the chosen file path
        String filePath = file.getAbsolutePath();

        // Command to execute pg_dump
        String[] cmd = {
                "pg_dump",
                "-U", "sotirisxaram",
                "-d", databaseName,
                "-f", filePath
        };

        // Execute pg_dump command
        ProcessBuilder pb = new ProcessBuilder(cmd);
        Process process = pb.start();

        // Wait for the process to complete
        int exitCode = process.waitFor();

        // Check if the backup was successful
        if (exitCode == 0) {
            // Backup successful
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Backup Successful");
            alert.setHeaderText(null);
            alert.setContentText("Backup of the database 'lostandfound' was successful.");
            alert.showAndWait();
            System.out.println("Backup successful");

        } else {
            logger.log(Level.WARNING,"Backup database fail ");
        }
    }
}
