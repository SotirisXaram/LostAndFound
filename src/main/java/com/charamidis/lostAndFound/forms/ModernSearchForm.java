package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.models.Record;
import com.charamidis.lostAndFound.models.User;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ModernSearchForm extends Stage {
    
    private Connection connection;
    private TableView<Record> tableView;
    private ObservableList<Record> recordList;
    private FilteredList<Record> filteredList;
    
    public ModernSearchForm(Connection connection) {
        this.connection = connection;
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.DECORATED);
        setTitle("Αναζήτηση Εγγραφών");
        
        createUI();
        loadRecords();
        
        // Set window properties
        setMinWidth(1000);
        setMinHeight(600);
        setWidth(1200);
        setHeight(700);
        
        // Center on screen
        centerOnScreen();
    }
    
    private void createUI() {
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #f8f9fa;");
        
        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        Label titleLabel = new Label("Αναζήτηση Εγγραφών");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.rgb(52, 73, 94));
        
        header.getChildren().add(titleLabel);
        
        // Search Panel
        VBox searchPanel = createSearchPanel();
        
        // Results Panel
        VBox resultsPanel = createResultsPanel();
        
        // Buttons Panel
        HBox buttonsPanel = createButtonsPanel();
        
        mainContainer.getChildren().addAll(header, searchPanel, resultsPanel, buttonsPanel);
        
        Scene scene = new Scene(mainContainer);
        setScene(scene);
        show();
    }
    
    private VBox createSearchPanel() {
        VBox searchPanel = new VBox(15);
        searchPanel.setPadding(new Insets(20));
        searchPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        Label searchLabel = new Label("Κριτήρια Αναζήτησης");
        searchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        searchLabel.setTextFill(Color.rgb(52, 73, 94));
        
        // Search fields in a grid
        GridPane searchGrid = new GridPane();
        searchGrid.setHgap(15);
        searchGrid.setVgap(10);
        searchGrid.setPadding(new Insets(10));
        
        // Row 1
        TextField searchField = new TextField();
        searchField.setPromptText("Αναζήτηση σε όλα τα πεδία...");
        searchField.setPrefWidth(300);
        
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.setPromptText("Κατηγορία");
        categoryCombo.getItems().addAll("", "Ταυτότητα", "Διαβατήριο", "Τηλέφωνο", "Πορτοφόλι", "Κλειδιά", "Άλλο");
        categoryCombo.setPrefWidth(150);
        
        // Status combo removed - not available in Record model
        
        searchGrid.add(new Label("Αναζήτηση:"), 0, 0);
        searchGrid.add(searchField, 1, 0);
        searchGrid.add(new Label("Κατηγορία:"), 2, 0);
        searchGrid.add(categoryCombo, 3, 0);
        
        // Row 2
        DatePicker fromDatePicker = new DatePicker();
        fromDatePicker.setPromptText("Από ημερομηνία");
        fromDatePicker.setPrefWidth(150);
        
        DatePicker toDatePicker = new DatePicker();
        toDatePicker.setPromptText("Έως ημερομηνία");
        toDatePicker.setPrefWidth(150);
        
        TextField locationField = new TextField();
        locationField.setPromptText("Τοποθεσία");
        locationField.setPrefWidth(200);
        
        searchGrid.add(new Label("Από:"), 0, 1);
        searchGrid.add(fromDatePicker, 1, 1);
        searchGrid.add(new Label("Έως:"), 2, 1);
        searchGrid.add(toDatePicker, 3, 1);
        searchGrid.add(new Label("Τοποθεσία:"), 4, 1);
        searchGrid.add(locationField, 5, 1);
        
        // Search and Clear buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        
        Button searchBtn = new Button("Αναζήτηση");
        searchBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8 16;");
        searchBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        Button clearBtn = new Button("Καθαρισμός");
        clearBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8 16;");
        clearBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        buttonBox.getChildren().addAll(clearBtn, searchBtn);
        
        // Set up search functionality
        searchBtn.setOnAction(e -> performSearch(searchField.getText(), categoryCombo.getValue(), 
                fromDatePicker.getValue(), toDatePicker.getValue(), locationField.getText()));
        
        clearBtn.setOnAction(e -> {
            searchField.clear();
            categoryCombo.setValue(null);
            fromDatePicker.setValue(null);
            toDatePicker.setValue(null);
            locationField.clear();
            filteredList.setPredicate(null);
        });
        
        searchPanel.getChildren().addAll(searchLabel, searchGrid, buttonBox);
        return searchPanel;
    }
    
    private VBox createResultsPanel() {
        VBox resultsPanel = new VBox(10);
        resultsPanel.setPadding(new Insets(20));
        resultsPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        Label resultsLabel = new Label("Αποτελέσματα Αναζήτησης");
        resultsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        resultsLabel.setTextFill(Color.rgb(52, 73, 94));
        
        // Create table
        tableView = new TableView<>();
        tableView.setPrefHeight(300);
        
        // Create columns
        TableColumn<Record, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(60);
        
        TableColumn<Record, String> dateCol = new TableColumn<>("Ημερομηνία");
        dateCol.setCellValueFactory(cellData -> {
            Record record = cellData.getValue();
            if (record.getFound_date() != null) {
                return new javafx.beans.property.SimpleStringProperty(record.getFound_date().toString());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        dateCol.setPrefWidth(100);
        
        TableColumn<Record, String> timeCol = new TableColumn<>("Ώρα");
        timeCol.setCellValueFactory(cellData -> {
            Record record = cellData.getValue();
            if (record.getFound_time() != null) {
                String timeStr = record.getFound_time().toString();
                if (timeStr.length() > 5) {
                    timeStr = timeStr.substring(0, 5); // Take only HH:mm part
                }
                return new javafx.beans.property.SimpleStringProperty(timeStr);
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        timeCol.setPrefWidth(80);
        
        TableColumn<Record, String> descriptionCol = new TableColumn<>("Περιγραφή");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("item_description"));
        descriptionCol.setPrefWidth(200);
        
        TableColumn<Record, String> categoryCol = new TableColumn<>("Κατηγορία");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("item_category"));
        categoryCol.setPrefWidth(120);
        
        TableColumn<Record, String> locationCol = new TableColumn<>("Τοποθεσία");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("found_location"));
        locationCol.setPrefWidth(150);
        
        // Status column removed - not available in Record model
        
        TableColumn<Record, String> founderCol = new TableColumn<>("Αναζητών");
        founderCol.setCellValueFactory(cellData -> {
            Record record = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                record.getFounder_first_name() + " " + record.getFounder_last_name()
            );
        });
        founderCol.setPrefWidth(150);
        
        tableView.getColumns().addAll(idCol, dateCol, timeCol, descriptionCol, categoryCol, locationCol, founderCol);
        
        // Initialize data
        recordList = FXCollections.observableArrayList();
        filteredList = new FilteredList<>(recordList);
        SortedList<Record> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
        
        // Add double-click to edit
        tableView.setRowFactory(tv -> {
            TableRow<Record> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Record record = row.getItem();
                    editRecord(record);
                }
            });
            return row;
        });
        
        resultsPanel.getChildren().addAll(resultsLabel, tableView);
        return resultsPanel;
    }
    
    private HBox createButtonsPanel() {
        HBox buttonsPanel = new HBox(15);
        buttonsPanel.setAlignment(Pos.CENTER_RIGHT);
        buttonsPanel.setPadding(new Insets(20, 0, 0, 0));
        
        Button editBtn = new Button("Επεξεργασία");
        editBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8 16;");
        editBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        editBtn.setOnAction(e -> {
            Record selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                editRecord(selected);
            } else {
                new MessageBoxOk("Παρακαλώ επιλέξτε μια εγγραφή για επεξεργασία.");
            }
        });
        
        Button printBtn = new Button("Εκτύπωση");
        printBtn.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8 16;");
        printBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        printBtn.setOnAction(e -> {
            Record selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // TODO: Implement print functionality
                new MessageBoxOk("Εκτύπωση εγγραφής: " + selected.getId());
            } else {
                new MessageBoxOk("Παρακαλώ επιλέξτε μια εγγραφή για εκτύπωση.");
            }
        });
        
        Button closeBtn = new Button("Κλείσιμο");
        closeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8 16;");
        closeBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        closeBtn.setOnAction(e -> close());
        
        buttonsPanel.getChildren().addAll(editBtn, printBtn, closeBtn);
        return buttonsPanel;
    }
    
    private void loadRecords() {
        try {
            String query = "SELECT * FROM records ORDER BY id DESC";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            recordList.clear();
            
            while (rs.next()) {
                // Safe date parsing
                String recordDateTime = rs.getString("record_datetime");
                String recordDate = "";
                String recordTime = "";
                
                if (recordDateTime != null && !recordDateTime.isEmpty()) {
                    try {
                        LocalDateTime dateTime;
                        if (recordDateTime.contains("T")) {
                            // Handle microseconds in datetime
                            String cleanDateTime = recordDateTime.substring(0, 19);
                            dateTime = LocalDateTime.parse(cleanDateTime);
                        } else {
                            dateTime = LocalDateTime.parse(recordDateTime);
                        }
                        recordDate = dateTime.toLocalDate().toString();
                        recordTime = dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                    } catch (Exception e) {
                        System.out.println("Error parsing record_datetime: " + recordDateTime + " - " + e.getMessage());
                        LocalDateTime now = LocalDateTime.now();
                        recordDate = now.toLocalDate().toString();
                        recordTime = now.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                    }
                }
                
                Record record = new Record();
                record.setId(rs.getInt("id"));
                
                // Handle date and time conversion
                String foundDateStr = rs.getString("found_date");
                if (foundDateStr != null) {
                    try {
                        record.setFound_date(java.sql.Date.valueOf(foundDateStr));
                    } catch (Exception e) {
                        record.setFound_date(null);
                    }
                }
                
                String foundTimeStr = rs.getString("found_time");
                if (foundTimeStr != null) {
                    try {
                        record.setFound_time(java.sql.Time.valueOf(foundTimeStr));
                    } catch (Exception e) {
                        record.setFound_time(null);
                    }
                }
                
                record.setItem_description(rs.getString("item_description"));
                record.setItem_category(rs.getString("item_category"));
                record.setFound_location(rs.getString("found_location"));
                record.setFounder_first_name(rs.getString("founder_first_name"));
                record.setFounder_last_name(rs.getString("founder_last_name"));
                record.setItem_brand(rs.getString("item_brand"));
                record.setItem_model(rs.getString("item_model"));
                record.setItem_color(rs.getString("item_color"));
                record.setItem_serial_number(rs.getString("item_serial_number"));
                record.setStorage_location(rs.getString("storage_location"));
                
                recordList.add(record);
            }
            
            System.out.println("📊 ModernSearchForm loaded " + recordList.size() + " records");
            stmt.close();
            
            // Refresh the table to ensure data is displayed
            Platform.runLater(() -> {
                tableView.refresh();
            });
        } catch (SQLException e) {
            new MessageBoxOk("Σφάλμα φόρτωσης εγγραφών: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void performSearch(String searchText, String category, 
                             LocalDate fromDate, LocalDate toDate, String location) {
        filteredList.setPredicate(record -> {
            boolean matches = true;
            
            // Text search
            if (searchText != null && !searchText.trim().isEmpty()) {
                String searchLower = searchText.toLowerCase();
                matches = matches && (
                    (record.getItem_description() != null && record.getItem_description().toLowerCase().contains(searchLower)) ||
                    (record.getFounder_first_name() != null && record.getFounder_first_name().toLowerCase().contains(searchLower)) ||
                    (record.getFounder_last_name() != null && record.getFounder_last_name().toLowerCase().contains(searchLower)) ||
                    (record.getItem_brand() != null && record.getItem_brand().toLowerCase().contains(searchLower)) ||
                    (record.getItem_model() != null && record.getItem_model().toLowerCase().contains(searchLower))
                );
            }
            
            // Category filter
            if (category != null && !category.isEmpty()) {
                matches = matches && category.equals(record.getItem_category());
            }
            
            // Date range filter
            if (fromDate != null) {
                try {
                    if (record.getFound_date() != null) {
                        LocalDate recordDate = record.getFound_date().toLocalDate();
                        matches = matches && !recordDate.isBefore(fromDate);
                    }
                } catch (Exception e) {
                    // Skip if date parsing fails
                }
            }
            
            if (toDate != null) {
                try {
                    if (record.getFound_date() != null) {
                        LocalDate recordDate = record.getFound_date().toLocalDate();
                        matches = matches && !recordDate.isAfter(toDate);
                    }
                } catch (Exception e) {
                    // Skip if date parsing fails
                }
            }
            
            // Location filter
            if (location != null && !location.trim().isEmpty()) {
                String locationLower = location.toLowerCase();
                matches = matches && (
                    (record.getFound_location() != null && record.getFound_location().toLowerCase().contains(locationLower)) ||
                    (record.getStorage_location() != null && record.getStorage_location().toLowerCase().contains(locationLower))
                );
            }
            
            return matches;
        });
    }
    
    private void editRecord(Record record) {
        // Open FormRecord in edit mode
        FormRecord formRecord = new FormRecord(connection, null);
        // Note: loadRecordForEdit method needs to be implemented in FormRecord
        new MessageBoxOk("Επεξεργασία εγγραφής ID: " + record.getId() + " - Σε εξέλιξη");
    }
}
