package com.charamidis.lostAndFound.exporters;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.charamidis.lostAndFound.utils.AppLogger;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.stage.FileChooser;

public class CsvExporter {
    private Connection finalConn;
    private static final Logger logger = AppLogger.getLogger();

    public CsvExporter(Connection conn) {
        this.finalConn = conn;
        exportDataToCsv();
    }

    public void exportDataToCsv() {
        String query = "SELECT * FROM records";

        try (Statement stmt = finalConn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Create a FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save CSV File");
            fileChooser.setInitialFileName("Records_data-" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".csv");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

            // Show save file dialog
            java.io.File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                // Write data to CSV file
                try (FileWriter writer = new FileWriter(file)) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    // Write header row
                    for (int i = 1; i <= columnCount; i++) {
                        writer.append(metaData.getColumnName(i));
                        if (i < columnCount) {
                            writer.append(",");
                        }
                    }
                    writer.append("\n");

                    // Write data rows
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            String columnType = metaData.getColumnTypeName(i);
                            String value;
                            
                            // Handle different data types properly
                            if (columnType.equals("TIMESTAMP") || columnType.equals("DATETIME")) {
                                Timestamp timestamp = rs.getTimestamp(i);
                                if (timestamp != null) {
                                    value = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                                } else {
                                    value = "";
                                }
                            } else if (columnType.equals("DATE")) {
                                Date date = rs.getDate(i);
                                if (date != null) {
                                    value = date.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                } else {
                                    value = "";
                                }
                            } else if (columnType.equals("TIME")) {
                                Time time = rs.getTime(i);
                                if (time != null) {
                                    value = time.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                                } else {
                                    value = "";
                                }
                            } else {
                                // For other types, use string representation
                                String stringValue = rs.getString(i);
                                value = stringValue != null ? stringValue : "";
                            }
                            
                            writer.append(value);
                            if (i < columnCount) {
                                writer.append(",");
                            }
                        }
                        writer.append("\n");
                    }

                    System.out.println("Data exported to CSV file: " + file.getAbsolutePath());
                    new MessageBoxOk("Data exported to CSV file: " + file.getAbsolutePath());
                } catch (IOException e) {
                  logger.log(Level.SEVERE,"error in cvs export io",e);
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE,"error in cvs export sql",e);

        }
    }
}
