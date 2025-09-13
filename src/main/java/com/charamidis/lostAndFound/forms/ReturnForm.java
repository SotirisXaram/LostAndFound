package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.models.Record;
import com.charamidis.lostAndFound.models.User;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import com.charamidis.lostAndFound.utils.StatisticsManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ReturnForm extends Stage {
    
    private Connection connection;
    private User currentUser;
    private Record record;
    
    // Form fields
    private TextField recordIdField;
    private TextField itemDescriptionField;
    private TextField returnOfficerField;
    private TextField returnLastNameField;
    private TextField returnFirstNameField;
    private TextField returnDateField;
    private TextField returnTimeField;
    private TextField returnTelephoneField;
    private TextField returnIdNumberField;
    private TextField returnFatherNameField;
    private TextField returnDateOfBirthField;
    private TextField returnStreetAddressField;
    private TextField returnStreetNumberField;
    private TextArea commentField;
    
    // Buttons
    private Button saveBtn;
    private Button cancelBtn;
    
    public ReturnForm(Connection conn, User user, Record record) {
        this.connection = conn;
        this.currentUser = user;
        this.record = record;
        
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.DECORATED);
        setTitle("Επιστροφή Αντικειμένου - ID: " + record.getId());
        
        createUI();
        populateForm();
        
        // Set window properties
        setMinWidth(600);
        setMinHeight(700);
        setWidth(700);
        setHeight(800);
        
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
        
        // Form content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        VBox formContent = createFormContent();
        scrollPane.setContent(formContent);
        
        // Buttons
        HBox buttonContainer = createButtonContainer();
        
        mainContainer.getChildren().addAll(header, scrollPane, buttonContainer);
        
        Scene scene = new Scene(mainContainer);
        setScene(scene);
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        Label title = new Label("Επιστροφή Αντικειμένου");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.rgb(52, 73, 94));
        
        header.getChildren().add(title);
        return header;
    }
    
    private VBox createFormContent() {
        VBox formContent = new VBox(20);
        formContent.setPadding(new Insets(20));
        
        // Record Information Section
        VBox recordSection = createRecordInfoSection();
        
        // Return Information Section
        VBox returnSection = createReturnInfoSection();
        
        // Claimant Information Section
        VBox claimantSection = createClaimantInfoSection();
        
        formContent.getChildren().addAll(recordSection, returnSection, claimantSection);
        return formContent;
    }
    
    private VBox createRecordInfoSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: #e8f4fd; -fx-background-radius: 8; -fx-border-color: #bee5eb; -fx-border-radius: 8;");
        
        Label sectionTitle = new Label("Πληροφορίες Εγγραφής");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.rgb(0, 123, 255));
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        
        // Record ID (read-only)
        recordIdField = new TextField();
        recordIdField.setEditable(false);
        recordIdField.setStyle("-fx-background-color: #f8f9fa;");
        grid.add(new Label("ID Εγγραφής:"), 0, 0);
        grid.add(recordIdField, 1, 0);
        
        // Item Description (read-only)
        itemDescriptionField = new TextField();
        itemDescriptionField.setEditable(false);
        itemDescriptionField.setStyle("-fx-background-color: #f8f9fa;");
        grid.add(new Label("Περιγραφή Αντικειμένου:"), 0, 1);
        grid.add(itemDescriptionField, 1, 1);
        
        section.getChildren().addAll(sectionTitle, grid);
        return section;
    }
    
    private VBox createReturnInfoSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-border-color: #dee2e6; -fx-border-radius: 8;");
        
        Label sectionTitle = new Label("Πληροφορίες Επιστροφής");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.rgb(52, 73, 94));
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        
        // Return Officer (read-only)
        returnOfficerField = new TextField();
        returnOfficerField.setEditable(false);
        returnOfficerField.setStyle("-fx-background-color: #f8f9fa;");
        grid.add(new Label("ΑΜ Αστυνομικού:"), 0, 0);
        grid.add(returnOfficerField, 1, 0);
        
        // Return Date
        returnDateField = new TextField();
        returnDateField.setPromptText("DD/MM/YYYY");
        returnDateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        grid.add(new Label("Ημερομηνία Επιστροφής:"), 0, 1);
        grid.add(returnDateField, 1, 1);
        
        // Return Time
        returnTimeField = new TextField();
        returnTimeField.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        returnTimeField.setPromptText("HH:mm");
        grid.add(new Label("Ώρα Επιστροφής:"), 0, 2);
        grid.add(returnTimeField, 1, 2);
        
        // Comment
        commentField = new TextArea();
        commentField.setPromptText("Σχόλια επιστροφής...");
        commentField.setPrefRowCount(3);
        grid.add(new Label("Σχόλια:"), 0, 3);
        grid.add(commentField, 1, 3);
        
        section.getChildren().addAll(sectionTitle, grid);
        return section;
    }
    
    private VBox createClaimantInfoSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: #fff3cd; -fx-background-radius: 8; -fx-border-color: #ffeaa7; -fx-border-radius: 8;");
        
        Label sectionTitle = new Label("Πληροφορίες Δικαιούχου");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.rgb(133, 100, 4));
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        
        // Last Name
        returnLastNameField = new TextField();
        returnLastNameField.setPromptText("Επώνυμο");
        grid.add(new Label("Επώνυμο:"), 0, 0);
        grid.add(returnLastNameField, 1, 0);
        
        // First Name
        returnFirstNameField = new TextField();
        returnFirstNameField.setPromptText("Όνομα");
        grid.add(new Label("Όνομα:"), 0, 1);
        grid.add(returnFirstNameField, 1, 1);
        
        // Telephone
        returnTelephoneField = new TextField();
        returnTelephoneField.setPromptText("Τηλέφωνο");
        grid.add(new Label("Τηλέφωνο:"), 0, 2);
        grid.add(returnTelephoneField, 1, 2);
        
        // ID Number
        returnIdNumberField = new TextField();
        returnIdNumberField.setPromptText("Αριθμός Ταυτότητας");
        grid.add(new Label("Αριθμός Ταυτότητας:"), 0, 3);
        grid.add(returnIdNumberField, 1, 3);
        
        // Father's Name
        returnFatherNameField = new TextField();
        returnFatherNameField.setPromptText("Όνομα Πατέρα");
        grid.add(new Label("Όνομα Πατέρα:"), 0, 4);
        grid.add(returnFatherNameField, 1, 4);
        
        // Date of Birth
        returnDateOfBirthField = new TextField();
        returnDateOfBirthField.setPromptText("DD/MM/YYYY");
        grid.add(new Label("Ημερομηνία Γέννησης:"), 0, 5);
        grid.add(returnDateOfBirthField, 1, 5);
        
        // Street Address
        returnStreetAddressField = new TextField();
        returnStreetAddressField.setPromptText("Διεύθυνση");
        grid.add(new Label("Διεύθυνση:"), 0, 6);
        grid.add(returnStreetAddressField, 1, 6);
        
        // Street Number
        returnStreetNumberField = new TextField();
        returnStreetNumberField.setPromptText("Αριθμός");
        grid.add(new Label("Αριθμός:"), 0, 7);
        grid.add(returnStreetNumberField, 1, 7);
        
        section.getChildren().addAll(sectionTitle, grid);
        return section;
    }
    
    private HBox createButtonContainer() {
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20, 0, 0, 0));
        
        saveBtn = new Button("Αποθήκευση Επιστροφής");
        saveBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 10 20; -fx-font-weight: bold;");
        saveBtn.setOnAction(e -> saveReturn());
        
        cancelBtn = new Button("Ακύρωση");
        cancelBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 10 20; -fx-font-weight: bold;");
        cancelBtn.setOnAction(e -> close());
        
        buttonContainer.getChildren().addAll(saveBtn, cancelBtn);
        return buttonContainer;
    }
    
    private void populateForm() {
        // Populate record information
        recordIdField.setText(String.valueOf(record.getId()));
        itemDescriptionField.setText(record.getItem_description());
        returnOfficerField.setText(String.valueOf(currentUser.getAm()));
    }
    
    private void saveReturn() {
        if (!validateForm()) {
            return;
        }
        
        try {
            String insertQuery = "INSERT INTO returns (record_id, return_officer, return_last_name, return_first_name, " +
                               "return_date, return_time, return_telephone, return_id_number, return_father_name, " +
                               "return_date_of_birth, return_street_address, return_street_number, comment) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement stmt = connection.prepareStatement(insertQuery);
            
            stmt.setInt(1, record.getId());
            stmt.setInt(2, currentUser.getAm());
            stmt.setString(3, returnLastNameField.getText());
            stmt.setString(4, returnFirstNameField.getText());
            stmt.setString(5, returnDateField.getText().trim());
            stmt.setString(6, returnTimeField.getText());
            stmt.setString(7, returnTelephoneField.getText());
            stmt.setString(8, returnIdNumberField.getText());
            stmt.setString(9, returnFatherNameField.getText());
            stmt.setString(10, returnDateOfBirthField.getText().trim().isEmpty() ? null : returnDateOfBirthField.getText().trim());
            stmt.setString(11, returnStreetAddressField.getText());
            stmt.setString(12, returnStreetNumberField.getText());
            stmt.setString(13, commentField.getText());
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            if (rowsAffected > 0) {
                // Update record status to returned
                updateRecordStatus();
                
                // Update statistics
                if (StatisticsManager.getInstance() != null) {
                    StatisticsManager.getInstance().forceUpdate();
                }
                
                new MessageBoxOk("Η επιστροφή αποθηκεύτηκε επιτυχώς!");
                close();
            } else {
                new MessageBoxOk("Σφάλμα κατά την αποθήκευση της επιστροφής.");
            }
            
        } catch (SQLException e) {
            new MessageBoxOk("Σφάλμα αποθήκευσης: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updateRecordStatus() throws SQLException {
        String updateQuery = "UPDATE records SET status = 'returned' WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(updateQuery);
        stmt.setInt(1, record.getId());
        stmt.executeUpdate();
        stmt.close();
    }
    
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();
        
        if (returnLastNameField.getText().trim().isEmpty()) {
            errors.append("• Το επώνυμο είναι υποχρεωτικό\n");
        }
        
        if (returnFirstNameField.getText().trim().isEmpty()) {
            errors.append("• Το όνομα είναι υποχρεωτικό\n");
        }
        
        if (returnTelephoneField.getText().trim().isEmpty()) {
            errors.append("• Το τηλέφωνο είναι υποχρεωτικό\n");
        }
        
        if (returnIdNumberField.getText().trim().isEmpty()) {
            errors.append("• Ο αριθμός ταυτότητας είναι υποχρεωτικός\n");
        }
        
        if (returnStreetAddressField.getText().trim().isEmpty()) {
            errors.append("• Η διεύθυνση είναι υποχρεωτική\n");
        }
        
        if (returnStreetNumberField.getText().trim().isEmpty()) {
            errors.append("• Ο αριθμός διεύθυνσης είναι υποχρεωτικός\n");
        }
        
        // Validate time format
        if (!returnTimeField.getText().trim().isEmpty()) {
            try {
                LocalTime.parse(returnTimeField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
            } catch (Exception e) {
                errors.append("• Η ώρα πρέπει να είναι σε μορφή HH:mm (π.χ. 14:30)\n");
            }
        }
        
        // Validate return date format (DD/MM/YYYY)
        if (returnDateField.getText().trim().isEmpty()) {
            errors.append("• Η ημερομηνία επιστροφής είναι υποχρεωτική\n");
        } else {
            String dateStr = returnDateField.getText().trim();
            if (!isValidDateFormat(dateStr)) {
                errors.append("• Η ημερομηνία επιστροφής πρέπει να είναι σε μορφή DD/MM/YYYY (π.χ. 25/12/2024)\n");
            }
        }
        
        // Validate date of birth format if provided
        if (!returnDateOfBirthField.getText().trim().isEmpty()) {
            String dateOfBirthStr = returnDateOfBirthField.getText().trim();
            if (!isValidDateFormat(dateOfBirthStr)) {
                errors.append("• Η ημερομηνία γέννησης πρέπει να είναι σε μορφή DD/MM/YYYY (π.χ. 15/03/1990)\n");
            }
        }
        
        if (errors.length() > 0) {
            new MessageBoxOk("Παρακαλώ διορθώστε τα ακόλουθα σφάλματα:\n\n" + errors.toString());
            return false;
        }
        
        return true;
    }
    
    private boolean isValidDateFormat(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        
        // Check if date matches DD/MM/YYYY format
        String datePattern = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$";
        return dateStr.trim().matches(datePattern);
    }
}
