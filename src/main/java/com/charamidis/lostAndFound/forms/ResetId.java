package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResetId {
    Connection finalConn;
    Button btnSave,btnCancel;
    VBox vBox;
    HBox hbox;
    Scene scene;
    Stage window;
    HBox hboxBtn ;
    Label lblId ;
    TextField txtId;




    public  ResetId(Connection conn){
        finalConn = conn;
        
        // Create main container with professional styling
        VBox mainContainer = new VBox(30);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setStyle("-fx-background-color: #f8f9fa;");
        
        // Header section
        VBox headerSection = new VBox(10);
        headerSection.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Reset ID");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.rgb(52, 73, 94));
        
        Label subtitleLabel = new Label("Reset the database ID sequence");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.rgb(108, 117, 125));
        
        headerSection.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Form section
        VBox formSection = new VBox(20);
        formSection.setAlignment(Pos.CENTER);
        formSection.setPadding(new Insets(30));
        formSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        lblId = new Label("New ID Value:");
        lblId.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblId.setTextFill(Color.rgb(52, 73, 94));
        
        txtId = new TextField();
        txtId.setPromptText("Enter the new ID value");
        txtId.setPrefWidth(200);
        txtId.setPrefHeight(35);
        txtId.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
        
        VBox inputContainer = new VBox(8);
        inputContainer.setAlignment(Pos.CENTER);
        inputContainer.getChildren().addAll(lblId, txtId);
        
        // Button section
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        
        btnSave = new Button("Reset ID");
        btnSave.setPrefWidth(120);
        btnSave.setPrefHeight(40);
        btnSave.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 14;");
        
        btnCancel = new Button("Cancel");
        btnCancel.setPrefWidth(120);
        btnCancel.setPrefHeight(40);
        btnCancel.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 14;");
        
        buttonContainer.getChildren().addAll(btnCancel, btnSave);
        
        formSection.getChildren().addAll(inputContainer, buttonContainer);
        mainContainer.getChildren().addAll(headerSection, formSection);

        btnCancel.setOnAction(e->{
            window.close();
        });

        btnSave.setOnAction(e->{
           MessageBoxOk msg =  new MessageBoxOk("Θέλετε σίγουρα να αλλάξετε το ID ? \n Παρακαλούμε να λάβετε υπόψη ότι η χειροκίνητη αλλαγή αναγνωριστικών βάσης δεδομένων μπορεί να έχει σοβαρές συνέπειες και πρέπει να προσεγγίζεται με προσοχή.");
           boolean resp = msg.getRes();
           if(resp){
               if(txtId.getText().trim().isEmpty()){
                   new MessageBoxOk("Δώστε αριθμό");
               }else{
                    if(txtId.getText().trim().matches("[0-9]+")){
                        // SQLite doesn't support sequences, so we'll update the sqlite_sequence table
                        String resetQuery = "UPDATE sqlite_sequence SET seq = ? WHERE name = 'records'";
                        try {
                            PreparedStatement pstmt = finalConn.prepareStatement(resetQuery);
                            pstmt.setInt(1, Integer.parseInt(txtId.getText().trim()) - 1);
                            pstmt.executeUpdate();
                            pstmt.close();
                            new MessageBoxOk("Το ID έχει επαναφερθεί.");
                            window.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            MessageBoxOk errorMsg = new MessageBoxOk("Σφάλμα κατά την επαναφορά του ID.");
                        }

                    }else {
                        new MessageBoxOk("Δώστε ακέραιο αριθμό ");
                    }
               }

           }else{
               window.close();
           }
        });



        scene = new Scene(mainContainer);
        window = new Stage();
        window.setTitle("Reset ID");
        window.setScene(scene);
        window.setMinWidth(500);
        window.setMinHeight(400);
        window.setWidth(500);
        window.setHeight(450);
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.DECORATED);
        window.centerOnScreen();

        window.show();




    }
}
