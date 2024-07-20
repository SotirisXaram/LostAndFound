package com.charamidis.lostAndFound.exporters;

import com.charamidis.lostAndFound.utils.AppLogger;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.application.Platform;
import javafx.stage.FileChooser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelExporter {
    private final ExecutorService es = Executors.newSingleThreadExecutor();
    private final Connection finalConn;
    private static final Logger logger = Logger.getLogger(AppLogger.class.getName());

    public ExcelExporter(Connection conn) {
        this.finalConn = conn;
        es.submit(this::exportDataToExcel);
    }

    public void exportDataToExcel() {
        String query = "SELECT * FROM records";

        try (Statement stmt = finalConn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            List<List<String>> data = new ArrayList<>();
            List<String> columnNames = new ArrayList<>();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Extract column names
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            // Extract data from ResultSet
            while (rs.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            Platform.runLater(() -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Excel File");
                fileChooser.setInitialFileName("Records_data-" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".xlsx");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

                java.io.File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    es.submit(() -> writeExcelFile(columnNames, data, file));
                }
            });

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error executing SQL query", e);
        }
    }

    private void writeExcelFile(List<String> columnNames, List<List<String>> data, java.io.File file) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(file)) {

            XSSFSheet sheet = workbook.createSheet("Records");

            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnNames.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnNames.get(i));
            }

            // Create data rows
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);
                List<String> rowData = data.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(rowData.get(j));
                }
            }

            for (int i = 0; i < columnNames.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(fos);

            Platform.runLater(() ->{
                new MessageBoxOk("Data exported to Excel file: " + file.getAbsolutePath());
                es.shutdown();
            });

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing to Excel file", e);
        }
    }

    public void shutdown() {
        es.shutdown();

    }
}