package com.charamidis.lostAndFound.pages;

import com.charamidis.lostAndFound.models.Record;
import com.charamidis.lostAndFound.models.User;
import com.charamidis.lostAndFound.web.WebServerManager;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import com.charamidis.lostAndFound.forms.FormRecord;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import java.sql.Time;
import java.util.function.Predicate;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class RecordsShow {

    Scene scene;
    Stage stage;
    TableView<Record> tv;
    VBox mainContainer;
    Connection connection;
    TextField searchField;
    ComboBox<String> filterComboBox;
    Label statusLabel;
    List<Record> allRecords = new ArrayList<>();

    public RecordsShow(Connection conn) {
        connection = conn;
        createModernUI();
        loadRecords();
        stage.show();
    }

    private void createModernUI() {
        // Create main container with modern styling
        mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef);");
        
        // Create header section
        createHeader();
        
        // Create search and filter section
        createSearchAndFilter();
        
        // Create table section
        createTable();
        
        // Create action buttons section
        createActionButtons();
        
        // Create status bar
        createStatusBar();
        
        // Setup window
        setupWindow();
    }

    private void createHeader() {
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 20, 0));
        
        Label titleLabel = new Label("📋 Records Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        
        // Add a subtle shadow effect
        titleLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 2, 0, 0, 1);");
        
        headerBox.getChildren().add(titleLabel);
        mainContainer.getChildren().add(headerBox);
    }

    private void createSearchAndFilter() {
        HBox searchFilterBox = new HBox(15);
        searchFilterBox.setAlignment(Pos.CENTER_LEFT);
        searchFilterBox.setPadding(new Insets(10));
        searchFilterBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        // Search field
        Label searchLabel = new Label("🔍 Search:");
        searchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        searchLabel.setTextFill(Color.web("#34495e"));
        
        searchField = new TextField();
        searchField.setPromptText("Search records...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #bdc3c7; -fx-padding: 8;");
        
        // Filter combo box
        Label filterLabel = new Label("Filter by:");
        filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        filterLabel.setTextFill(Color.web("#34495e"));
        
        filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll("All Records", "Today", "This Week", "This Month", "With Images", "Without Images");
        filterComboBox.setValue("All Records");
        filterComboBox.setPrefWidth(150);
        filterComboBox.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #bdc3c7;");
        
        // Clear button
        Button clearButton = new Button("Clear");
        clearButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8 16;");
        clearButton.setOnAction(e -> {
            searchField.clear();
            filterComboBox.setValue("All Records");
            applyFilters();
        });
        
        searchFilterBox.getChildren().addAll(searchLabel, searchField, filterLabel, filterComboBox, clearButton);
        mainContainer.getChildren().add(searchFilterBox);
        
        // Add search functionality
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        filterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    private void createTable() {
        // Create table with modern styling
        tv = new TableView<>();
        tv.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        tv.setEditable(false);
        
        // Create columns with better organization
        createTableColumns();
        
        // Enable row selection
        tv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        // Add table to scroll pane for better handling
        ScrollPane scrollPane = new ScrollPane(tv);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background-radius: 10;");
        
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        mainContainer.getChildren().add(scrollPane);
    }

    private void createTableColumns() {
        // ID Column
        TableColumn<Record, Integer> columnId = new TableColumn<>("ID");
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnId.setPrefWidth(60);
        columnId.setStyle("-fx-alignment: CENTER;");
        
        // Record Date & Time (combined)
        TableColumn<Record, String> columnRecordDateTime = new TableColumn<>("Record Date/Time");
        columnRecordDateTime.setCellValueFactory(cellData -> {
            Record record = cellData.getValue();
            String date = record.getRecord_date() != null ? record.getRecord_date() : "N/A";
            String time = record.getRecord_time() != null ? record.getRecord_time() : "N/A";
            return new javafx.beans.property.SimpleStringProperty(date + " " + time);
        });
        columnRecordDateTime.setPrefWidth(140);
        
        // Officer ID
        TableColumn<Record, Integer> columnOfficerId = new TableColumn<>("Officer");
        columnOfficerId.setCellValueFactory(new PropertyValueFactory<>("officer_id"));
        columnOfficerId.setPrefWidth(80);
        columnOfficerId.setStyle("-fx-alignment: CENTER;");
        
        // Founder Name (combined)
        TableColumn<Record, String> columnFounderName = new TableColumn<>("Founder");
        columnFounderName.setCellValueFactory(cellData -> {
            Record record = cellData.getValue();
            String firstName = record.getFounder_first_name() != null ? record.getFounder_first_name() : "";
            String lastName = record.getFounder_last_name() != null ? record.getFounder_last_name() : "";
            return new javafx.beans.property.SimpleStringProperty(firstName + " " + lastName);
        });
        columnFounderName.setPrefWidth(150);
        
        // Contact Info
        TableColumn<Record, String> columnContact = new TableColumn<>("Contact");
        columnContact.setCellValueFactory(cellData -> {
            Record record = cellData.getValue();
            String phone = record.getFounder_telephone() != null ? record.getFounder_telephone() : "N/A";
            return new javafx.beans.property.SimpleStringProperty(phone);
        });
        columnContact.setPrefWidth(120);
        
        // Item Details
        TableColumn<Record, String> columnItemDetails = new TableColumn<>("Item Details");
        columnItemDetails.setCellValueFactory(cellData -> {
            Record record = cellData.getValue();
            String category = record.getItem_category() != null ? record.getItem_category() : "";
            String brand = record.getItem_brand() != null ? record.getItem_brand() : "";
            String model = record.getItem_model() != null ? record.getItem_model() : "";
            return new javafx.beans.property.SimpleStringProperty(category + " " + brand + " " + model);
        });
        columnItemDetails.setPrefWidth(200);
        
        // Description
        TableColumn<Record, String> columnDescription = new TableColumn<>("Description");
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("item_description"));
        columnDescription.setPrefWidth(250);
        columnDescription.setCellFactory(tc -> {
            TableCell<Record, String> cell = new TableCell<Record, String>() {
                private Text text = new Text();
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        text.setText(item);
                        text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(10));
                        setGraphic(text);
                    }
                }
            };
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            return cell;
        });
        
        // Found Location
        TableColumn<Record, String> columnLocation = new TableColumn<>("Found Location");
        columnLocation.setCellValueFactory(new PropertyValueFactory<>("found_location"));
        columnLocation.setPrefWidth(150);
        
        // Found Date
        TableColumn<Record, String> columnFoundDate = new TableColumn<>("Found Date");
        columnFoundDate.setCellValueFactory(cellData -> {
            Record record = cellData.getValue();
            String date = record.getFound_date() != null ? record.getFound_date().toString() : "N/A";
            return new javafx.beans.property.SimpleStringProperty(date);
        });
        columnFoundDate.setPrefWidth(100);
        
        // Storage Location
        TableColumn<Record, String> columnStorage = new TableColumn<>("Storage");
        columnStorage.setCellValueFactory(new PropertyValueFactory<>("storage_location"));
        columnStorage.setPrefWidth(120);
        
        // Image Status
        TableColumn<Record, String> columnImageStatus = new TableColumn<>("Image");
        columnImageStatus.setCellValueFactory(cellData -> {
            Record record = cellData.getValue();
            boolean hasImage = record.getPicture_path() != null && !record.getPicture_path().isEmpty();
            return new javafx.beans.property.SimpleStringProperty(hasImage ? "📷" : "❌");
        });
        columnImageStatus.setPrefWidth(80);
        columnImageStatus.setStyle("-fx-alignment: CENTER;");
        
        // Add columns to table
        tv.getColumns().addAll(columnId, columnRecordDateTime, columnOfficerId, columnFounderName, 
                              columnContact, columnItemDetails, columnDescription, columnLocation, 
                              columnFoundDate, columnStorage, columnImageStatus);
        
        // Make columns resizable
        tv.getColumns().forEach(col -> col.setResizable(true));
    }

    private void createActionButtons() {
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15));
        buttonBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        // Create modern styled buttons
        Button addButton = createStyledButton("➕ Add New", "#27ae60", "#2ecc71");
        Button editButton = createStyledButton("✏️ Edit", "#3498db", "#5dade2");
        Button deleteButton = createStyledButton("🗑️ Delete", "#e74c3c", "#ec7063");
        Button refreshButton = createStyledButton("🔄 Refresh", "#f39c12", "#f7dc6f");
        Button exportButton = createStyledButton("📊 Export", "#9b59b6", "#bb8fce");
        Button viewImageButton = createStyledButton("🖼️ View Image", "#16a085", "#48c9b0");
        
        // Button event handlers
        addButton.setOnAction(e -> openFormRecord(null));
        editButton.setOnAction(e -> {
            Record selected = tv.getSelectionModel().getSelectedItem();
            if (selected != null) {
                openFormRecord(selected);
            } else {
                showAlert("Please select a record to edit.");
            }
        });
        deleteButton.setOnAction(e -> deleteSelectedRecord());
        refreshButton.setOnAction(e -> loadRecords());
        exportButton.setOnAction(e -> showAlert("Export functionality will be implemented soon!"));
        viewImageButton.setOnAction(e -> viewSelectedImage());
        
        buttonBox.getChildren().addAll(addButton, editButton, deleteButton, refreshButton, exportButton, viewImageButton);
        mainContainer.getChildren().add(buttonBox);
    }

    private Button createStyledButton(String text, String color1, String color2) {
        Button button = new Button(text);
        button.setStyle(String.format(
            "-fx-background-color: linear-gradient(to bottom, %s, %s); " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 10 20; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 2);",
            color1, color2
        ));
        
        // Hover effect
        button.setOnMouseEntered(e -> button.setStyle(String.format(
            "-fx-background-color: linear-gradient(to bottom, %s, %s); " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 10 20; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 3);",
            color1, color2
        )));
        
        button.setOnMouseExited(e -> button.setStyle(String.format(
            "-fx-background-color: linear-gradient(to bottom, %s, %s); " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 10 20; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 2);",
            color1, color2
        )));
        
        return button;
    }

    private void createStatusBar() {
        HBox statusBox = new HBox();
        statusBox.setAlignment(Pos.CENTER_LEFT);
        statusBox.setPadding(new Insets(10));
        statusBox.setStyle("-fx-background-color: #34495e; -fx-background-radius: 5;");
        
        statusLabel = new Label("Ready");
        statusLabel.setTextFill(Color.WHITE);
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        statusBox.getChildren().add(statusLabel);
        mainContainer.getChildren().add(statusBox);
    }

    private void setupWindow() {
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();
        
        double windowWidth = Math.min(1400, screenWidth * 0.95);
        double windowHeight = Math.min(900, screenHeight * 0.95);
        
        scene = new Scene(mainContainer, windowWidth, windowHeight);
        stage = new Stage();
        stage.setTitle("📋 Records Management System");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(true);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.setMaxWidth(screenWidth * 0.98);
        stage.setMaxHeight(screenHeight * 0.98);
        
        // Center window
        stage.setX((screenWidth - windowWidth) / 2);
        stage.setY((screenHeight - windowHeight) / 2);
    }

    private void loadRecords() {
        allRecords.clear();
        tv.getItems().clear();
        
        try (Statement stm = connection.createStatement()) {
            ResultSet resultSet = stm.executeQuery("SELECT * FROM records ORDER BY id DESC");
            while (resultSet.next()) {
                Record record = new Record(
                    resultSet.getInt("id"),
                    resultSet.getTimestamp("record_datetime").toLocalDateTime().toLocalDate().toString(),
                    resultSet.getTimestamp("record_datetime").toLocalDateTime().toLocalTime().withSecond(0).toString(),
                    resultSet.getInt("officer_id"),
                    resultSet.getString("founder_last_name"),
                    resultSet.getString("founder_first_name"),
                    resultSet.getString("founder_id_number"),
                    resultSet.getString("founder_telephone"),
                    resultSet.getString("founder_street_address"),
                    resultSet.getString("founder_street_number"),
                    resultSet.getString("founder_father_name"),
                    resultSet.getString("founder_area_inhabitant"),
                    resultSet.getDate("found_date"),
                    resultSet.getTime("found_time"),
                    resultSet.getString("found_location"),
                    resultSet.getString("item_description"),
                    resultSet.getString("item_category"),
                    resultSet.getString("item_brand"),
                    resultSet.getString("item_model"),
                    resultSet.getString("item_color"),
                    resultSet.getString("item_serial_number"),
                    resultSet.getString("storage_location"),
                    resultSet.getString("picture_path")
                );
                allRecords.add(record);
            }
            applyFilters();
            updateStatus("Loaded " + allRecords.size() + " records");
        } catch (SQLException exception) {
            showAlert("Error loading records: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase();
        String filterValue = filterComboBox.getValue();
        
        Predicate<Record> searchPredicate = record -> {
            if (searchText.isEmpty()) return true;
            
            return (record.getFounder_first_name() != null && record.getFounder_first_name().toLowerCase().contains(searchText)) ||
                   (record.getFounder_last_name() != null && record.getFounder_last_name().toLowerCase().contains(searchText)) ||
                   (record.getItem_description() != null && record.getItem_description().toLowerCase().contains(searchText)) ||
                   (record.getFound_location() != null && record.getFound_location().toLowerCase().contains(searchText)) ||
                   (record.getItem_category() != null && record.getItem_category().toLowerCase().contains(searchText)) ||
                   (record.getItem_brand() != null && record.getItem_brand().toLowerCase().contains(searchText));
        };
        
        Predicate<Record> filterPredicate = record -> {
            switch (filterValue) {
                case "Today":
                    return record.getRecord_date() != null && record.getRecord_date().equals(java.time.LocalDate.now().toString());
                case "This Week":
                    // Simple implementation - can be enhanced
                    return true;
                case "This Month":
                    // Simple implementation - can be enhanced
                    return true;
                case "With Images":
                    return record.getPicture_path() != null && !record.getPicture_path().isEmpty();
                case "Without Images":
                    return record.getPicture_path() == null || record.getPicture_path().isEmpty();
                default:
                    return true;
            }
        };
        
        tv.getItems().clear();
        allRecords.stream()
            .filter(searchPredicate.and(filterPredicate))
            .forEach(tv.getItems()::add);
        
        updateStatus("Showing " + tv.getItems().size() + " of " + allRecords.size() + " records");
    }

    private void openFormRecord(Record record) {
        try {
            User adminUser = new User(287874, "Admin", "User", "1990-01-01", "2025-01-01", "00:00:00", "admin");
            new FormRecord(connection, adminUser);
            // Refresh after form closes
            loadRecords();
        } catch (Exception ex) {
            showAlert("Error opening form: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void deleteSelectedRecord() {
        Record selected = tv.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a record to delete.");
            return;
        }
        
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Record #" + selected.getId());
        confirmDialog.setContentText("Are you sure you want to delete this record?\n\nThis action cannot be undone.");
        confirmDialog.getDialogPane().setStyle("-fx-background-color: white;");
        
        if (confirmDialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM records WHERE id = ?")) {
                stmt.setInt(1, selected.getId());
                stmt.executeUpdate();
                
                allRecords.remove(selected);
                tv.getItems().remove(selected);
                updateStatus("Record #" + selected.getId() + " deleted successfully");
                
                // Broadcast to web dashboard
                WebServerManager.broadcastRecordChange("DELETED", 
                    String.valueOf(selected.getId()), 
                    "Record deleted: " + selected.getItem_description());
            } catch (SQLException ex) {
                showAlert("Error deleting record: " + ex.getMessage());
            }
        }
    }

    private void viewSelectedImage() {
        Record selected = tv.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a record to view its image.");
            return;
        }
        
        if (selected.getPicture_path() == null || selected.getPicture_path().isEmpty()) {
            showAlert("This record has no associated image.");
            return;
        }
        
        try {
            Stage imageStage = new Stage();
            imageStage.setTitle("Image for Record #" + selected.getId());
            
            ImageView imageView = new ImageView();
            Image image = new Image("file:" + selected.getPicture_path());
            imageView.setImage(image);
            imageView.setFitWidth(600);
            imageView.setFitHeight(400);
            imageView.setPreserveRatio(true);
            
            ScrollPane scrollPane = new ScrollPane(imageView);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            
            Scene imageScene = new Scene(scrollPane, 650, 450);
            imageStage.setScene(imageScene);
            imageStage.show();
        } catch (Exception ex) {
            showAlert("Error loading image: " + ex.getMessage());
        }
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private void showAlert(String message) {
        new MessageBoxOk(message);
    }
}