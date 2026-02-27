package com.charamidis.lostAndFound.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqliteDatabaseInitializer {
    private static final Logger logger = AppLogger.getLogger();
    public static String DB_FILE;
    public static String DB_URL;

    static {
        resetToDefaultPath();
    }

    public static void resetToDefaultPath() {
        File localDb = new File("lostandfound.db");
        if (localDb.exists() && localDb.canWrite()) {
            DB_FILE = localDb.getAbsolutePath();
        } else {
            String dataFolder = ImageManager.getDataFolderPath();
            File dataDir = new File(dataFolder);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            DB_FILE = new File(dataDir, "lostandfound.db").getAbsolutePath();
        }
        DB_URL = "jdbc:sqlite:" + DB_FILE;
    }

    public static void setDbFileForTesting(File file) {
        DB_FILE = file.getAbsolutePath();
        DB_URL = "jdbc:sqlite:" + DB_FILE;
    }
    
    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            logger.info("SQLite database connection established");
            
            // Read and execute the schema file
            String schema = readSchemaFile();
            if (schema != null && !schema.trim().isEmpty()) {
                String[] statements = schema.split(";");
                try (Statement stmt = conn.createStatement()) {
                    for (String statement : statements) {
                        String trimmedStatement = statement.trim();
                        if (!trimmedStatement.isEmpty()) {
                            stmt.execute(trimmedStatement);
                        }
                    }
                }
                logger.info("Database schema initialized successfully");
                
                // Check and add picture_path column if it doesn't exist
                addPicturePathColumnIfNotExists(conn);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error initializing SQLite database", e);
        }
    }
    
    private static String readSchemaFile() {
        try (InputStream inputStream = SqliteDatabaseInitializer.class.getClassLoader()
                .getResourceAsStream("sqlite_schema.sql");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            
            StringBuilder schema = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                schema.append(line).append("\n");
            }
            return schema.toString();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading schema file", e);
            return null;
        }
    }
    
    private static void addPicturePathColumnIfNotExists(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // Check if picture_path column exists
            String checkColumnQuery = "PRAGMA table_info(records)";
            var resultSet = stmt.executeQuery(checkColumnQuery);
            boolean columnExists = false;
            
            while (resultSet.next()) {
                if ("picture_path".equals(resultSet.getString("name"))) {
                    columnExists = true;
                    break;
                }
            }
            resultSet.close();
            
            // Add column if it doesn't exist
            if (!columnExists) {
                String addColumnQuery = "ALTER TABLE records ADD COLUMN picture_path VARCHAR(500)";
                stmt.execute(addColumnQuery);
                logger.info("Added picture_path column to records table");
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error checking/adding picture_path column", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
