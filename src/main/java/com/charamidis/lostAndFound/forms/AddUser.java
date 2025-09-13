package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.utils.AppLogger;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AddUser {

    Stage window;
    Scene scene;
    VBox vbox;
    HBox hbox;
    Button btnSave, btnCancel;
    TextField txtFirstName, txtLastName, txtAm, txtDateOfBirth, txtPassword;
    ComboBox<String> comboBox;
    DatePicker datePicker;
    Label lblFirstName, lblLastName, lblAm, lblDateOfBirth, lblPassword, lblRole;
    Connection connection;
    private static final Logger logger = AppLogger.getLogger();

    public AddUser(Connection conn) {
        connection = conn;
        
        // Create main container with professional styling
        VBox mainContainer = new VBox(30);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setStyle("-fx-background-color: #f8f9fa;");
        
        // Header section
        VBox headerSection = new VBox(10);
        headerSection.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Add New User");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.rgb(52, 73, 94));
        
        Label subtitleLabel = new Label("Create a new user account");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.rgb(108, 117, 125));
        
        headerSection.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Form section
        VBox formSection = new VBox(20);
        formSection.setAlignment(Pos.CENTER);
        formSection.setPadding(new Insets(40));
        formSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        // Form fields
        txtAm = new TextField();
        txtAm.setPromptText("Employee ID");
        txtAm.setPrefWidth(300);
        txtAm.setPrefHeight(40);
        txtAm.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
        
        txtFirstName = new TextField();
        txtFirstName.setPromptText("First Name");
        txtFirstName.setPrefWidth(300);
        txtFirstName.setPrefHeight(40);
        txtFirstName.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
        
        txtLastName = new TextField();
        txtLastName.setPromptText("Last Name");
        txtLastName.setPrefWidth(300);
        txtLastName.setPrefHeight(40);
        txtLastName.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
        
        txtPassword = new TextField();
        txtPassword.setPromptText("Password");
        txtPassword.setPrefWidth(300);
        txtPassword.setPrefHeight(40);
        txtPassword.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
        
        lblDateOfBirth = new Label("Date of Birth:");
        lblDateOfBirth.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblDateOfBirth.setTextFill(Color.rgb(52, 73, 94));
        
        datePicker = new DatePicker();
        datePicker.setPrefWidth(300);
        datePicker.setPrefHeight(40);
        datePicker.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-font-size: 14;");
        
        comboBox = new ComboBox<>();
        comboBox.getItems().addAll("user", "admin");
        comboBox.setValue("user");
        comboBox.setPromptText("Select Role");
        comboBox.setPrefWidth(300);
        comboBox.setPrefHeight(40);
        comboBox.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-font-size: 14;");
        
        // Button section
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        
        btnSave = new Button("Add User");
        btnSave.setPrefWidth(120);
        btnSave.setPrefHeight(45);
        btnSave.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 14;");
        
        btnCancel = new Button("Cancel");
        btnCancel.setPrefWidth(120);
        btnCancel.setPrefHeight(45);
        btnCancel.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 14;");
        
        buttonContainer.getChildren().addAll(btnCancel, btnSave);
        
        formSection.getChildren().addAll(txtAm, txtFirstName, txtLastName, txtPassword, lblDateOfBirth, datePicker, comboBox, buttonContainer);
        mainContainer.getChildren().addAll(headerSection, formSection);

        scene = new Scene(mainContainer, 500, 700);
        window = new Stage();
        window.setTitle("Add New User");
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.DECORATED);

        btnCancel.setOnAction(e->{
            window.close();
        });

        btnSave.setOnAction(e->{
            if(txtFirstName.getText().equals("") || txtLastName.getText().equals("") || txtAm.getText().equals("") || txtPassword.getText().equals("") || comboBox.getValue().equals("")){
                new MessageBoxOk("ΤΑ ΠΕΔΙΑ ΠΡΕΠΕΙ ΝΑ ΣΥΜΠΛΗΡΩΘΟΥΝ");
            }else {
                try{
                    String hashedPassword = BCrypt.hashpw(txtPassword.getText(), BCrypt.gensalt());
                    PreparedStatement stm = connection.prepareStatement("INSERT INTO users (am,first_name,last_name,date_of_birth,role,password) VALUES (?,?,?,?,?,?)");
                    stm.setInt(1,Integer.parseInt(txtAm.getText()));
                    stm.setString(2,txtFirstName.getText());
                    stm.setString(3,txtLastName.getText());
                    stm.setDate(4,java.sql.Date.valueOf(datePicker.getValue().toString()));
                    stm.setString(5,comboBox.getValue());
                    stm.setString(6,hashedPassword);
                    stm.executeUpdate();
                    stm.close();

                    new MessageBoxOk("User added successfully");
                    window.close();

                }catch (SQLException exception){
                    new MessageBoxOk(exception.getMessage());
                    logger.log(Level.SEVERE,"Cant add user ",exception);
                }
            }

        });


        window.setMinWidth(500);
        window.setMinHeight(700);
        window.setWidth(500);
        window.setHeight(750);
        window.setScene(scene);
        window.centerOnScreen();

        window.show();

    }

}