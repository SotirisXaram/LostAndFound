package com.charamidis.lostAndFound.pages;

import javafx.scene.chart.PieChart;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.geometry.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ItemsPerOfficers {

    Scene scene;
    Stage window;
    Connection finalConn;
    PieChart pieChart;

    public ItemsPerOfficers(Connection conn) {

        finalConn = conn;
        pieChart = new PieChart();

        VBox vBox = new VBox(pieChart);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 10, 10, 10));

        scene = new Scene(vBox, 850, 650);
        window = new Stage();
        window.setHeight(650);
        window.setWidth(850);
        window.setScene(scene);
        window.setTitle("Items Per Officers Last Month");

        window.show();

        // Initialize chart with data for the last month
        updateChart();
    }

    private void updateChart() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);

        Map<String, Integer> officerCounts = new HashMap<>();

        try {
            Statement stm = finalConn.createStatement();
            String query = "SELECT (u.last_name || ' ' || u.first_name) AS officer_name, COUNT(*) AS count " +
                    "FROM records r " +
                    "JOIN users u ON r.officer_id = u.am " +
                    "WHERE r.found_date BETWEEN '" + firstDayOfMonth + "' AND '" + currentDate + "' " +
                    "GROUP BY officer_name " +
                    "ORDER BY count DESC;";

            ResultSet resultSet = stm.executeQuery(query);
            while (resultSet.next()) {
                String officerName = resultSet.getString("officer_name");
                int count = resultSet.getInt("count");
                officerCounts.put(officerName + " (" + count + ")", count);


            }
            stm.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pieChart.setAnimated(false);
        pieChart.getData().clear();
        for (Map.Entry<String, Integer> entry : officerCounts.entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChart.getData().add(slice);
        }
    }
}
