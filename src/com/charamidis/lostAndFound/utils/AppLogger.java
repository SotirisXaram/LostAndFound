package com.charamidis.lostAndFound.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class AppLogger {

    private static final Logger logger = Logger.getLogger(AppLogger.class.getName());

    static {
        try {
            // Define the path to the "exports_data" folder on the desktop
            String userHome = System.getProperty("user.home");
            File exportDir = new File(userHome, "Desktop/export_data/logs");

            // Ensure the directory exists
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            // Set up the file handler to write logs to a file in the specified directory
            FileHandler fileHandler = new FileHandler(new File(exportDir, "app.log").getPath(), true); // true for append mode
            fileHandler.setFormatter(new SimpleFormatter()); // Use simple text format
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL); // Log all levels
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}