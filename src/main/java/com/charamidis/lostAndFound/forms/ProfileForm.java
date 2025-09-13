package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.utils.MessageBoxOk;
import com.charamidis.lostAndFound.models.User;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;


public class ProfileForm {
    Label lblFirstName,lblLastName,lblBirthday,lblRole,lblPassword,lblAm,lblLastSignedIn;
    TextField txtFirstName,txtLastName,txtBirthday,txtRole,txtPassword,txtAm,txtLastSignedIn;
    GridPane grid;
    Connection connection;
    Scene profileScene;
    Button btnChange ;
    public ProfileForm(Connection conn, User user){
        connection = conn;
        
        // Create main container with professional styling
        VBox mainContainer = new VBox(30);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setStyle("-fx-background-color: #f8f9fa;");
        
        // Header section
        VBox headerSection = new VBox(10);
        headerSection.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("User Profile");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.rgb(52, 73, 94));
        
        Label subtitleLabel = new Label("Manage your account information and settings");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.rgb(108, 117, 125));
        
        headerSection.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Form section
        VBox formSection = new VBox(20);
        formSection.setPadding(new Insets(30));
        formSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        // Create labels with modern styling
        lblFirstName = new Label("First Name:");
        lblFirstName.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblFirstName.setTextFill(Color.rgb(52, 73, 94));
        
        lblLastName = new Label("Last Name:");
        lblLastName.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblLastName.setTextFill(Color.rgb(52, 73, 94));
        
        lblAm = new Label("Employee ID:");
        lblAm.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblAm.setTextFill(Color.rgb(52, 73, 94));
        
        lblRole = new Label("Role:");
        lblRole.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblRole.setTextFill(Color.rgb(52, 73, 94));
        
        lblBirthday = new Label("Date of Birth:");
        lblBirthday.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblBirthday.setTextFill(Color.rgb(52, 73, 94));
        
        lblLastSignedIn = new Label("Last Login:");
        lblLastSignedIn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblLastSignedIn.setTextFill(Color.rgb(52, 73, 94));
        
        lblPassword = new Label("Password:");
        lblPassword.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblPassword.setTextFill(Color.rgb(52, 73, 94));

        // Create text fields with modern styling
        txtFirstName = new TextField(user.getFirstName());
        txtFirstName.setPrefWidth(250);
        txtFirstName.setPrefHeight(35);
        txtFirstName.setEditable(false);
        txtFirstName.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
        
        txtLastName = new TextField(user.getLastName());
        txtLastName.setPrefWidth(250);
        txtLastName.setPrefHeight(35);
        txtLastName.setEditable(false);
        txtLastName.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
        
        txtAm = new TextField(String.valueOf(user.getAm()));
        txtAm.setPrefWidth(250);
        txtAm.setPrefHeight(35);
        txtAm.setEditable(false);
        txtAm.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
        
        txtRole = new TextField(user.getRole());
        txtRole.setPrefWidth(250);
        txtRole.setPrefHeight(35);
        txtRole.setEditable(false);
        txtRole.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
        
        txtBirthday = new TextField(user.getBirthday());
        txtBirthday.setPrefWidth(250);
        txtBirthday.setPrefHeight(35);
        txtBirthday.setEditable(false);
        txtBirthday.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
        
        txtLastSignedIn = new TextField(user.getDateLoggedIn() + " " + user.getTimeLoggedIn());
        txtLastSignedIn.setPrefWidth(250);
        txtLastSignedIn.setPrefHeight(35);
        txtLastSignedIn.setEditable(false);
        txtLastSignedIn.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
        
        txtPassword = new TextField("************");
        txtPassword.setPrefWidth(250);
        txtPassword.setPrefHeight(35);
        txtPassword.setEditable(false);
        txtPassword.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");

        btnChange = new Button("Change Password");
        btnChange.setPrefWidth(150);
        btnChange.setPrefHeight(40);
        btnChange.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 14;");

        // Create form grid with modern layout
        grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);


        // Add form fields to grid
        grid.add(lblFirstName, 0, 0);
        grid.add(txtFirstName, 1, 0);
        
        grid.add(lblLastName, 0, 1);
        grid.add(txtLastName, 1, 1);
        
        grid.add(lblAm, 0, 2);
        grid.add(txtAm, 1, 2);
        
        grid.add(lblBirthday, 0, 3);
        grid.add(txtBirthday, 1, 3);
        
        grid.add(lblRole, 0, 4);
        grid.add(txtRole, 1, 4);
        
        grid.add(lblLastSignedIn, 0, 5);
        grid.add(txtLastSignedIn, 1, 5);
        
        grid.add(lblPassword, 0, 6);
        grid.add(txtPassword, 1, 6);
        
        // Button container
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().add(btnChange);
        
        grid.add(buttonContainer, 0, 7, 2, 1);
        GridPane.setHalignment(buttonContainer, HPos.CENTER);

        btnChange.setOnAction(e->{
            txtPassword.setEditable(true);
            txtPassword.setText("");
            txtPassword.setStyle("-fx-background-color: white; -fx-border-color: #007bff; -fx-border-width: 2; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
            btnChange.setVisible(false);

            Button savebtn = new Button("Save Password");
            savebtn.setPrefWidth(120);
            savebtn.setPrefHeight(35);
            savebtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 12;");
            
            Button cancelbtn = new Button("Cancel");
            cancelbtn.setPrefWidth(120);
            cancelbtn.setPrefHeight(35);
            cancelbtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 12;");

            HBox hboxbtns = new HBox(15);
            hboxbtns.getChildren().addAll(savebtn, cancelbtn);
            hboxbtns.setAlignment(Pos.CENTER);
            grid.add(hboxbtns, 0, 7, 2, 1);
            GridPane.setHalignment(hboxbtns, HPos.CENTER);

            savebtn.setOnAction(saveEvent->{
                if(txtPassword.getText().trim().isEmpty()){
                    new MessageBoxOk("Please enter a password");
                }else {
                     String hashedPassword = null;
                     hashedPassword = BCrypt.hashpw(txtPassword.getText().trim(),BCrypt.gensalt());
                    if(!hashedPassword.isEmpty()){
                        try{
                            String updQuery = "UPDATE users SET password = ? WHERE am = ?";
                            PreparedStatement stm = conn.prepareStatement(updQuery);
                            stm.setString(1,hashedPassword);
                            stm.setInt(2,user.getAm());
                            stm.executeUpdate();
                            stm.close();
                            hboxbtns.setVisible(false);
                            btnChange.setVisible(true);
                            txtPassword.setText("************");
                            txtPassword.setEditable(false);
                            txtPassword.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
                            new MessageBoxOk("Password updated successfully!");
                        }catch (SQLException exception){
                            new MessageBoxOk("Error updating password: " + exception.getMessage());
                            exception.printStackTrace();
                        }
                    }else{
                        new MessageBoxOk("Error hashing the password");
                    }

                }
            });

            cancelbtn.setOnAction(cancelEvent->{
                txtPassword.setEditable(false);
                txtPassword.setText("************");
                txtPassword.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
                hboxbtns.setVisible(false);
                btnChange.setVisible(true);
            });



        });


        // Set column constraints for proper layout
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPrefWidth(150);
        col0.setHalignment(HPos.RIGHT);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(300);
        col1.setHalignment(HPos.LEFT);

        grid.getColumnConstraints().addAll(col0, col1);





        // Add form to section and sections to main container
        formSection.getChildren().add(grid);
        mainContainer.getChildren().addAll(headerSection, formSection);

        profileScene = new Scene(mainContainer);


    }

    public Scene getScene(){
        return this.profileScene;
    }

}
