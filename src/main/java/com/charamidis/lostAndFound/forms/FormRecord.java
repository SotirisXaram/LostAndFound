package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.models.Record;
import com.charamidis.lostAndFound.models.User;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import com.charamidis.lostAndFound.utils.ImageManager;
import com.charamidis.lostAndFound.utils.ActivityLogger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class FormRecord extends Stage {
    
    private Connection connection;
    private User currentUser;
    private ObservableList<Record> recordsList;
    private TableView<Record> recordsTable;
    private boolean isEditMode = false;
    private int editingRecordId = -1;
    
    // Form fields
    private TextField idField;
    private TextField officerIdField;
    private TextField founderLastNameField;
    private TextField founderFirstNameField;
    private TextField founderIdNumberField;
    private TextField founderTelephoneField;
    private TextField founderStreetAddressField;
    private TextField founderStreetNumberField;
    private TextField founderFatherNameField;
    private TextField founderAreaInhabitantField;
    private DatePicker foundDatePicker;
    private TextField foundTimeField;
    private TextField foundLocationField;
    private TextArea itemDescriptionField;
    private ComboBox<String> itemCategoryCombo;
    private TextField itemBrandField;
    private TextField itemModelField;
    private TextField itemColorField;
    private TextField itemSerialNumberField;
    private TextArea itemOtherDetailsField;
    private TextField storageLocationField;
    private ComboBox<String> statusCombo;
    
    // Image fields
    private ImageView imageView;
    private Button addImageBtn;
    private Button removeImageBtn;
    private String currentImagePath = null;
    
    // Buttons
    private Button newBtn;
    private Button editBtn;
    private Button deleteBtn;
    private Button saveBtn;
    private Button cancelBtn;
    private Button printBtn;
    private Button returnBtn;
    
    private static final Logger logger = Logger.getLogger(FormRecord.class.getName());
    
    public FormRecord(Connection conn, User user) {
        this.connection = conn;
        this.currentUser = user;
        
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.DECORATED);
        setTitle("Διαχείριση Εγγραφών");
        
        createUI();
        loadRecords();
        
        // Set window properties
        setMinWidth(1200);
        setMinHeight(800);
        setWidth(1400);
        setHeight(900);
        
        // Center on screen
        centerOnScreen();
        show();
    }
    
    private void createUI() {
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #f5f5f5;");
        
        // Header
        HBox header = createHeader();
        
        // Main content area
        HBox contentArea = new HBox(20);
        contentArea.setPrefHeight(600);
        
        // Left side - Form
        VBox formContainer = createFormContainer();
        
        // Right side - Records table
        VBox tableContainer = createTableContainer();
        
        contentArea.getChildren().addAll(formContainer, tableContainer);
        
        // Bottom buttons
        HBox buttonContainer = createButtonContainer();
        
        mainContainer.getChildren().addAll(header, contentArea, buttonContainer);
        
        Scene scene = new Scene(mainContainer);
        setScene(scene);
        
        // Set up keyboard shortcuts
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                cancelAction();
            } else if (e.isControlDown() && e.getCode() == KeyCode.S) {
                saveAction();
            } else if (e.isControlDown() && e.getCode() == KeyCode.N) {
                newAction();
            }
        });
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        Label titleLabel = new Label("Διαχείριση Εγγραφών");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.rgb(52, 73, 94));
        
        header.getChildren().add(titleLabel);
        return header;
    }
    
    private VBox createFormContainer() {
        VBox formContainer = new VBox(15);
        formContainer.setPrefWidth(600);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        // Form title
        Label formTitle = new Label("Στοιχεία Εγγραφής");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        formTitle.setTextFill(Color.rgb(52, 73, 94));
        
        // Scrollable form content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        VBox formContent = new VBox(15);
        formContent.setPadding(new Insets(10));
        
        // Create form sections
        formContent.getChildren().addAll(
            createBasicInfoSection(),
            createFounderInfoSection(),
            createItemInfoSection(),
            createImageSection()
        );
        
        scrollPane.setContent(formContent);
        
        formContainer.getChildren().addAll(formTitle, scrollPane);
        return formContainer;
    }
    
    private VBox createBasicInfoSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
        
        Label sectionTitle = new Label("Βασικές Πληροφορίες");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        sectionTitle.setTextFill(Color.rgb(52, 73, 94));
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        
        // ID field (read-only)
        idField = new TextField();
        idField.setEditable(false);
        idField.setStyle("-fx-background-color: #e9ecef;");
        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        
        // Officer ID field (read-only)
        officerIdField = new TextField();
        officerIdField.setEditable(false);
        officerIdField.setStyle("-fx-background-color: #e9ecef;");
        if (currentUser != null) {
            officerIdField.setText(String.valueOf(currentUser.getAm()));
        }
        grid.add(new Label("ΑΜ Αστυνομικού:"), 0, 1);
        grid.add(officerIdField, 1, 1);
        
        // Found date and time
        foundDatePicker = new DatePicker();
        foundDatePicker.setValue(LocalDate.now().minusDays(1)); // Default to yesterday
        grid.add(new Label("Ημερομηνία Εύρεσης:"), 0, 2);
        grid.add(foundDatePicker, 1, 2);
        
        foundTimeField = new TextField();
        foundTimeField.setText(LocalTime.now().minusMinutes(30).format(DateTimeFormatter.ofPattern("HH:mm")));
        foundTimeField.setPromptText("HH:mm");
        grid.add(new Label("Ώρα Εύρεσης:"), 0, 3);
        grid.add(foundTimeField, 1, 3);
        
        // Found location
        foundLocationField = new TextField();
        foundLocationField.setPromptText("Τοποθεσία εύρεσης");
        grid.add(new Label("Τοποθεσία:"), 0, 4);
        grid.add(foundLocationField, 1, 4);
        
        // Storage location
        storageLocationField = new TextField();
        storageLocationField.setPromptText("Τοποθεσία αποθήκευσης");
        grid.add(new Label("Αποθήκευση:"), 0, 5);
        grid.add(storageLocationField, 1, 5);
        
        section.getChildren().addAll(sectionTitle, grid);
        return section;
    }
    
    private VBox createFounderInfoSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
        
        Label sectionTitle = new Label("Στοιχεία Αναζητών");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        sectionTitle.setTextFill(Color.rgb(52, 73, 94));
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        
        // Founder name fields
        founderLastNameField = new TextField();
        founderLastNameField.setPromptText("Επώνυμο");
        grid.add(new Label("Επώνυμο *:"), 0, 0);
        grid.add(founderLastNameField, 1, 0);
        
        founderFirstNameField = new TextField();
        founderFirstNameField.setPromptText("Όνομα");
        grid.add(new Label("Όνομα *:"), 0, 1);
        grid.add(founderFirstNameField, 1, 1);
        
        founderIdNumberField = new TextField();
        founderIdNumberField.setPromptText("Αριθμός Ταυτότητας");
        grid.add(new Label("Αριθμός Ταυτότητας *:"), 0, 2);
        grid.add(founderIdNumberField, 1, 2);
        
        founderTelephoneField = new TextField();
        founderTelephoneField.setPromptText("Τηλέφωνο");
        grid.add(new Label("Τηλέφωνο:"), 0, 3);
        grid.add(founderTelephoneField, 1, 3);
        
        founderFatherNameField = new TextField();
        founderFatherNameField.setPromptText("Όνομα Πατέρα");
        grid.add(new Label("Όνομα Πατέρα:"), 0, 4);
        grid.add(founderFatherNameField, 1, 4);
        
        founderAreaInhabitantField = new TextField();
        founderAreaInhabitantField.setPromptText("Περιοχή Κατοικίας");
        grid.add(new Label("Περιοχή:"), 0, 5);
        grid.add(founderAreaInhabitantField, 1, 5);
        
        // Address fields
        founderStreetAddressField = new TextField();
        founderStreetAddressField.setPromptText("Οδός");
        grid.add(new Label("Οδός:"), 0, 6);
        grid.add(founderStreetAddressField, 1, 6);
        
        founderStreetNumberField = new TextField();
        founderStreetNumberField.setPromptText("Αριθμός");
        grid.add(new Label("Αριθμός:"), 0, 7);
        grid.add(founderStreetNumberField, 1, 7);
        
        section.getChildren().addAll(sectionTitle, grid);
        return section;
    }
    
    private VBox createItemInfoSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
        
        Label sectionTitle = new Label("Στοιχεία Αντικειμένου");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        sectionTitle.setTextFill(Color.rgb(52, 73, 94));
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        
        // Item description
        itemDescriptionField = new TextArea();
        itemDescriptionField.setPromptText("Περιγραφή αντικειμένου");
        itemDescriptionField.setPrefRowCount(3);
        grid.add(new Label("Περιγραφή *:"), 0, 0);
        grid.add(itemDescriptionField, 1, 0);
        
        // Item category
        itemCategoryCombo = new ComboBox<>();
        itemCategoryCombo.getItems().addAll("", "Ταυτότητα", "Διαβατήριο", "Τηλέφωνο", "Πορτοφόλι", "Κλειδιά", "Άλλο");
        itemCategoryCombo.setPromptText("Επιλέξτε κατηγορία");
        grid.add(new Label("Κατηγορία:"), 0, 1);
        grid.add(itemCategoryCombo, 1, 1);
        
        // Brand and model
        itemBrandField = new TextField();
        itemBrandField.setPromptText("Μάρκα");
        grid.add(new Label("Μάρκα:"), 0, 2);
        grid.add(itemBrandField, 1, 2);
        
        itemModelField = new TextField();
        itemModelField.setPromptText("Μοντέλο");
        grid.add(new Label("Μοντέλο:"), 0, 3);
        grid.add(itemModelField, 1, 3);
        
        // Color and serial
        itemColorField = new TextField();
        itemColorField.setPromptText("Χρώμα");
        grid.add(new Label("Χρώμα:"), 0, 4);
        grid.add(itemColorField, 1, 4);
        
        itemSerialNumberField = new TextField();
        itemSerialNumberField.setPromptText("Σειριακός Αριθμός");
        grid.add(new Label("Σειριακός Αριθμός:"), 0, 5);
        grid.add(itemSerialNumberField, 1, 5);
        
        // Other details
        itemOtherDetailsField = new TextArea();
        itemOtherDetailsField.setPromptText("Επιπλέον λεπτομέρειες");
        itemOtherDetailsField.setPrefRowCount(2);
        grid.add(new Label("Επιπλέον Λεπτομέρειες:"), 0, 6);
        grid.add(itemOtherDetailsField, 1, 6);
        
        // Status
        statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("stored", "returned", "disposed");
        statusCombo.setValue("stored");
        grid.add(new Label("Κατάσταση:"), 0, 7);
        grid.add(statusCombo, 1, 7);
        
        section.getChildren().addAll(sectionTitle, grid);
        return section;
    }
    
    private VBox createImageSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
        
        Label sectionTitle = new Label("Εικόνα Αντικειμένου");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        sectionTitle.setTextFill(Color.rgb(52, 73, 94));
        
        HBox imageContainer = new HBox(15);
        imageContainer.setAlignment(Pos.CENTER_LEFT);
        
        // Image view
        imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-border-color: #dee2e6; -fx-border-width: 2; -fx-border-radius: 8;");
        
        // Image buttons
        VBox buttonContainer = new VBox(10);
        buttonContainer.setAlignment(Pos.CENTER_LEFT);
        
        addImageBtn = new Button("Προσθήκη Εικόνας");
        addImageBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8 16;");
        addImageBtn.setOnAction(e -> addImage());
        
        removeImageBtn = new Button("Αφαίρεση Εικόνας");
        removeImageBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8 16;");
        removeImageBtn.setOnAction(e -> removeImage());
        removeImageBtn.setVisible(false);
        
        buttonContainer.getChildren().addAll(addImageBtn, removeImageBtn);
        imageContainer.getChildren().addAll(imageView, buttonContainer);
        
        section.getChildren().addAll(sectionTitle, imageContainer);
        return section;
    }
    
    private VBox createTableContainer() {
        VBox tableContainer = new VBox(10);
        tableContainer.setPrefWidth(600);
        tableContainer.setPadding(new Insets(20));
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        Label tableTitle = new Label("Εγγραφές");
        tableTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        tableTitle.setTextFill(Color.rgb(52, 73, 94));
        
        // Create table
        recordsTable = new TableView<>();
        recordsTable.setPrefHeight(500);
        
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
                return new javafx.beans.property.SimpleStringProperty(record.getFound_time().toString());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        timeCol.setPrefWidth(80);
        
        TableColumn<Record, String> descriptionCol = new TableColumn<>("Περιγραφή");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("item_description"));
        descriptionCol.setPrefWidth(200);
        
        TableColumn<Record, String> categoryCol = new TableColumn<>("Κατηγορία");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("item_category"));
        categoryCol.setPrefWidth(120);
        
        TableColumn<Record, String> founderCol = new TableColumn<>("Αναζητών");
        founderCol.setCellValueFactory(cellData -> {
            Record record = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                record.getFounder_first_name() + " " + record.getFounder_last_name()
            );
        });
        founderCol.setPrefWidth(150);
        
        recordsTable.getColumns().addAll(idCol, dateCol, timeCol, descriptionCol, categoryCol, founderCol);
        
        // Initialize data
        recordsList = FXCollections.observableArrayList();
        recordsTable.setItems(recordsList);
        
        // Add selection listener
        recordsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadRecordForEdit(newSelection);
            }
        });
        
        tableContainer.getChildren().addAll(tableTitle, recordsTable);
        return tableContainer;
    }
    
    private HBox createButtonContainer() {
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20, 0, 0, 0));
        
        // Create buttons
        newBtn = createButton("Νέα", "#007bff", e -> newAction());
        editBtn = createButton("Επεξεργασία", "#ffc107", e -> editAction());
        deleteBtn = createButton("Διαγραφή", "#dc3545", e -> deleteAction());
        saveBtn = createButton("Αποθήκευση", "#28a745", e -> saveAction());
        cancelBtn = createButton("Ακύρωση", "#6c757d", e -> cancelAction());
        printBtn = createButton("Εκτύπωση", "#6f42c1", e -> printAction());
        returnBtn = createButton("Επιστροφή", "#17a2b8", e -> returnAction());
        
        // Initially disable edit/delete buttons
        editBtn.setDisable(true);
        deleteBtn.setDisable(true);
        printBtn.setDisable(true);
        
        buttonContainer.getChildren().addAll(newBtn, editBtn, deleteBtn, saveBtn, cancelBtn, printBtn, returnBtn);
        return buttonContainer;
    }
    
    private Button createButton(String text, String color, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8 16;");
        button.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        button.setOnAction(handler);
        return button;
    }
    
    private void loadRecords() {
        try {
            String query = "SELECT * FROM records ORDER BY id DESC";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            recordsList.clear();
            
            while (rs.next()) {
                Record record = new Record();
                record.setId(rs.getInt("id"));
                record.setOfficer_id(rs.getInt("officer_id"));
                record.setFounder_last_name(rs.getString("founder_last_name"));
                record.setFounder_first_name(rs.getString("founder_first_name"));
                record.setFounder_id_number(rs.getString("founder_id_number"));
                record.setFounder_telephone(rs.getString("founder_telephone"));
                record.setFounder_street_address(rs.getString("founder_street_address"));
                record.setFounder_street_number(rs.getString("founder_street_number"));
                record.setFounder_father_name(rs.getString("founder_father_name"));
                record.setFounder_area_inhabitant(rs.getString("founder_area_inhabitant"));
                
                // Handle dates safely
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
                
                record.setFound_location(rs.getString("found_location"));
                record.setItem_description(rs.getString("item_description"));
                record.setItem_category(rs.getString("item_category"));
                record.setItem_brand(rs.getString("item_brand"));
                record.setItem_model(rs.getString("item_model"));
                record.setItem_color(rs.getString("item_color"));
                record.setItem_serial_number(rs.getString("item_serial_number"));
                // Note: item_other_details field doesn't exist in Record model
                record.setStorage_location(rs.getString("storage_location"));
                record.setPicture_path(rs.getString("picture_path"));
                
                recordsList.add(record);
            }
            
            stmt.close();
        } catch (SQLException e) {
            new MessageBoxOk("Σφάλμα φόρτωσης εγγραφών: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadRecordForEdit(Record record) {
        if (record == null) return;
        
        // Populate form fields
        idField.setText(String.valueOf(record.getId()));
        officerIdField.setText(String.valueOf(record.getOfficer_id()));
        founderLastNameField.setText(record.getFounder_last_name());
        founderFirstNameField.setText(record.getFounder_first_name());
        founderIdNumberField.setText(record.getFounder_id_number());
        founderTelephoneField.setText(record.getFounder_telephone());
        founderStreetAddressField.setText(record.getFounder_street_address());
        founderStreetNumberField.setText(record.getFounder_street_number());
        founderFatherNameField.setText(record.getFounder_father_name());
        founderAreaInhabitantField.setText(record.getFounder_area_inhabitant());
        
        // Set dates
        if (record.getFound_date() != null) {
            foundDatePicker.setValue(record.getFound_date().toLocalDate());
        }
        if (record.getFound_time() != null) {
            foundTimeField.setText(record.getFound_time().toString());
        }
        
        foundLocationField.setText(record.getFound_location());
        itemDescriptionField.setText(record.getItem_description());
        itemCategoryCombo.setValue(record.getItem_category());
        itemBrandField.setText(record.getItem_brand());
        itemModelField.setText(record.getItem_model());
        itemColorField.setText(record.getItem_color());
        itemSerialNumberField.setText(record.getItem_serial_number());
        // Note: item_other_details field doesn't exist in Record model
        storageLocationField.setText(record.getStorage_location());
        
        // Load image if exists
        if (record.getPicture_path() != null && !record.getPicture_path().isEmpty()) {
            File imageFile = new File(record.getPicture_path());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                imageView.setImage(image);
                imageView.setVisible(true);
                removeImageBtn.setVisible(true);
                currentImagePath = record.getPicture_path();
            }
        }
        
        // Enable edit/delete buttons
        editBtn.setDisable(false);
        deleteBtn.setDisable(false);
        printBtn.setDisable(false);
    }
    
    private void newAction() {
        clearForm();
        isEditMode = false;
        editingRecordId = -1;
        enableFormFields(true);
        saveBtn.setDisable(false);
        cancelBtn.setDisable(false);
        editBtn.setDisable(true);
        deleteBtn.setDisable(true);
        printBtn.setDisable(true);
    }
    
    private void editAction() {
        if (recordsTable.getSelectionModel().getSelectedItem() != null) {
            isEditMode = true;
            editingRecordId = recordsTable.getSelectionModel().getSelectedItem().getId();
            enableFormFields(true);
            saveBtn.setDisable(false);
            cancelBtn.setDisable(false);
        }
    }
    
    private void deleteAction() {
        Record selectedRecord = recordsTable.getSelectionModel().getSelectedItem();
        if (selectedRecord != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Επιβεβαίωση Διαγραφής");
            alert.setHeaderText("Διαγραφή Εγγραφής");
            alert.setContentText("Είστε σίγουροι ότι θέλετε να διαγράψετε αυτή την εγγραφή;");
            
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        String query = "DELETE FROM records WHERE id = ?";
                        PreparedStatement stmt = connection.prepareStatement(query);
                        stmt.setInt(1, selectedRecord.getId());
                        stmt.executeUpdate();
                        stmt.close();
                        
        // Log activity
        ActivityLogger.logActivity(String.valueOf(currentUser.getAm()), "DELETE_RECORD", 
            "Deleted record ID: " + selectedRecord.getId());
                        
                        loadRecords();
                        clearForm();
                        new MessageBoxOk("Η εγγραφή διαγράφηκε επιτυχώς.");
                    } catch (SQLException e) {
                        new MessageBoxOk("Σφάλμα διαγραφής εγγραφής: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    
    private void saveAction() {
        if (!validateForm()) {
            return;
        }
        
        try {
            if (isEditMode) {
                updateRecord();
            } else {
                insertRecord();
            }
            
            loadRecords();
            clearForm();
            isEditMode = false;
            editingRecordId = -1;
            saveBtn.setDisable(true);
            cancelBtn.setDisable(true);
            
        } catch (SQLException e) {
            new MessageBoxOk("Σφάλμα αποθήκευσης: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void insertRecord() throws SQLException {
        String query = "INSERT INTO records (officer_id, founder_last_name, founder_first_name, " +
                      "founder_id_number, founder_telephone, founder_street_address, founder_street_number, " +
                      "founder_father_name, founder_area_inhabitant, found_date, found_time, found_location, " +
                      "item_description, item_category, item_brand, item_model, item_color, item_serial_number, " +
                      "item_other_details, storage_location, picture_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement stmt = connection.prepareStatement(query);
        
        stmt.setInt(1, currentUser.getAm());
        stmt.setString(2, founderLastNameField.getText());
        stmt.setString(3, founderFirstNameField.getText());
        stmt.setString(4, founderIdNumberField.getText());
        stmt.setString(5, founderTelephoneField.getText());
        stmt.setString(6, founderStreetAddressField.getText());
        stmt.setString(7, founderStreetNumberField.getText());
        stmt.setString(8, founderFatherNameField.getText());
        stmt.setString(9, founderAreaInhabitantField.getText());
        stmt.setString(10, foundDatePicker.getValue().toString());
        stmt.setString(11, foundTimeField.getText());
        stmt.setString(12, foundLocationField.getText());
        stmt.setString(13, itemDescriptionField.getText());
        stmt.setString(14, itemCategoryCombo.getValue());
        stmt.setString(15, itemBrandField.getText());
        stmt.setString(16, itemModelField.getText());
        stmt.setString(17, itemColorField.getText());
        stmt.setString(18, itemSerialNumberField.getText());
        stmt.setString(19, itemOtherDetailsField.getText());
        stmt.setString(20, storageLocationField.getText());
        stmt.setString(21, currentImagePath);
        
        stmt.executeUpdate();
        stmt.close();
        
        // Log activity
        ActivityLogger.logActivity(String.valueOf(currentUser.getAm()), "CREATE_RECORD", 
            "Created new record");
        
        new MessageBoxOk("Η εγγραφή αποθηκεύτηκε επιτυχώς.");
    }
    
    private void updateRecord() throws SQLException {
        String query = "UPDATE records SET founder_last_name = ?, founder_first_name = ?, " +
                      "founder_id_number = ?, founder_telephone = ?, founder_street_address = ?, " +
                      "founder_street_number = ?, founder_father_name = ?, founder_area_inhabitant = ?, " +
                      "found_date = ?, found_time = ?, found_location = ?, item_description = ?, " +
                      "item_category = ?, item_brand = ?, item_model = ?, item_color = ?, " +
                      "item_serial_number = ?, item_other_details = ?, storage_location = ?, " +
                      "picture_path = ? WHERE id = ?";
        
        PreparedStatement stmt = connection.prepareStatement(query);
        
        stmt.setString(1, founderLastNameField.getText());
        stmt.setString(2, founderFirstNameField.getText());
        stmt.setString(3, founderIdNumberField.getText());
        stmt.setString(4, founderTelephoneField.getText());
        stmt.setString(5, founderStreetAddressField.getText());
        stmt.setString(6, founderStreetNumberField.getText());
        stmt.setString(7, founderFatherNameField.getText());
        stmt.setString(8, founderAreaInhabitantField.getText());
        stmt.setString(9, foundDatePicker.getValue().toString());
        stmt.setString(10, foundTimeField.getText());
        stmt.setString(11, foundLocationField.getText());
        stmt.setString(12, itemDescriptionField.getText());
        stmt.setString(13, itemCategoryCombo.getValue());
        stmt.setString(14, itemBrandField.getText());
        stmt.setString(15, itemModelField.getText());
        stmt.setString(16, itemColorField.getText());
        stmt.setString(17, itemSerialNumberField.getText());
        stmt.setString(18, itemOtherDetailsField.getText());
        stmt.setString(19, storageLocationField.getText());
        stmt.setString(20, currentImagePath);
        stmt.setInt(21, editingRecordId);
        
        stmt.executeUpdate();
        stmt.close();
        
        // Log activity
        ActivityLogger.logActivity(String.valueOf(currentUser.getAm()), "UPDATE_RECORD", 
            "Updated record ID: " + editingRecordId);
        
        new MessageBoxOk("Η εγγραφή ενημερώθηκε επιτυχώς.");
    }
    
    private void cancelAction() {
        clearForm();
        isEditMode = false;
        editingRecordId = -1;
        enableFormFields(false);
        saveBtn.setDisable(true);
        cancelBtn.setDisable(true);
        editBtn.setDisable(true);
        deleteBtn.setDisable(true);
        printBtn.setDisable(true);
    }
    
    private void printAction() {
        Record selectedRecord = recordsTable.getSelectionModel().getSelectedItem();
        if (selectedRecord != null) {
            // TODO: Implement print functionality
            new MessageBoxOk("Εκτύπωση εγγραφής ID: " + selectedRecord.getId() + " - Σε εξέλιξη");
        }
    }
    
    private void returnAction() {
        close();
    }
    
    private void addImage() {
        try {
            File imageFile = ImageManager.selectImageFile(this);
            if (imageFile != null) {
                // Copy image to data folder and get relative path
                String relativePath = ImageManager.copyImageToDataFolder(imageFile, editingRecordId > 0 ? editingRecordId : 1);
                if (relativePath != null) {
                    Image image = new Image(imageFile.toURI().toString());
                    imageView.setImage(image);
                    imageView.setVisible(true);
                    removeImageBtn.setVisible(true);
                    currentImagePath = relativePath;
                }
            }
        } catch (Exception e) {
            new MessageBoxOk("Σφάλμα επιλογής εικόνας: " + e.getMessage());
        }
    }
    
    private void removeImage() {
        imageView.setImage(null);
        imageView.setVisible(false);
        removeImageBtn.setVisible(false);
        currentImagePath = null;
    }
    
    private void clearForm() {
        idField.clear();
        founderLastNameField.clear();
        founderFirstNameField.clear();
        founderIdNumberField.clear();
        founderTelephoneField.clear();
        founderStreetAddressField.clear();
        founderStreetNumberField.clear();
        founderFatherNameField.clear();
        founderAreaInhabitantField.clear();
        foundDatePicker.setValue(LocalDate.now().minusDays(1));
        foundTimeField.setText(LocalTime.now().minusMinutes(30).format(DateTimeFormatter.ofPattern("HH:mm")));
        foundLocationField.clear();
        itemDescriptionField.clear();
        itemCategoryCombo.setValue(null);
        itemBrandField.clear();
        itemModelField.clear();
        itemColorField.clear();
        itemSerialNumberField.clear();
        itemOtherDetailsField.clear();
        storageLocationField.clear();
        statusCombo.setValue("stored");
        
        // Clear image
        imageView.setImage(null);
        imageView.setVisible(false);
        removeImageBtn.setVisible(false);
        currentImagePath = null;
    }
    
    private void enableFormFields(boolean enabled) {
        founderLastNameField.setEditable(enabled);
        founderFirstNameField.setEditable(enabled);
        founderIdNumberField.setEditable(enabled);
        founderTelephoneField.setEditable(enabled);
        founderStreetAddressField.setEditable(enabled);
        founderStreetNumberField.setEditable(enabled);
        founderFatherNameField.setEditable(enabled);
        founderAreaInhabitantField.setEditable(enabled);
        foundDatePicker.setDisable(!enabled);
        foundTimeField.setEditable(enabled);
        foundLocationField.setEditable(enabled);
        itemDescriptionField.setEditable(enabled);
        itemCategoryCombo.setDisable(!enabled);
        itemBrandField.setEditable(enabled);
        itemModelField.setEditable(enabled);
        itemColorField.setEditable(enabled);
        itemSerialNumberField.setEditable(enabled);
        itemOtherDetailsField.setEditable(enabled);
        storageLocationField.setEditable(enabled);
        statusCombo.setDisable(!enabled);
        addImageBtn.setDisable(!enabled);
    }
    
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();
        
        if (founderLastNameField.getText().trim().isEmpty()) {
            errors.append("Το επώνυμο είναι υποχρεωτικό\n");
        }
        if (founderFirstNameField.getText().trim().isEmpty()) {
            errors.append("Το όνομα είναι υποχρεωτικό\n");
        }
        if (founderIdNumberField.getText().trim().isEmpty()) {
            errors.append("Ο αριθμός ταυτότητας είναι υποχρεωτικός\n");
        }
        if (itemDescriptionField.getText().trim().isEmpty()) {
            errors.append("Η περιγραφή αντικειμένου είναι υποχρεωτική\n");
        }
        if (foundDatePicker.getValue() == null) {
            errors.append("Η ημερομηνία εύρεσης είναι υποχρεωτική\n");
        }
        if (foundTimeField.getText().trim().isEmpty()) {
            errors.append("Η ώρα εύρεσης είναι υποχρεωτική\n");
        }
        
        if (errors.length() > 0) {
            new MessageBoxOk(errors.toString());
            return false;
        }
        
        return true;
    }
}
