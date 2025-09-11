package com.charamidis.lostAndFound.pages;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StatisticsDashboard {
    
    private Stage window;
    private Scene scene;
    private Connection connection;
    private TabPane tabPane;
    
    public StatisticsDashboard(Connection conn) {
        this.connection = conn;
        this.window = new Stage();
        this.window.setTitle("Statistics Dashboard");
        this.window.setWidth(1200);
        this.window.setHeight(800);
        this.window.setResizable(true);
        
        createUI();
        this.window.setScene(scene);
        this.window.show();
    }
    
    private void createUI() {
        tabPane = new TabPane();
        
        // Overview Tab
        Tab overviewTab = new Tab("Overview");
        overviewTab.setContent(createOverviewTab());
        
        // Items Statistics Tab
        Tab itemsTab = new Tab("Items Statistics");
        itemsTab.setContent(createItemsTab());
        
        // Returns Statistics Tab
        Tab returnsTab = new Tab("Returns Statistics");
        returnsTab.setContent(createReturnsTab());
        
        // Officers Statistics Tab
        Tab officersTab = new Tab("Officers Statistics");
        officersTab.setContent(createOfficersTab());
        
        // Categories Statistics Tab
        Tab categoriesTab = new Tab("Categories Statistics");
        categoriesTab.setContent(createCategoriesTab());
        
        tabPane.getTabs().addAll(overviewTab, itemsTab, returnsTab, officersTab, categoriesTab);
        
        // Export buttons
        HBox exportButtons = new HBox(10);
        exportButtons.setAlignment(Pos.CENTER);
        exportButtons.setPadding(new Insets(10));
        
        Button exportPdfBtn = new Button("Export to PDF");
        exportPdfBtn.setOnAction(e -> exportToPDF());
        
        Button exportHtmlBtn = new Button("Export to HTML");
        exportHtmlBtn.setOnAction(e -> exportToHTML());
        
        exportButtons.getChildren().addAll(exportPdfBtn, exportHtmlBtn);
        
        VBox mainLayout = new VBox(tabPane, exportButtons);
        mainLayout.setSpacing(10);
        
        scene = new Scene(mainLayout);
    }
    
    private VBox createOverviewTab() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        
        // Key metrics
        GridPane metricsGrid = new GridPane();
        metricsGrid.setHgap(20);
        metricsGrid.setVgap(10);
        
        int totalItems = getTotalItems();
        int totalReturns = getTotalReturns();
        int itemsThisMonth = getItemsThisMonth();
        int returnsThisMonth = getReturnsThisMonth();
        int activeOfficers = getActiveOfficers();
        int pendingReturns = totalItems - totalReturns;
        
        metricsGrid.add(new Label("Total Items Found:"), 0, 0);
        metricsGrid.add(new Label(String.valueOf(totalItems)), 1, 0);
        
        metricsGrid.add(new Label("Total Returns:"), 0, 1);
        metricsGrid.add(new Label(String.valueOf(totalReturns)), 1, 1);
        
        metricsGrid.add(new Label("Pending Returns:"), 0, 2);
        metricsGrid.add(new Label(String.valueOf(pendingReturns)), 1, 2);
        
        metricsGrid.add(new Label("Items This Month:"), 0, 3);
        metricsGrid.add(new Label(String.valueOf(itemsThisMonth)), 1, 3);
        
        metricsGrid.add(new Label("Returns This Month:"), 0, 4);
        metricsGrid.add(new Label(String.valueOf(returnsThisMonth)), 1, 4);
        
        metricsGrid.add(new Label("Active Officers:"), 0, 5);
        metricsGrid.add(new Label(String.valueOf(activeOfficers)), 1, 5);
        
        // Monthly trend chart
        BarChart<String, Number> monthlyChart = createMonthlyTrendChart();
        
        layout.getChildren().addAll(
            new Label("Key Metrics"),
            metricsGrid,
            new Separator(),
            new Label("Monthly Trend"),
            monthlyChart
        );
        
        return layout;
    }
    
    private VBox createItemsTab() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        
        // Items by month chart
        BarChart<String, Number> itemsChart = createItemsByMonthChart();
        
        // Items by category chart
        PieChart categoryChart = createItemsByCategoryChart();
        
        HBox chartsBox = new HBox(20);
        chartsBox.getChildren().addAll(itemsChart, categoryChart);
        
        layout.getChildren().addAll(
            new Label("Items Statistics"),
            chartsBox
        );
        
        return layout;
    }
    
    private VBox createReturnsTab() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        
        // Returns by month chart
        BarChart<String, Number> returnsChart = createReturnsByMonthChart();
        
        // Return rate chart
        LineChart<String, Number> returnRateChart = createReturnRateChart();
        
        HBox chartsBox = new HBox(20);
        chartsBox.getChildren().addAll(returnsChart, returnRateChart);
        
        layout.getChildren().addAll(
            new Label("Returns Statistics"),
            chartsBox
        );
        
        return layout;
    }
    
    private VBox createOfficersTab() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        
        // Officers performance chart
        BarChart<String, Number> officersChart = createOfficersPerformanceChart();
        
        // Officers pie chart
        PieChart officersPieChart = createOfficersPieChart();
        
        HBox chartsBox = new HBox(20);
        chartsBox.getChildren().addAll(officersChart, officersPieChart);
        
        layout.getChildren().addAll(
            new Label("Officers Statistics"),
            chartsBox
        );
        
        return layout;
    }
    
    private VBox createCategoriesTab() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        
        // Categories distribution
        PieChart categoriesChart = createCategoriesDistributionChart();
        
        // Categories by month
        BarChart<String, Number> categoriesMonthlyChart = createCategoriesMonthlyChart();
        
        HBox chartsBox = new HBox(20);
        chartsBox.getChildren().addAll(categoriesChart, categoriesMonthlyChart);
        
        layout.getChildren().addAll(
            new Label("Categories Statistics"),
            chartsBox
        );
        
        return layout;
    }
    
    // Chart creation methods
    private BarChart<String, Number> createMonthlyTrendChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Monthly Trend");
        chart.setAnimated(false);
        
        XYChart.Series<String, Number> itemsSeries = new XYChart.Series<>();
        itemsSeries.setName("Items Found");
        
        XYChart.Series<String, Number> returnsSeries = new XYChart.Series<>();
        returnsSeries.setName("Returns");
        
        try {
            if (connection == null || connection.isClosed()) {
                System.err.println("Database connection is null or closed");
                return chart;
            }
            
            Statement stmt = connection.createStatement();
            String query = "SELECT strftime('%Y-%m', found_date) AS month, COUNT(*) AS count " +
                    "FROM records " +
                    "WHERE found_date >= date('now', '-12 months') " +
                    "GROUP BY strftime('%Y-%m', found_date) " +
                    "ORDER BY month";
            
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String month = rs.getString("month");
                int count = rs.getInt("count");
                itemsSeries.getData().add(new XYChart.Data<>(month, count));
            }
            
            String returnQuery = "SELECT strftime('%Y-%m', return_date) AS month, COUNT(*) AS count " +
                    "FROM returns " +
                    "WHERE return_date >= date('now', '-12 months') " +
                    "GROUP BY strftime('%Y-%m', return_date) " +
                    "ORDER BY month";
            
            rs = stmt.executeQuery(returnQuery);
            while (rs.next()) {
                String month = rs.getString("month");
                int count = rs.getInt("count");
                returnsSeries.getData().add(new XYChart.Data<>(month, count));
            }
            
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error in createMonthlyTrendChart: " + e.getMessage());
            e.printStackTrace();
        }
        
        chart.getData().addAll(itemsSeries, returnsSeries);
        return chart;
    }
    
    private BarChart<String, Number> createItemsByMonthChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Items Found by Month");
        chart.setAnimated(false);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Items");
        
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT strftime('%Y-%m', found_date) AS month, COUNT(*) AS count " +
                    "FROM records " +
                    "WHERE found_date >= date('now', '-12 months') " +
                    "GROUP BY strftime('%Y-%m', found_date) " +
                    "ORDER BY month";
            
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String month = rs.getString("month");
                int count = rs.getInt("count");
                series.getData().add(new XYChart.Data<>(month, count));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        chart.getData().add(series);
        return chart;
    }
    
    private PieChart createItemsByCategoryChart() {
        PieChart chart = new PieChart();
        chart.setTitle("Items by Category");
        chart.setAnimated(false);
        
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT item_category, COUNT(*) AS count " +
                    "FROM records " +
                    "WHERE item_category IS NOT NULL AND item_category != '' " +
                    "GROUP BY item_category " +
                    "ORDER BY count DESC";
            
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String category = rs.getString("item_category");
                int count = rs.getInt("count");
                chart.getData().add(new PieChart.Data(category + " (" + count + ")", count));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return chart;
    }
    
    private BarChart<String, Number> createReturnsByMonthChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Returns by Month");
        chart.setAnimated(false);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Returns");
        
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT strftime('%Y-%m', return_date) AS month, COUNT(*) AS count " +
                    "FROM returns " +
                    "WHERE return_date >= date('now', '-12 months') " +
                    "GROUP BY strftime('%Y-%m', return_date) " +
                    "ORDER BY month";
            
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String month = rs.getString("month");
                int count = rs.getInt("count");
                series.getData().add(new XYChart.Data<>(month, count));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        chart.getData().add(series);
        return chart;
    }
    
    private LineChart<String, Number> createReturnRateChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Return Rate by Month");
        chart.setAnimated(false);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Return Rate %");
        
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT " +
                    "strftime('%Y-%m', r.found_date) AS month, " +
                    "COUNT(r.id) AS total_items, " +
                    "COUNT(ret.id) AS returns, " +
                    "ROUND((COUNT(ret.id) * 100.0 / COUNT(r.id)), 2) AS return_rate " +
                    "FROM records r " +
                    "LEFT JOIN returns ret ON r.id = ret.record_id " +
                    "WHERE r.found_date >= date('now', '-12 months') " +
                    "GROUP BY strftime('%Y-%m', r.found_date) " +
                    "ORDER BY month";
            
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String month = rs.getString("month");
                double returnRate = rs.getDouble("return_rate");
                series.getData().add(new XYChart.Data<>(month, returnRate));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        chart.getData().add(series);
        return chart;
    }
    
    private BarChart<String, Number> createOfficersPerformanceChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Officers Performance (This Month)");
        chart.setAnimated(false);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Items Found");
        
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT (u.last_name || ' ' || u.first_name) AS officer_name, COUNT(*) AS count " +
                    "FROM records r " +
                    "JOIN users u ON r.officer_id = u.am " +
                    "WHERE r.found_date >= date('now', 'start of month') " +
                    "GROUP BY officer_name " +
                    "ORDER BY count DESC " +
                    "LIMIT 10";
            
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String officerName = rs.getString("officer_name");
                int count = rs.getInt("count");
                series.getData().add(new XYChart.Data<>(officerName, count));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        chart.getData().add(series);
        return chart;
    }
    
    private PieChart createOfficersPieChart() {
        PieChart chart = new PieChart();
        chart.setTitle("Officers Distribution (This Month)");
        chart.setAnimated(false);
        
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT (u.last_name || ' ' || u.first_name) AS officer_name, COUNT(*) AS count " +
                    "FROM records r " +
                    "JOIN users u ON r.officer_id = u.am " +
                    "WHERE r.found_date >= date('now', 'start of month') " +
                    "GROUP BY officer_name " +
                    "ORDER BY count DESC " +
                    "LIMIT 8";
            
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String officerName = rs.getString("officer_name");
                int count = rs.getInt("count");
                chart.getData().add(new PieChart.Data(officerName + " (" + count + ")", count));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return chart;
    }
    
    private PieChart createCategoriesDistributionChart() {
        PieChart chart = new PieChart();
        chart.setTitle("Categories Distribution");
        chart.setAnimated(false);
        
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT item_category, COUNT(*) AS count " +
                    "FROM records " +
                    "WHERE item_category IS NOT NULL AND item_category != '' " +
                    "GROUP BY item_category " +
                    "ORDER BY count DESC " +
                    "LIMIT 10";
            
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String category = rs.getString("item_category");
                int count = rs.getInt("count");
                chart.getData().add(new PieChart.Data(category + " (" + count + ")", count));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return chart;
    }
    
    private BarChart<String, Number> createCategoriesMonthlyChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Top Categories by Month");
        chart.setAnimated(false);
        
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT item_category, strftime('%Y-%m', found_date) AS month, COUNT(*) AS count " +
                    "FROM records " +
                    "WHERE item_category IS NOT NULL AND item_category != '' " +
                    "AND found_date >= date('now', '-6 months') " +
                    "GROUP BY item_category, strftime('%Y-%m', found_date) " +
                    "ORDER BY month, count DESC";
            
            Map<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                String category = rs.getString("item_category");
                String month = rs.getString("month");
                int count = rs.getInt("count");
                
                if (!seriesMap.containsKey(category)) {
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName(category);
                    seriesMap.put(category, series);
                }
                
                seriesMap.get(category).getData().add(new XYChart.Data<>(month, count));
            }
            
            chart.getData().addAll(seriesMap.values());
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return chart;
    }
    
    // Helper methods for metrics
    private int getTotalItems() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM records");
            int count = rs.getInt(1);
            stmt.close();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    private int getTotalReturns() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM returns");
            int count = rs.getInt(1);
            stmt.close();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    private int getItemsThisMonth() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM records WHERE found_date >= date('now', 'start of month')");
            int count = rs.getInt(1);
            stmt.close();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    private int getReturnsThisMonth() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM returns WHERE return_date >= date('now', 'start of month')");
            int count = rs.getInt(1);
            stmt.close();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    private int getActiveOfficers() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(DISTINCT officer_id) FROM records WHERE found_date >= date('now', '-30 days')");
            int count = rs.getInt(1);
            stmt.close();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    private void exportToPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(window);
        
        if (file != null) {
            try {
                String htmlContent = generateHTMLReport();
                ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
                FileOutputStream outputStream = new FileOutputStream(file);
                HtmlConverter.convertToPdf(inputStream, outputStream);
                outputStream.close();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Export Successful");
                alert.setHeaderText("PDF Report Generated");
                alert.setContentText("The statistics report has been saved to: " + file.getAbsolutePath());
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Export Failed");
                alert.setHeaderText("Error generating PDF");
                alert.setContentText("An error occurred while generating the PDF: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
    
    private void exportToHTML() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save HTML Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML Files", "*.html"));
        File file = fileChooser.showSaveDialog(window);
        
        if (file != null) {
            try {
                String htmlContent = generateHTMLReport();
                FileWriter writer = new FileWriter(file);
                writer.write(htmlContent);
                writer.close();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Export Successful");
                alert.setHeaderText("HTML Report Generated");
                alert.setContentText("The statistics report has been saved to: " + file.getAbsolutePath());
                alert.showAndWait();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Export Failed");
                alert.setHeaderText("Error generating HTML");
                alert.setContentText("An error occurred while generating the HTML: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
    
    private String generateHTMLReport() {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<title>Lost & Found Statistics Report</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        html.append("h1, h2 { color: #333; }");
        html.append("table { border-collapse: collapse; width: 100%; margin: 20px 0; }");
        html.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        html.append("th { background-color: #f2f2f2; }");
        html.append(".metric { display: inline-block; margin: 10px; padding: 15px; background-color: #f9f9f9; border-radius: 5px; }");
        html.append("</style>");
        html.append("</head><body>");
        
        html.append("<h1>Lost & Found Statistics Report</h1>");
        html.append("<p>Generated on: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</p>");
        
        // Key metrics
        html.append("<h2>Key Metrics</h2>");
        html.append("<div class='metric'><strong>Total Items Found:</strong> " + getTotalItems() + "</div>");
        html.append("<div class='metric'><strong>Total Returns:</strong> " + getTotalReturns() + "</div>");
        html.append("<div class='metric'><strong>Pending Returns:</strong> " + (getTotalItems() - getTotalReturns()) + "</div>");
        html.append("<div class='metric'><strong>Items This Month:</strong> " + getItemsThisMonth() + "</div>");
        html.append("<div class='metric'><strong>Returns This Month:</strong> " + getReturnsThisMonth() + "</div>");
        html.append("<div class='metric'><strong>Active Officers:</strong> " + getActiveOfficers() + "</div>");
        
        // Monthly statistics
        html.append("<h2>Monthly Statistics</h2>");
        html.append(generateMonthlyStatsTable());
        
        // Categories statistics
        html.append("<h2>Categories Statistics</h2>");
        html.append(generateCategoriesStatsTable());
        
        // Officers statistics
        html.append("<h2>Officers Statistics</h2>");
        html.append(generateOfficersStatsTable());
        
        html.append("</body></html>");
        
        return html.toString();
    }
    
    private String generateMonthlyStatsTable() {
        StringBuilder table = new StringBuilder();
        table.append("<table><tr><th>Month</th><th>Items Found</th><th>Returns</th><th>Return Rate</th></tr>");
        
        try {
            if (connection == null || connection.isClosed()) {
                System.err.println("Database connection is null or closed in generateMonthlyStatsTable");
                table.append("<tr><td colspan='4'>Database connection error</td></tr>");
                table.append("</table>");
                return table.toString();
            }
            
            Statement stmt = connection.createStatement();
            String query = "SELECT " +
                    "strftime('%Y-%m', r.found_date) AS month, " +
                    "COUNT(r.id) AS total_items, " +
                    "COUNT(ret.id) AS returns, " +
                    "ROUND((COUNT(ret.id) * 100.0 / COUNT(r.id)), 2) AS return_rate " +
                    "FROM records r " +
                    "LEFT JOIN returns ret ON r.id = ret.record_id " +
                    "WHERE r.found_date >= date('now', '-12 months') " +
                    "GROUP BY strftime('%Y-%m', r.found_date) " +
                    "ORDER BY month";
            
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String month = rs.getString("month");
                int totalItems = rs.getInt("total_items");
                int returns = rs.getInt("returns");
                double returnRate = rs.getDouble("return_rate");
                
                table.append("<tr>");
                table.append("<td>").append(month).append("</td>");
                table.append("<td>").append(totalItems).append("</td>");
                table.append("<td>").append(returns).append("</td>");
                table.append("<td>").append(returnRate).append("%</td>");
                table.append("</tr>");
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error in generateMonthlyStatsTable: " + e.getMessage());
            e.printStackTrace();
            table.append("<tr><td colspan='4'>Error: " + e.getMessage() + "</td></tr>");
        }
        
        table.append("</table>");
        return table.toString();
    }
    
    private String generateCategoriesStatsTable() {
        StringBuilder table = new StringBuilder();
        table.append("<table><tr><th>Category</th><th>Count</th><th>Percentage</th></tr>");
        
        int totalItems = getTotalItems();
        
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT item_category, COUNT(*) AS count " +
                    "FROM records " +
                    "WHERE item_category IS NOT NULL AND item_category != '' " +
                    "GROUP BY item_category " +
                    "ORDER BY count DESC";
            
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String category = rs.getString("item_category");
                int count = rs.getInt("count");
                double percentage = totalItems > 0 ? (count * 100.0 / totalItems) : 0;
                
                table.append("<tr>");
                table.append("<td>").append(category).append("</td>");
                table.append("<td>").append(count).append("</td>");
                table.append("<td>").append(String.format("%.2f", percentage)).append("%</td>");
                table.append("</tr>");
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        table.append("</table>");
        return table.toString();
    }
    
    private String generateOfficersStatsTable() {
        StringBuilder table = new StringBuilder();
        table.append("<table><tr><th>Officer</th><th>Items This Month</th><th>Total Items</th></tr>");
        
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT " +
                    "(u.last_name || ' ' || u.first_name) AS officer_name, " +
                    "COUNT(CASE WHEN r.found_date >= date('now', 'start of month') THEN 1 END) AS this_month, " +
                    "COUNT(r.id) AS total " +
                    "FROM records r " +
                    "JOIN users u ON r.officer_id = u.am " +
                    "GROUP BY officer_name " +
                    "ORDER BY this_month DESC";
            
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String officerName = rs.getString("officer_name");
                int thisMonth = rs.getInt("this_month");
                int total = rs.getInt("total");
                
                table.append("<tr>");
                table.append("<td>").append(officerName).append("</td>");
                table.append("<td>").append(thisMonth).append("</td>");
                table.append("<td>").append(total).append("</td>");
                table.append("</tr>");
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        table.append("</table>");
        return table.toString();
    }
}
