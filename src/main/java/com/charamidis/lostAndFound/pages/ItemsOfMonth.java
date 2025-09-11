package com.charamidis.lostAndFound.pages;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ItemsOfMonth {

    Scene scene;
    Stage window;
    Connection finalConn;
    CategoryAxis xAxis;
    NumberAxis yAxis;
    BarChart<String, Number> barChart;
    XYChart.Series<String, Number> recordData;
    XYChart.Series<String, Number> returnData;

    public ItemsOfMonth(Connection conn) {

        finalConn = conn;
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setAnimated(false);
        recordData = new XYChart.Series<>();
        returnData = new XYChart.Series<>();

        VBox vBox = new VBox(barChart);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 10, 10, 10));

        scene = new Scene(vBox, 850, 650);
        window = new Stage();
        window.setHeight(650);
        window.setWidth(850);
        window.setScene(scene);
        window.setTitle("Records and Returns of Last Month");

        window.show();

        updateChart();
    }

    private void updateChart() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastMonthDate = firstDayOfMonth.minusDays(1); // End of last month
        LocalDate firstDayLastMonth = firstDayOfMonth.minusMonths(1);

        // Clear previous data
        recordData.getData().clear();
        returnData.getData().clear();

        try {
            Statement stm = finalConn.createStatement();

            // Query to get the counts of records for last month
            String recordQuery = "SELECT strftime('%Y-%m-%d', record_datetime) AS day, COUNT(*) AS count " +
                    "FROM records " +
                    "WHERE record_datetime >= '" + firstDayLastMonth + "' AND record_datetime <= '" + lastMonthDate + "' " +
                    "GROUP BY strftime('%d', record_datetime) " +
                    "ORDER BY strftime('%d', record_datetime);";
            ResultSet recordResultSet = stm.executeQuery(recordQuery);
            while (recordResultSet.next()) {
                String day = recordResultSet.getString("day");
                int count = recordResultSet.getInt("count");
                recordData.getData().add(new XYChart.Data<>(day, count));
            }
            recordResultSet.close();

            // Query to get the counts of returns for last month
            String returnQuery = "SELECT strftime('%Y-%m-%d', return_date) AS day, COUNT(*) AS count " +
                    "FROM returns " +
                    "WHERE return_date >= '" + firstDayLastMonth + "' AND return_date <= '" + lastMonthDate + "' " +
                    "GROUP BY strftime('%d', return_date) " +
                    "ORDER BY strftime('%d', return_date);";
            ResultSet returnResultSet = stm.executeQuery(returnQuery);
            while (returnResultSet.next()) {
                String day = returnResultSet.getString("day");
                int count = returnResultSet.getInt("count");
                returnData.getData().add(new XYChart.Data<>(day, count));
            }
            returnResultSet.close();

            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        recordData.setName("Records");
        returnData.setName("Returns");

        barChart.getData().clear();
        barChart.getData().addAll(recordData, returnData);
    }
}
