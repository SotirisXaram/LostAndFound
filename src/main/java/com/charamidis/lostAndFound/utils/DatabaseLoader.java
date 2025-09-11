package com.charamidis.lostAndFound.utils;

import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseLoader {
    private static final Logger logger = AppLogger.getLogger();
    
    public static void loadDatabase() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Database File");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SQLite Database", "*.db"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
            );
            
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile == null) {
                return;
            }
            
            if (!selectedFile.exists()) {
                new MessageBoxOk("Selected file does not exist!");
                return;
            }
            
            // Create backup of current database
            File currentDb = new File("lostandfound.db");
            if (currentDb.exists()) {
                File backupFile = new File("lostandfound_backup_" + System.currentTimeMillis() + ".db");
                Files.copy(currentDb.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                logger.info("Backup created: " + backupFile.getName());
            }
            
            // Replace current database with selected file
            Files.copy(selectedFile.toPath(), currentDb.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            new MessageBoxOk("Database loaded successfully!\nApplication will restart to apply changes.");
            logger.info("Database loaded from: " + selectedFile.getAbsolutePath());
            
            // Restart application
            System.exit(0);
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading database", e);
            new MessageBoxOk("Error loading database: " + e.getMessage());
        }
    }
}
