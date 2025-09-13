package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;

import java.sql.*;

public class DeleteUser {
    Connection finalConn;
    TextField textFieldAm;
    Label lblAm;
    HBox hBox;
    VBox vBox;
    HBox hboxBtns;
    Scene scene;
    Button btnDelete,btnCancel;
    Stage stage;
    public DeleteUser(Connection conn){
        finalConn = conn;
        
        // Create main container with professional styling
        VBox mainContainer = new VBox(30);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setStyle("-fx-background-color: #f8f9fa;");
        
        // Header section
        VBox headerSection = new VBox(10);
        headerSection.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Delete User");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.rgb(52, 73, 94));
        
        Label subtitleLabel = new Label("Remove a user from the system");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.rgb(108, 117, 125));
        
        headerSection.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Form section
        VBox formSection = new VBox(20);
        formSection.setAlignment(Pos.CENTER);
        formSection.setPadding(new Insets(30));
        formSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        lblAm = new Label("Employee ID:");
        lblAm.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblAm.setTextFill(Color.rgb(52, 73, 94));
        
        textFieldAm = new TextField();
        textFieldAm.setPromptText("Enter employee ID to delete");
        textFieldAm.setPrefWidth(250);
        textFieldAm.setPrefHeight(35);
        textFieldAm.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
        
        VBox inputContainer = new VBox(8);
        inputContainer.setAlignment(Pos.CENTER);
        inputContainer.getChildren().addAll(lblAm, textFieldAm);
        
        // Button section
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        
        btnDelete = new Button("Delete User");
        btnDelete.setPrefWidth(120);
        btnDelete.setPrefHeight(40);
        btnDelete.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 14;");
        btnDelete.setDefaultButton(true);
        
        btnCancel = new Button("Cancel");
        btnCancel.setPrefWidth(120);
        btnCancel.setPrefHeight(40);
        btnCancel.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 14;");
        
        buttonContainer.getChildren().addAll(btnCancel, btnDelete);
        
        formSection.getChildren().addAll(inputContainer, buttonContainer);
        mainContainer.getChildren().addAll(headerSection, formSection);
        
        btnDelete.setOnAction(e->{
            PreparedStatement stm = null;
            if(!textFieldAm.getText().isEmpty()) {
                try {
                    stm = conn.prepareStatement("DELETE FROM users WHERE am = ?");
                    stm.setInt(1,Integer.parseInt(textFieldAm.getText()));
                    int rows =  stm.executeUpdate();
                    stm.close();
                    if(rows>0){
                        new MessageBoxOk("User deleted successfully");
                        stage.close();
                    }else{
                        new MessageBoxOk("User not found");
                    }

                } catch (SQLException |  NumberFormatException ex ) {
                    new MessageBoxOk("Please enter a valid integer value"+"\n"+ex.getMessage());
                }
            }else{
                new MessageBoxOk("Please enter the employee ID");
            }
        });
        
        btnCancel.setOnAction(e->{
            stage.close();
        });

        scene = new Scene(mainContainer);
        stage = new Stage();
        stage.setTitle("Delete User");
        stage.setMinWidth(500);
        stage.setMinHeight(400);
        stage.setWidth(500);
        stage.setHeight(450);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.centerOnScreen();
        stage.show();

    }
}
