package com.charamidis.lostAndFound.pages;

import com.charamidis.lostAndFound.models.Record;
import com.charamidis.lostAndFound.models.User;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import com.charamidis.lostAndFound.utils.StatisticsManager;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReturnScreen {
    Label lblOfficer,lblLastName,lblFirstName,lblDate,lblTime,lblTelephone,lblDateOfBirth,lblIdNumber,lblFatherName,lblStreetAddress,lblStreetNumber,lblComment;
    TextField txtOfficer,txtLastName,txtFirstName,txtDate,txtTime,txtTelephone,txtDateOfBirth,txtIdNumber,txtFatherName,txtStreetAddress,txtStreetNumber;
    TextArea txtComment;
    Button btnSave,btnCancel;
    Stage stage;
    Scene scene;
    GridPane grid;
    HBox saveDeleteHbox;
    public ReturnScreen(Connection conn, User user, Record record) {

        // Create main container with professional styling
        VBox mainContainer = new VBox(30);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setStyle("-fx-background-color: #f8f9fa;");
        
        // Header section
        VBox headerSection = new VBox(10);
        headerSection.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Process Return");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.rgb(52, 73, 94));
        
        Label subtitleLabel = new Label("Complete the return process for the selected item");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.rgb(108, 117, 125));
        
        headerSection.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Form section
        VBox formSection = new VBox(20);
        formSection.setPadding(new Insets(30));
        formSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setHgrow(Priority.SOMETIMES);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        col1.setHalignment(HPos.RIGHT);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHalignment(HPos.LEFT);
        col2.setHgrow(Priority.SOMETIMES);

        Region rEmpty = new Region();
        grid.add(rEmpty,3,0);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setHgrow(Priority.SOMETIMES);

        grid.getColumnConstraints().addAll(col0,col1,col2,col3);

         // Create labels with modern styling
         lblOfficer = new Label("Officer ID:");
         lblOfficer.setFont(Font.font("Arial", FontWeight.BOLD, 14));
         lblOfficer.setTextFill(Color.rgb(52, 73, 94));
         
         txtOfficer = new TextField();
         txtOfficer.setText(Integer.valueOf(user.getAm()).toString());
         txtOfficer.setEditable(false);
         txtOfficer.setPrefWidth(200);
         txtOfficer.setPrefHeight(35);
         txtOfficer.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");

         lblLastName = new Label("Last Name:");
         lblLastName.setFont(Font.font("Arial", FontWeight.BOLD, 14));
         lblLastName.setTextFill(Color.rgb(52, 73, 94));
         
         txtLastName = new TextField();
         txtLastName.setPrefWidth(200);
         txtLastName.setPrefHeight(35);
         txtLastName.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");

         lblFirstName = new Label("First Name:");
         lblFirstName.setFont(Font.font("Arial", FontWeight.BOLD, 14));
         lblFirstName.setTextFill(Color.rgb(52, 73, 94));
         
         txtFirstName = new TextField();
         txtFirstName.setPrefWidth(200);
         txtFirstName.setPrefHeight(35);
         txtFirstName.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");

         lblDate = new Label("Date:");
         lblDate.setFont(Font.font("Arial", FontWeight.BOLD, 14));
         lblDate.setTextFill(Color.rgb(52, 73, 94));
         
         txtDate = new TextField();
         txtDate.setText(LocalDate.now().toString());
         txtDate.setPrefWidth(200);
         txtDate.setPrefHeight(35);
         txtDate.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");

         lblTime = new Label("Time:");
         lblTime.setFont(Font.font("Arial", FontWeight.BOLD, 14));
         lblTime.setTextFill(Color.rgb(52, 73, 94));
         
         txtTime = new TextField();
         txtTime.setText(LocalTime.now().withSecond(0).withNano(0).toString());
         txtTime.setPrefWidth(200);
         txtTime.setPrefHeight(35);
         txtTime.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");

         lblTelephone = new Label("Telephone:");
         lblTelephone.setFont(Font.font("Arial", FontWeight.BOLD, 14));
         lblTelephone.setTextFill(Color.rgb(52, 73, 94));
         
         txtTelephone = new TextField();
         txtTelephone.setPrefWidth(200);
         txtTelephone.setPrefHeight(35);
         txtTelephone.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");

         lblIdNumber = new Label("ID Number:");
         lblIdNumber.setFont(Font.font("Arial", FontWeight.BOLD, 14));
         lblIdNumber.setTextFill(Color.rgb(52, 73, 94));
         
         txtIdNumber = new TextField();
         txtIdNumber.setPrefWidth(200);
         txtIdNumber.setPrefHeight(35);
         txtIdNumber.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");

         lblFatherName = new Label("Father's Name:");
         lblFatherName.setFont(Font.font("Arial", FontWeight.BOLD, 14));
         lblFatherName.setTextFill(Color.rgb(52, 73, 94));
         
         txtFatherName = new TextField();
         txtFatherName.setPrefWidth(200);
         txtFatherName.setPrefHeight(35);
         txtFatherName.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");

         lblDateOfBirth = new Label("Date of Birth:");
         lblDateOfBirth.setFont(Font.font("Arial", FontWeight.BOLD, 14));
         lblDateOfBirth.setTextFill(Color.rgb(52, 73, 94));
         
         txtDateOfBirth = new TextField();
         txtDateOfBirth.setPrefWidth(200);
         txtDateOfBirth.setPrefHeight(35);
         txtDateOfBirth.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");

         lblStreetAddress = new Label("Street Address:");
         lblStreetAddress.setFont(Font.font("Arial", FontWeight.BOLD, 14));
         lblStreetAddress.setTextFill(Color.rgb(52, 73, 94));
         
         txtStreetAddress = new TextField();
         txtStreetAddress.setPrefWidth(200);
         txtStreetAddress.setPrefHeight(35);
         txtStreetAddress.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");

         lblStreetNumber = new Label("Street Number:");
         lblStreetNumber.setFont(Font.font("Arial", FontWeight.BOLD, 14));
         lblStreetNumber.setTextFill(Color.rgb(52, 73, 94));
         
         txtStreetNumber = new TextField();
         txtStreetNumber.setPrefWidth(200);
         txtStreetNumber.setPrefHeight(35);
         txtStreetNumber.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");

         lblComment = new Label("Comments:");
         lblComment.setFont(Font.font("Arial", FontWeight.BOLD, 14));
         lblComment.setTextFill(Color.rgb(52, 73, 94));
         
         txtComment = new TextArea();
         txtComment.setPrefHeight(100);
         txtComment.setPrefWidth(300);
         txtComment.setWrapText(true);
         txtComment.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");

         btnSave = new Button("Save Return");
         btnSave.setPrefWidth(120);
         btnSave.setPrefHeight(40);
         btnSave.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 14;");
         
         btnCancel = new Button("Cancel");
         btnCancel.setPrefWidth(120);
         btnCancel.setPrefHeight(40);
         btnCancel.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 14;");

         saveDeleteHbox = new HBox(15);
         saveDeleteHbox.getChildren().addAll(btnCancel, btnSave);
         saveDeleteHbox.setAlignment(Pos.CENTER);

        grid.add(lblOfficer, 1, 0);
        grid.add(txtOfficer, 2, 0);
        grid.add(lblLastName, 1, 1);
        grid.add(txtLastName, 2, 1);
        grid.add(lblFirstName, 1, 2);
        grid.add(txtFirstName, 2, 2);
        grid.add(lblDate, 1, 3);
        grid.add(txtDate, 2, 3);
        grid.add(lblTime, 1, 4);
        grid.add(txtTime, 2, 4);
        grid.add(lblTelephone, 1, 5);
        grid.add(txtTelephone, 2, 5);
        grid.add(lblIdNumber, 1, 6);
        grid.add(txtIdNumber, 2, 6);
        grid.add(lblFatherName, 1, 7);
        grid.add(txtFatherName, 2, 7);
        grid.add(lblDateOfBirth, 1, 8);
        grid.add(txtDateOfBirth, 2, 8);
        grid.add(lblStreetAddress, 1, 9);
        grid.add(txtStreetAddress, 2, 9);
        grid.add(lblStreetNumber, 1, 10);
        grid.add(txtStreetNumber, 2, 10);
        grid.add(lblComment,1,11);
        grid.add(txtComment,2,11);
        grid.add(saveDeleteHbox, 1, 12, 2, 1);


        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);




        scene = new Scene(grid);
        stage = new Stage();
        stage.setTitle("Επιστροφή");
        stage.setWidth(550);
        stage.setResizable(false);
        stage.setHeight(650);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);

        btnCancel.setOnAction(e->{
            stage.close();
        });




        btnSave.setOnAction(e -> {
            if (validateForm()) {
                try {
                    String insertQuery = "INSERT INTO returns (id, return_officer, return_last_name, return_first_name, return_date, return_time, return_telephone, return_id_number, return_father_name, return_date_of_birth, return_street_address, return_street_number,comment) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStm = conn.prepareStatement(insertQuery);

                    insertStm.setLong(1, record.getId());
                    insertStm.setInt(2, Integer.parseInt(txtOfficer.getText()));
                    insertStm.setString(3, txtLastName.getText());
                    insertStm.setString(4, txtFirstName.getText());
                    insertStm.setDate(5, Date.valueOf(txtDate.getText()));
                    insertStm.setTime(6, Time.valueOf(LocalTime.parse(txtTime.getText()).withSecond(0)));
                    insertStm.setString(7, txtTelephone.getText());
                    insertStm.setString(8, txtIdNumber.getText());
                    insertStm.setString(9, txtFatherName.getText());
                    insertStm.setObject(10, txtDateOfBirth.getText().isEmpty() ? null : Date.valueOf(txtDateOfBirth.getText())); // Handle null date of birth
                    insertStm.setString(11, txtStreetAddress.getText());
                    insertStm.setString(12, txtStreetNumber.getText());
                    insertStm.setString(13,txtComment.getText().isEmpty()?"":txtComment.getText());

                    int rowsAffected = insertStm.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Return inserted successfully.");
                        new MessageBoxOk("Η επιστροφή έγινε με επιτυχία");
                        
                        // Update statistics
                        if (StatisticsManager.getInstance() != null) {
                            StatisticsManager.getInstance().forceUpdate();
                        }
                        
                        stage.close();
                    } else {
                        System.out.println("Failed to insert return.");
                    }
                    insertStm.close();
                } catch (SQLException ex) {
                    new MessageBoxOk(ex.getMessage());
                    ex.printStackTrace();
                }

            }
        });


        stage.show();


    }

    public boolean validateForm(){
        String errors = "";

        if (txtOfficer.getText().length() < 1 ) {
            errors += "Officer AM must provide.\n";
        }
        if (txtFirstName.getText().length() < 1 ) {
            errors += "First Name must provide.\n";
        }
        if (txtLastName.getText().length() < 1) {
            errors += "Last name must provide.\n";
        }
        if (txtDate.getText().length() == 0) {
            errors += "Date must be provided.\n";
        }
        if (txtTime.getText().length() <1) {
            errors += "Time must be provided.\n";
        }



        if (errors.isEmpty()) {
            return true;
        } else {
            new MessageBoxOk(errors);
            return false;
        }
    }

}
