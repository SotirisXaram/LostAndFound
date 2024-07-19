package com.charamidis.lostAndFound.pages;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.geometry.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReturnsLastYear {

    Scene scene;
    Stage window;
    Connection finalConn;
    CategoryAxis xAxis;
    NumberAxis yAxis;
    BarChart<String, Number> barChart;
    XYChart.Series<String, Number> data;

    public ReturnsLastYear(Connection conn) {

        finalConn = conn;
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        barChart = new BarChart<>(xAxis, yAxis);
        data = new XYChart.Series<>();

        VBox vBox = new VBox(barChart);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        barChart.setAnimated(false);

        scene = new Scene(vBox, 850, 650);
        window = new Stage();
        window.setHeight(650);
        window.setWidth(850);
        window.setScene(scene);
        window.setTitle("Returns in the Last Year");

        window.show();

        // Initialize chart with data for the last year
        updateChart();
    }

    private void updateChart() {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastYearDate = currentDate.minusYears(1).plusDays(1); // To include the current month

        // Create a map to store counts for each month, initialized to 0
        Map<String, Integer> monthCounts = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        for (int i = 0; i < 12; i++) {
            LocalDate monthDate = currentDate.minusMonths(i);
            monthCounts.put(monthDate.format(formatter), 0);
        }

        try {
            Statement stm = finalConn.createStatement();
            String query = "SELECT TO_CHAR(return_date, 'YYYY-MM') AS month, COUNT(*) AS count " +
                    "FROM returns " +
                    "WHERE return_date BETWEEN '" + lastYearDate + "' AND '" + currentDate + "' " +
                    "GROUP BY TO_CHAR(return_date, 'YYYY-MM') " +
                    "ORDER BY month;";

            ResultSet resultSet = stm.executeQuery(query);
            while (resultSet.next()) {
                String month = resultSet.getString("month");
                int count = resultSet.getInt("count");
                monthCounts.put(month, count);
            }
            stm.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Update data series with the counts
        data.getData().clear();
        for (Map.Entry<String, Integer> entry : monthCounts.entrySet()) {
            String monthLabel = entry.getKey() + " (" + entry.getValue() + ")";
            data.getData().add(new XYChart.Data<>(monthLabel, entry.getValue()));
        }

        data.setName("Returns in the Last Year");
        if (!barChart.getData().contains(data)) {
            barChart.getData().add(data);
        }
    }
}
