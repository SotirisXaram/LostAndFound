package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteReturn {
    Connection finalConn;
    Button btnSave,btnCancel;
    VBox vBox;
    HBox hbox;
    Scene scene;
    Stage window;
    HBox hboxBtn ;
    Label lblId ;
    TextField txtId;




    public DeleteReturn(Connection conn){
        finalConn = conn;
        
        // Create main container with professional styling
        VBox mainContainer = new VBox(30);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setStyle("-fx-background-color: #f8f9fa;");
        
        // Header section
        VBox headerSection = new VBox(10);
        headerSection.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Delete Return");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.rgb(52, 73, 94));
        
        Label subtitleLabel = new Label("Remove a return record from the system");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.rgb(108, 117, 125));
        
        headerSection.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Form section
        VBox formSection = new VBox(20);
        formSection.setAlignment(Pos.CENTER);
        formSection.setPadding(new Insets(30));
        formSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        lblId = new Label("Return ID:");
        lblId.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblId.setTextFill(Color.rgb(52, 73, 94));
        
        txtId = new TextField();
        txtId.setPromptText("Enter return ID to delete");
        txtId.setPrefWidth(200);
        txtId.setPrefHeight(35);
        txtId.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
        
        VBox inputContainer = new VBox(8);
        inputContainer.setAlignment(Pos.CENTER);
        inputContainer.getChildren().addAll(lblId, txtId);
        
        // Button section
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        
        btnSave = new Button("Delete Return");
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
            MessageBoxOk msg =  new MessageBoxOk("Are you sure you want to delete this return?\nThis action cannot be undone.");
            boolean resp = msg.getRes();
            if(resp){
                if(txtId.getText().trim().isEmpty()){
                    new MessageBoxOk("Please enter a return ID");
                }else{
                    if(txtId.getText().trim().matches("[0-9]+")){
                        String deleteQuery = "DELETE FROM returns WHERE id = ?";
                        try {
                            PreparedStatement stm = finalConn.prepareStatement(deleteQuery);
                            stm.setInt(1, Integer.parseInt(txtId.getText().trim()));
                            int rowsAffected = stm.executeUpdate();
                            stm.close();
                            if(rowsAffected > 0) {
                                new MessageBoxOk("Return deleted successfully");
                                window.close();
                            } else {
                                new MessageBoxOk("Return not found");
                            }
                        } catch (SQLException ex) {
                            new MessageBoxOk("Error deleting return: " + ex.getMessage());
                        }

                    }else {
                        new MessageBoxOk("Please enter a valid integer value");
                    }
                }

            }else{
                window.close();
            }
        });

        scene = new Scene(mainContainer);
        window = new Stage();
        window.setTitle("Delete Return");
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
