package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.states.EnumFormState;
import com.charamidis.lostAndFound.utils.AppLogger;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import com.charamidis.lostAndFound.utils.PrintReport;
import com.charamidis.lostAndFound.utils.ImageManager;
import com.charamidis.lostAndFound.web.WebServerManager;
import com.charamidis.lostAndFound.utils.ActivityLogger;
import com.charamidis.lostAndFound.pages.ReturnScreen;
import com.charamidis.lostAndFound.models.User;
import com.charamidis.lostAndFound.models.Record;
import javafx.stage.Screen;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.pdf417.PDF417Writer;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.geometry.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FormRecord {

    // Modern UI Components
    Stage window;
    Scene scene;
    VBox mainContainer;
    ScrollPane scrollPane;
    ListView<Record> listView;
    
    // Form sections
    VBox personalInfoSection;
    VBox itemInfoSection;
    VBox locationInfoSection;
    VBox imageSection;
    
    // Labels
    Label lblId, lblDate, lblOfficerId, lblFoundTime, lblFounderLastName, lblFounderFirstName, 
          lblFounderIdNumber, lblFounderTelephone, lblFounderStreetNumber, lblFounderStreetAddress, 
          lblFounderFatherName, lblFounderAreaInhabitant, lblFoundDate, lblFoundLocation, 
          lblItemDescription, lblItemCategory, lblItemBrand, lblItemModel, lblItemColor, 
          lblItemSerialNumber, lblStorageLocation, lblItemPicture;
    
    // Input fields
    TextField txtId, txtOfficerId, txtFounderLastName, txtFoundTime, txtFounderFirstName, 
              txtFounderIdNumber, txtFounderTelephone, txtFounderStreetAddress, txtFounderStreetNumber, 
              txtFounderFatherName, txtFounderAreaInhabitant, txtFoundDate, txtFoundLocation, 
              txtItemDescription, txtItemCategory, txtItemBrand, txtItemModel, txtItemColor, 
              txtItemSerialNumber, txtStorageLocation;
    
    // Image controls
    Button btnSelectImage, btnRemoveImage;
    ImageView imageView;
    File selectedImageFile;
    VBox imageBox;
    DatePicker txtDate;
    
    // Action buttons
    Button btnNew, btnEdit, btnDelete, btnPrint, btnReturn, btnSave, btnCancel;
    HBox actionButtons;
    HBox saveCancelButtons;
    
    // Form state
    EnumFormState enumFormState;
    User currUser;
    Connection connection;
    private static final Logger logger = AppLogger.getLogger();

    public FormRecord(Connection conn, User user) {
        connection = conn;
        currUser = user;
        enumFormState = EnumFormState.VIEW;
        
        createModernUI();
        loadRecords();
        window.show();
    }

    private void createModernUI() {
        // Create main container with modern styling
        mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #f5f5f5;");
        
        // Create header
        createHeader();
        
        // Create form sections
        createFormSections();
        
        // Create action buttons
        createActionButtons();
        
        // Create list view
        createListView();
        
        // Setup window
        setupWindow();
    }

    private void createHeader() {
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 20, 0));
        
        Label titleLabel = new Label("📝 Record Form");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#333333"));
        titleLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 2, 0, 0, 1);");
        
        headerBox.getChildren().add(titleLabel);
        mainContainer.getChildren().add(headerBox);
    }

    private void createFormSections() {
        // Create scrollable form container
        VBox formContainer = new VBox(20);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-border-width: 1;");
        
        // Personal Information Section
        personalInfoSection = createSection("👤 Personal Information", createPersonalInfoFields());
        
        // Item Information Section
        itemInfoSection = createSection("📦 Item Information", createItemInfoFields());
        
        // Location Information Section
        locationInfoSection = createSection("📍 Location Information", createLocationInfoFields());
        
        // Image Section
        VBox imageContainer = createImageFields();
        imageSection = new VBox(15);
        imageSection.setPadding(new Insets(20));
        imageSection.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-border-color: #dee2e6; -fx-border-radius: 8;");
        
        Label imageSectionTitle = new Label("🖼️ Item Image");
        imageSectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        imageSectionTitle.setTextFill(Color.web("#495057"));
        imageSectionTitle.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 1, 0, 0, 1);");
        
        imageSection.getChildren().addAll(imageSectionTitle, imageContainer);
        
        formContainer.getChildren().addAll(personalInfoSection, itemInfoSection, locationInfoSection, imageSection);
        
        // Add scroll pane
        scrollPane = new ScrollPane(formContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background-radius: 10;");
        
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        mainContainer.getChildren().add(scrollPane);
    }

    private VBox createSection(String title, GridPane fields) {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-border-color: #dee2e6; -fx-border-radius: 8;");
        
        Label sectionTitle = new Label(title);
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.web("#495057"));
        sectionTitle.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 1, 0, 0, 1);");
        
        section.getChildren().addAll(sectionTitle, fields);
        return section;
    }

    private GridPane createPersonalInfoFields() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        // Create labels and fields
        createField(grid, 0, "ID:", lblId = new Label("ID:"), txtId = new TextField());
        createField(grid, 1, "Officer ID:", lblOfficerId = new Label("Officer ID:"), txtOfficerId = new TextField());
        createField(grid, 2, "Record Date:", lblDate = new Label("Record Date:"), txtDate = new DatePicker());
        createField(grid, 3, "Last Name:", lblFounderLastName = new Label("Last Name:"), txtFounderLastName = new TextField(), true);
        createField(grid, 4, "First Name:", lblFounderFirstName = new Label("First Name:"), txtFounderFirstName = new TextField(), true);
        createField(grid, 5, "ID Number:", lblFounderIdNumber = new Label("ID Number:"), txtFounderIdNumber = new TextField(), true);
        createField(grid, 6, "Father's Name:", lblFounderFatherName = new Label("Father's Name:"), txtFounderFatherName = new TextField());
        createField(grid, 7, "Area:", lblFounderAreaInhabitant = new Label("Area:"), txtFounderAreaInhabitant = new TextField());
        createField(grid, 8, "Street Address:", lblFounderStreetAddress = new Label("Street Address:"), txtFounderStreetAddress = new TextField());
        createField(grid, 9, "Street Number:", lblFounderStreetNumber = new Label("Street Number:"), txtFounderStreetNumber = new TextField());
        createField(grid, 10, "Telephone:", lblFounderTelephone = new Label("Telephone:"), txtFounderTelephone = new TextField());
        
        return grid;
    }

    private GridPane createItemInfoFields() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        createField(grid, 0, "Description:", lblItemDescription = new Label("Description:"), txtItemDescription = new TextField(), true);
        createField(grid, 1, "Category:", lblItemCategory = new Label("Category:"), txtItemCategory = new TextField());
        createField(grid, 2, "Brand:", lblItemBrand = new Label("Brand:"), txtItemBrand = new TextField());
        createField(grid, 3, "Model:", lblItemModel = new Label("Model:"), txtItemModel = new TextField());
        createField(grid, 4, "Color:", lblItemColor = new Label("Color:"), txtItemColor = new TextField());
        createField(grid, 5, "Serial Number:", lblItemSerialNumber = new Label("Serial Number:"), txtItemSerialNumber = new TextField());
        createField(grid, 6, "Storage Location:", lblStorageLocation = new Label("Storage Location:"), txtStorageLocation = new TextField());
        
        return grid;
    }

    private GridPane createLocationInfoFields() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        createField(grid, 0, "Found Location:", lblFoundLocation = new Label("Found Location:"), txtFoundLocation = new TextField(), true);
        createField(grid, 1, "Found Date:", lblFoundDate = new Label("Found Date:"), txtFoundDate = new TextField());
        createField(grid, 2, "Found Time:", lblFoundTime = new Label("Found Time:"), txtFoundTime = new TextField());
        
        return grid;
    }

    private VBox createImageFields() {
        VBox imageContainer = new VBox(15);
        imageContainer.setPadding(new Insets(10));
        
        // Image buttons
        HBox imageButtons = new HBox(10);
        btnSelectImage = createStyledButton("📷 Select Image", "#6c757d", "#868e96");
        btnRemoveImage = createStyledButton("🗑️ Remove Image", "#6c757d", "#868e96");
        
        btnSelectImage.setOnAction(e -> {
            File selectedFile = ImageManager.selectImageFile(window);
            if (selectedFile != null) {
                imageView.setImage(new Image(selectedFile.toURI().toString()));
                selectedImageFile = selectedFile;
                imageView.setVisible(true);
            }
        });
        
        btnRemoveImage.setOnAction(e -> {
            imageView.setImage(null);
            selectedImageFile = null;
            imageView.setVisible(false);
        });
        
        imageButtons.getChildren().addAll(btnSelectImage, btnRemoveImage);
        
        // Image view
        imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5;");
        imageView.setVisible(false);
        
        imageContainer.getChildren().addAll(imageButtons, imageView);
        
        return imageContainer;
    }

    private void createField(GridPane grid, int row, String labelText, Label label, Control field) {
        createField(grid, row, labelText, label, field, false);
    }
    
    private void createField(GridPane grid, int row, String labelText, Label label, Control field, boolean required) {
        label.setText(labelText + (required ? " *" : ""));
        label.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        label.setTextFill(required ? Color.web("#dc3545") : Color.web("#495057"));
        
        field.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #ced4da; -fx-padding: 8;");
        
        grid.add(label, 0, row);
        grid.add(field, 1, row);
        
        // Set column constraints
        ColumnConstraints labelCol = new ColumnConstraints();
        labelCol.setPrefWidth(150);
        labelCol.setHalignment(HPos.RIGHT);
        
        ColumnConstraints fieldCol = new ColumnConstraints();
        fieldCol.setHgrow(Priority.ALWAYS);
        
        if (grid.getColumnConstraints().isEmpty()) {
            grid.getColumnConstraints().addAll(labelCol, fieldCol);
        }
    }

    private void createActionButtons() {
        // Main action buttons
        actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER);
        actionButtons.setPadding(new Insets(15));
        actionButtons.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        btnNew = createStyledButton("➕ New", "#6c757d", "#868e96");
        btnEdit = createStyledButton("✏️ Edit", "#6c757d", "#868e96");
        btnDelete = createStyledButton("🗑️ Delete", "#6c757d", "#868e96");
        btnPrint = createStyledButton("🖨️ Print", "#6c757d", "#868e96");
        btnReturn = createStyledButton("↩️ Return", "#6c757d", "#868e96");
        
        // Set button actions
        btnNew.setOnAction(e -> {
            enumFormState = EnumFormState.ADD;
            changeState(enumFormState, null);
            txtFounderLastName.requestFocus();
        });
        
        btnEdit.setOnAction(e -> {
            if (listView.getSelectionModel().getSelectedItem() != null) {
                enumFormState = EnumFormState.EDIT;
                changeState(enumFormState, listView.getSelectionModel().getSelectedItem());
                txtFounderLastName.requestFocus();
            }
        });
        
        btnDelete.setOnAction(e -> deleteRecord());
        btnPrint.setOnAction(e -> printRecord());
        btnReturn.setOnAction(e -> returnRecord());
        
        // Add reset ID button for admin users
        if (currUser.getAm() == 287874) { // Admin user
            Button btnResetId = createStyledButton("🔄 Reset ID", "#6c757d", "#868e96");
            btnResetId.setOnAction(e -> resetRecordId());
            actionButtons.getChildren().add(btnResetId);
        }
        
        // Add buttons based on user role
        if (currUser.getRole().equals("admin")) {
            actionButtons.getChildren().addAll(btnNew, btnEdit, btnDelete, btnPrint, btnReturn);
        } else {
            actionButtons.getChildren().addAll(btnNew, btnEdit, btnPrint, btnReturn);
        }
        
        // Save/Cancel buttons (initially hidden)
        saveCancelButtons = new HBox(15);
        saveCancelButtons.setAlignment(Pos.CENTER);
        saveCancelButtons.setPadding(new Insets(15));
        saveCancelButtons.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 5; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-border-width: 1;");
        
        btnSave = createStyledButton("💾 Save", "#6c757d", "#868e96");
        btnCancel = createStyledButton("❌ Cancel", "#6c757d", "#868e96");
        
        btnSave.setOnAction(e -> saveRecord());
        btnCancel.setOnAction(e -> cancelEdit());
        
        saveCancelButtons.getChildren().addAll(btnSave, btnCancel);
        saveCancelButtons.setVisible(false);
        
        mainContainer.getChildren().addAll(actionButtons, saveCancelButtons);
    }

    private void createListView() {
        listView = new ListView<>();
        listView.setPrefHeight(200);
        listView.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        // List view selection listener
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnReturn.setDisable(false);
            
            if (newValue != null) {
                int itemId = newValue.getId();
                
                try {
                    String query = "SELECT id FROM returns WHERE id = ?";
                    PreparedStatement btnStm = connection.prepareStatement(query);
                    btnStm.setInt(1, itemId);
                    ResultSet set = btnStm.executeQuery();
                    
                    if (set.next()) {
                        btnReturn.setDisable(true);
                    }
                    
                    set.close();
                    btnStm.close();
                } catch (SQLException e) {
                    new MessageBoxOk(e.getMessage());
                    logger.log(Level.SEVERE, "Error with form record with sql conn", e);
                }
            }
        });
        
        // Barcode shortcut
        listView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.B && event.isShiftDown() && event.isControlDown()) {
                generateBarcode();
            }
        });
        
        VBox listContainer = new VBox(10);
        Label listTitle = new Label("📋 Records List");
        listTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        listTitle.setTextFill(Color.web("#2c3e50"));
        
        listContainer.getChildren().addAll(listTitle, listView);
        mainContainer.getChildren().add(listContainer);
    }

    private void setupWindow() {
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();
        
        double windowWidth = Math.min(1000, screenWidth * 0.9);
        double windowHeight = Math.min(800, screenHeight * 0.9);
        
        scene = new Scene(mainContainer, windowWidth, windowHeight);
        window = new Stage();
        window.setTitle("📝 Record Form - Lost & Found System");
        window.setScene(scene);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(true);
        window.setMinWidth(800);
        window.setMinHeight(600);
        window.setMaxWidth(screenWidth * 0.95);
        window.setMaxHeight(screenHeight * 0.95);
        
        // Center window
        window.setX((screenWidth - windowWidth) / 2);
        window.setY((screenHeight - windowHeight) / 2);
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

    private void loadRecords() {
        listView.getItems().clear();
        
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
                listView.getItems().add(record);
            }
        } catch (SQLException exception) {
            new MessageBoxOk("Error loading records: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void changeState(EnumFormState state, Record record) {
        enumFormState = state;
        
        switch (state) {
            case ADD:
                clearForm();
                enableForm(true);
                actionButtons.setVisible(false);
                saveCancelButtons.setVisible(true);
                imageSection.setVisible(true);
                break;
            case EDIT:
                if (record != null) {
                    populateForm(record);
                    enableForm(true);
                    actionButtons.setVisible(false);
                    saveCancelButtons.setVisible(true);
                    imageSection.setVisible(true);
                }
                break;
            case VIEW:
                enableForm(false);
                actionButtons.setVisible(true);
                saveCancelButtons.setVisible(false);
                imageSection.setVisible(true); // Always show image section
                break;
        }
    }

    private void clearForm() {
        txtId.clear();
        // Set officer ID to current user
        txtOfficerId.setText(String.valueOf(currUser.getAm()));
        
        // Set current date and time minus 30 minutes
        LocalDateTime now = LocalDateTime.now().minusMinutes(30);
        txtDate.setValue(now.toLocalDate());
        txtFoundDate.setText(now.toLocalDate().toString());
        txtFoundTime.setText(now.toLocalTime().withSecond(0).toString());
        
        txtFounderLastName.clear();
        txtFounderFirstName.clear();
        txtFounderIdNumber.clear();
        txtFounderFatherName.clear();
        txtFounderAreaInhabitant.clear();
        txtFounderStreetAddress.clear();
        txtFounderStreetNumber.clear();
        txtFounderTelephone.clear();
        txtFoundLocation.clear();
        txtItemDescription.clear();
        txtItemCategory.clear();
        txtItemBrand.clear();
        txtItemModel.clear();
        txtItemColor.clear();
        txtItemSerialNumber.clear();
        txtStorageLocation.clear();
        imageView.setImage(null);
        selectedImageFile = null;
    }

    private void populateForm(Record record) {
        txtId.setText(String.valueOf(record.getId()));
        txtOfficerId.setText(String.valueOf(record.getOfficer_id()));
        
        // Parse the record_datetime properly
        try {
            if (record.getRecord_date() != null && !record.getRecord_date().isEmpty()) {
                // Try to parse as LocalDateTime first
                if (record.getRecord_date().contains("T")) {
                    LocalDateTime dateTime = LocalDateTime.parse(record.getRecord_date());
                    txtDate.setValue(dateTime.toLocalDate());
                } else {
                    // Fallback to LocalDate
                    txtDate.setValue(LocalDate.parse(record.getRecord_date()));
                }
            }
        } catch (Exception e) {
            // If parsing fails, set to current date
            txtDate.setValue(LocalDate.now());
        }
        
        txtFounderLastName.setText(record.getFounder_last_name());
        txtFounderFirstName.setText(record.getFounder_first_name());
        txtFounderIdNumber.setText(record.getFounder_id_number());
        txtFounderFatherName.setText(record.getFounder_father_name() != null ? record.getFounder_father_name() : "");
        txtFounderAreaInhabitant.setText(record.getFounder_area_inhabitant() != null ? record.getFounder_area_inhabitant() : "");
        txtFounderStreetAddress.setText(record.getFounder_street_address() != null ? record.getFounder_street_address() : "");
        txtFounderStreetNumber.setText(record.getFounder_street_number() != null ? record.getFounder_street_number() : "");
        txtFounderTelephone.setText(record.getFounder_telephone() != null ? record.getFounder_telephone() : "");
        txtFoundLocation.setText(record.getFound_location());
        txtFoundDate.setText(record.getFound_date() != null ? record.getFound_date().toString() : "");
        txtFoundTime.setText(record.getFound_time() != null ? record.getFound_time().toString() : "");
        txtItemDescription.setText(record.getItem_description());
        txtItemCategory.setText(record.getItem_category() != null ? record.getItem_category() : "");
        txtItemBrand.setText(record.getItem_brand() != null ? record.getItem_brand() : "");
        txtItemModel.setText(record.getItem_model() != null ? record.getItem_model() : "");
        txtItemColor.setText(record.getItem_color() != null ? record.getItem_color() : "");
        txtItemSerialNumber.setText(record.getItem_serial_number() != null ? record.getItem_serial_number() : "");
        txtStorageLocation.setText(record.getStorage_location() != null ? record.getStorage_location() : "");
        
        // Load image if exists
        if (record.getPicture_path() != null && !record.getPicture_path().isEmpty()) {
            try {
                Image image = new Image("file:" + record.getPicture_path());
                imageView.setImage(image);
                imageView.setVisible(true);
                selectedImageFile = new File(record.getPicture_path());
            } catch (Exception e) {
                // Image not found or invalid
                imageView.setImage(null);
                imageView.setVisible(false);
                selectedImageFile = null;
            }
        } else {
            imageView.setImage(null);
            imageView.setVisible(false);
            selectedImageFile = null;
        }
    }

    private void enableForm(boolean enabled) {
        // ID and Officer ID are always disabled (non-editable)
        txtId.setDisable(true);
        txtOfficerId.setDisable(true);
        
        // Other fields are enabled/disabled based on state
        txtDate.setDisable(!enabled);
        txtFounderLastName.setDisable(!enabled);
        txtFounderFirstName.setDisable(!enabled);
        txtFounderIdNumber.setDisable(!enabled);
        txtFounderFatherName.setDisable(!enabled);
        txtFounderAreaInhabitant.setDisable(!enabled);
        txtFounderStreetAddress.setDisable(!enabled);
        txtFounderStreetNumber.setDisable(!enabled);
        txtFounderTelephone.setDisable(!enabled);
        txtFoundLocation.setDisable(!enabled);
        txtFoundDate.setDisable(!enabled);
        txtFoundTime.setDisable(!enabled);
        txtItemDescription.setDisable(!enabled);
        txtItemCategory.setDisable(!enabled);
        txtItemBrand.setDisable(!enabled);
        txtItemModel.setDisable(!enabled);
        txtItemColor.setDisable(!enabled);
        txtItemSerialNumber.setDisable(!enabled);
        txtStorageLocation.setDisable(!enabled);
        btnSelectImage.setDisable(!enabled);
        btnRemoveImage.setDisable(!enabled);
    }

    private void saveRecord() {
        if (validateForm()) {
            try {
                if (enumFormState == EnumFormState.ADD) {
                    addRecord();
                } else if (enumFormState == EnumFormState.EDIT) {
                    updateRecord();
                }
                changeState(EnumFormState.VIEW, null);
                loadRecords();
            } catch (Exception e) {
                new MessageBoxOk("Error saving record: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void cancelEdit() {
        changeState(EnumFormState.VIEW, null);
    }

    private void addRecord() {
        try {
            // Get the next ID
            String nextIdQuery = "SELECT COALESCE(MAX(id), 0) + 1 FROM records";
            try (PreparedStatement stmt = connection.prepareStatement(nextIdQuery)) {
                ResultSet rs = stmt.executeQuery();
                int newId = rs.getInt(1);
                
                // Set the ID in the form
                txtId.setText(String.valueOf(newId));
                
                // Set the officer ID to current user
                txtOfficerId.setText(String.valueOf(currUser.getAm()));
                
                // Insert new record
                String insertQuery = "INSERT INTO records (id, officer_id, record_datetime, founder_last_name, founder_first_name, " +
                    "founder_id_number, founder_father_name, founder_area_inhabitant, founder_street_address, " +
                    "founder_street_number, founder_telephone, found_location, found_date, found_time, " +
                    "item_description, item_category, item_brand, item_model, item_color, item_serial_number, " +
                    "storage_location, picture_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, newId);
                    insertStmt.setInt(2, currUser.getAm());
                    
                    // Create datetime from date and time
                    LocalDateTime recordDateTime = LocalDateTime.of(
                        txtDate.getValue() != null ? txtDate.getValue() : LocalDate.now(),
                        LocalTime.now()
                    );
                    insertStmt.setString(3, recordDateTime.toString());
                    
                    insertStmt.setString(4, txtFounderLastName.getText());
                    insertStmt.setString(5, txtFounderFirstName.getText());
                    insertStmt.setString(6, txtFounderIdNumber.getText());
                    insertStmt.setString(7, txtFounderFatherName.getText().isEmpty() ? null : txtFounderFatherName.getText());
                    insertStmt.setString(8, txtFounderAreaInhabitant.getText().isEmpty() ? null : txtFounderAreaInhabitant.getText());
                    insertStmt.setString(9, txtFounderStreetAddress.getText().isEmpty() ? null : txtFounderStreetAddress.getText());
                    insertStmt.setString(10, txtFounderStreetNumber.getText().isEmpty() ? null : txtFounderStreetNumber.getText());
                    insertStmt.setString(11, txtFounderTelephone.getText().isEmpty() ? null : txtFounderTelephone.getText());
                    insertStmt.setString(12, txtFoundLocation.getText());
                    insertStmt.setString(13, txtFoundDate.getText().isEmpty() ? null : txtFoundDate.getText());
                    insertStmt.setString(14, txtFoundTime.getText().isEmpty() ? null : txtFoundTime.getText());
                    insertStmt.setString(15, txtItemDescription.getText());
                    insertStmt.setString(16, txtItemCategory.getText().isEmpty() ? null : txtItemCategory.getText());
                    insertStmt.setString(17, txtItemBrand.getText().isEmpty() ? null : txtItemBrand.getText());
                    insertStmt.setString(18, txtItemModel.getText().isEmpty() ? null : txtItemModel.getText());
                    insertStmt.setString(19, txtItemColor.getText().isEmpty() ? null : txtItemColor.getText());
                    insertStmt.setString(20, txtItemSerialNumber.getText().isEmpty() ? null : txtItemSerialNumber.getText());
                    insertStmt.setString(21, txtStorageLocation.getText().isEmpty() ? null : txtStorageLocation.getText());
                    
                    // Handle image upload
                    String picturePath = null;
                    if (selectedImageFile != null) {
                        picturePath = ImageManager.copyImageToDataFolder(selectedImageFile, newId);
                    }
                    insertStmt.setString(22, picturePath);
                    
                    insertStmt.executeUpdate();
                    
                    // Log activity
                    ActivityLogger.logRecordAddActivity(currUser.getFirstName() + " " + currUser.getLastName(), 
                        String.valueOf(newId), txtItemDescription.getText());
                    
                    new MessageBoxOk("Record added successfully!");
                }
            }
        } catch (Exception e) {
            new MessageBoxOk("Error adding record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateRecord() {
        try {
            int recordId = Integer.parseInt(txtId.getText());
            
            String updateQuery = "UPDATE records SET officer_id = ?, record_datetime = ?, founder_last_name = ?, " +
                "founder_first_name = ?, founder_id_number = ?, founder_father_name = ?, founder_area_inhabitant = ?, " +
                "founder_street_address = ?, founder_street_number = ?, founder_telephone = ?, found_location = ?, " +
                "found_date = ?, found_time = ?, item_description = ?, item_category = ?, item_brand = ?, " +
                "item_model = ?, item_color = ?, item_serial_number = ?, storage_location = ?, picture_path = ? " +
                "WHERE id = ?";
            
            try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                stmt.setInt(1, currUser.getAm());
                
                // Create datetime from date and time
                LocalDateTime recordDateTime = LocalDateTime.of(
                    txtDate.getValue() != null ? txtDate.getValue() : LocalDate.now(),
                    LocalTime.now()
                );
                stmt.setString(2, recordDateTime.toString());
                stmt.setString(3, txtFounderLastName.getText());
                stmt.setString(4, txtFounderFirstName.getText());
                stmt.setString(5, txtFounderIdNumber.getText());
                stmt.setString(6, txtFounderFatherName.getText().isEmpty() ? null : txtFounderFatherName.getText());
                stmt.setString(7, txtFounderAreaInhabitant.getText().isEmpty() ? null : txtFounderAreaInhabitant.getText());
                stmt.setString(8, txtFounderStreetAddress.getText().isEmpty() ? null : txtFounderStreetAddress.getText());
                stmt.setString(9, txtFounderStreetNumber.getText().isEmpty() ? null : txtFounderStreetNumber.getText());
                stmt.setString(10, txtFounderTelephone.getText().isEmpty() ? null : txtFounderTelephone.getText());
                stmt.setString(11, txtFoundLocation.getText());
                stmt.setString(12, txtFoundDate.getText().isEmpty() ? null : txtFoundDate.getText());
                stmt.setString(13, txtFoundTime.getText().isEmpty() ? null : txtFoundTime.getText());
                stmt.setString(14, txtItemDescription.getText());
                stmt.setString(15, txtItemCategory.getText().isEmpty() ? null : txtItemCategory.getText());
                stmt.setString(16, txtItemBrand.getText().isEmpty() ? null : txtItemBrand.getText());
                stmt.setString(17, txtItemModel.getText().isEmpty() ? null : txtItemModel.getText());
                stmt.setString(18, txtItemColor.getText().isEmpty() ? null : txtItemColor.getText());
                stmt.setString(19, txtItemSerialNumber.getText().isEmpty() ? null : txtItemSerialNumber.getText());
                stmt.setString(20, txtStorageLocation.getText().isEmpty() ? null : txtStorageLocation.getText());
                
                // Handle image upload
                String picturePath = null;
                if (selectedImageFile != null) {
                    picturePath = ImageManager.copyImageToDataFolder(selectedImageFile, recordId);
                }
                stmt.setString(21, picturePath);
                stmt.setInt(22, recordId);
                
                stmt.executeUpdate();
                
                // Log activity
                ActivityLogger.logRecordEditActivity(currUser.getFirstName() + " " + currUser.getLastName(), 
                    String.valueOf(recordId), txtItemDescription.getText());
                
                new MessageBoxOk("Record updated successfully!");
            }
        } catch (Exception e) {
            new MessageBoxOk("Error updating record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteRecord() {
        Record selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Delete");
            confirmDialog.setHeaderText("Delete Record #" + selected.getId());
            confirmDialog.setContentText("Are you sure you want to delete this record?");
            confirmDialog.getDialogPane().setStyle("-fx-background-color: white;");
            
            if (confirmDialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM records WHERE id = ?")) {
                    stmt.setInt(1, selected.getId());
                    stmt.executeUpdate();
                    
                    // Log activity
                    ActivityLogger.logRecordDeleteActivity(currUser.getFirstName() + " " + currUser.getLastName(), 
                        String.valueOf(selected.getId()), selected.getItem_description());
                    
                    listView.getItems().remove(selected);
                    new MessageBoxOk("Record deleted successfully.");
                } catch (SQLException ex) {
                    new MessageBoxOk("Error deleting record: " + ex.getMessage());
                }
            }
        }
    }

    private void printRecord() {
        Record record = listView.getSelectionModel().getSelectedItem();
        if (record != null) {
            new PrintReport(record, connection);
        }
    }

    private void returnRecord() {
        Record selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                String query = "SELECT id FROM returns WHERE id = ?";
                PreparedStatement btnStm = connection.prepareStatement(query);
                btnStm.setInt(1, selected.getId());
                ResultSet set = btnStm.executeQuery();
                
                if (!set.next()) {
                    // Log activity
                    ActivityLogger.logReturnActivity(currUser.getFirstName() + " " + currUser.getLastName(), 
                        String.valueOf(selected.getId()), selected.getItem_description());
                    
                    new ReturnScreen(connection, currUser, selected);
                }
                
                set.close();
                btnStm.close();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Cannot return items sql problem", ex);
            }
        }
    }
    
    private void resetRecordId() {
        // Implementation for resetting record ID (admin only)
        Record selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                // Get the next available ID
                String nextIdQuery = "SELECT COALESCE(MAX(id), 0) + 1 FROM records";
                try (PreparedStatement stmt = connection.prepareStatement(nextIdQuery)) {
                    ResultSet rs = stmt.executeQuery();
                    int newId = rs.getInt(1);
                    
                    // Update the record with new ID
                    String updateQuery = "UPDATE records SET id = ? WHERE id = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, newId);
                        updateStmt.setInt(2, selected.getId());
                        updateStmt.executeUpdate();
                        
                        new MessageBoxOk("Record ID reset to: " + newId);
                        loadRecords();
                    }
                }
            } catch (Exception e) {
                new MessageBoxOk("Error resetting record ID: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            new MessageBoxOk("Please select a record to reset ID.");
        }
    }

    private void generateBarcode() {
        Record selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                String query = "SELECT uid FROM records WHERE id = ? AND item_description = ?";
                PreparedStatement stm = connection.prepareStatement(query);
                stm.setInt(1, selected.getId());
                stm.setString(2, selected.getItem_description());
                ResultSet set = stm.executeQuery();
                
                if (set.next()) {
                    String uid = set.getString("uid");
                    generatedBarcode(uid);
                }
                
                set.close();
                stm.close();
            } catch (SQLException e) {
                new MessageBoxOk(e.getMessage() + "\nError printing the barcode");
                logger.log(Level.FINE, "Error with barcode", e);
            }
        }
    }

    private void generatedBarcode(String data) {
        try {
            PDF417Writer writer = new PDF417Writer();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, 0);
            
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.PDF_417, 400, 200, hints);
            
            // Create barcode image
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            
            // Display barcode in a new window
            Stage barcodeStage = new Stage();
            barcodeStage.setTitle("Barcode - " + data);
            
            ImageView barcodeView = new ImageView();
            barcodeView.setImage(convertToFxImage(bufferedImage));
            
            VBox barcodeContainer = new VBox(10);
            barcodeContainer.setPadding(new Insets(20));
            barcodeContainer.setAlignment(Pos.CENTER);
            barcodeContainer.getChildren().addAll(
                new Label("Barcode for: " + data),
                barcodeView
            );
            
            Scene barcodeScene = new Scene(barcodeContainer, 450, 300);
            barcodeStage.setScene(barcodeScene);
            barcodeStage.show();
            
        } catch (WriterException e) {
            new MessageBoxOk("Error generating barcode: " + e.getMessage());
            logger.log(Level.SEVERE, "Error generating barcode", e);
        }
    }

    private Image convertToFxImage(BufferedImage bufferedImage) {
        // Convert BufferedImage to JavaFX Image
        // This is a simplified conversion - you might need a more robust implementation
        return new Image("data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(
            ((java.awt.image.DataBufferByte) bufferedImage.getData().getDataBuffer()).getData()
        ));
    }

    private boolean validateForm() {
        // Basic validation
        if (txtFounderLastName.getText().trim().isEmpty()) {
            new MessageBoxOk("Last Name is required");
            txtFounderLastName.requestFocus();
            return false;
        }
        
        if (txtFounderFirstName.getText().trim().isEmpty()) {
            new MessageBoxOk("First Name is required");
            txtFounderFirstName.requestFocus();
            return false;
        }
        
        if (txtItemDescription.getText().trim().isEmpty()) {
            new MessageBoxOk("Item Description is required");
            txtItemDescription.requestFocus();
            return false;
        }
        
        return true;
    }
}
