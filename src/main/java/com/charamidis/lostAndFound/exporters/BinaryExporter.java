package com.charamidis.lostAndFound.exporters;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.charamidis.lostAndFound.utils.AppLogger;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import com.charamidis.lostAndFound.models.Record;
import javafx.stage.FileChooser;

public class BinaryExporter {
    private Connection finalConn;
    private static final Logger logger = AppLogger.getLogger();

    public BinaryExporter(Connection conn) {
        this.finalConn = conn;
        exportDataToBinary();
    }

    public void exportDataToBinary() {
        String query = "SELECT * FROM records";

        try (Statement stmt = finalConn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Create a FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Binary File");
            fileChooser.setInitialFileName("Records_data-" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".lost");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Binary Files", "*.lost"));

            // Show save file dialog
            java.io.File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                // Write data to binary file
                try (FileOutputStream fos = new FileOutputStream(file);
                     ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                    while (rs.next()) {
                        Timestamp timestamp = rs.getTimestamp("record_datetime");
                        LocalDateTime dateTime = timestamp.toLocalDateTime();

                        LocalDate recordDate = dateTime.toLocalDate();
                        LocalTime recordTime = dateTime.toLocalTime();

                        Record record = new Record(
                                rs.getInt("id"),
                                recordDate.toString(),
                                recordTime.toString(),
                                rs.getInt("officer_id"),
                                rs.getString("founder_last_name"),
                                rs.getString("founder_first_name"),
                                rs.getString("founder_id_number"),
                                rs.getString("founder_telephone"),
                                rs.getString("founder_street_address"),
                                rs.getString("founder_street_number"),
                                rs.getString("founder_father_name"),
                                rs.getString("founder_area_inhabitant"),
                                rs.getDate("found_date"),
                                rs.getTime("found_time"),
                                rs.getString("found_location"),
                                rs.getString("item_description"),
                                rs.getString("item_category"),
                                rs.getString("item_brand"),
                                rs.getString("item_model"),
                                rs.getString("item_color"),
                                rs.getString("item_serial_number"),
                                rs.getString("storage_location"),
                                rs.getString("picture_path")
                        );
                        oos.writeObject(record);
                    }

                    System.out.println("Data exported to binary file: " + file.getAbsolutePath());
                    new MessageBoxOk("Data exported to binary file: " + file.getAbsolutePath());

                } catch (IOException e) {
                    logger.log(Level.SEVERE,"error in binary Export io",e);
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE,"error in binary Export with sql",e);

        }
    }
}
