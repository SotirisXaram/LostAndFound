package com.charamidis.lostAndFound.pages;

import com.charamidis.lostAndFound.models.Record;
import com.charamidis.lostAndFound.models.User;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class ReturnScreen {
    Label lblOfficer,lblLastName,lblFirstName,lblDate,lblTime,lblTelephone,lblDateOfBirth,lblIdNumber,lblFatherName,lblStreetAddress,lblStreetNumber,lblComment;
    TextField txtOfficer,txtLastName,txtFirstName,txtTime,txtTelephone,txtDateOfBirth,txtIdNumber,txtFatherName,txtStreetAddress,txtStreetNumber;
    TextArea txtComment;
    Button btnSave,btnCancel;
    DatePicker txtDate;
    Stage stage;
    Scene scene;
    GridPane grid;
    HBox saveDeleteHbox;
    public ReturnScreen(Connection conn, User user, Record record) {

        grid = new GridPane();

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

        lblOfficer = new Label("Αριθμός Μητρώου Υπαλλήλου:");
        txtOfficer = new TextField();
        txtOfficer.setText(Integer.valueOf(user.getAm()).toString());
        txtOfficer.setEditable(false);

        lblLastName = new Label("Επώνυμο:");
        txtLastName = new TextField();

        lblFirstName = new Label("Όνομα:");
        txtFirstName = new TextField();

        lblDate = new Label("Ημερομηνία:");
        txtDate = new DatePicker();
        txtDate.setValue(LocalDate.now());

        lblTime = new Label("Ώρα:");
        txtTime = new TextField();
        txtTime.setText(LocalTime.now().withSecond(0).withNano(0).toString());

        lblTelephone = new Label("Τηλέφωνο:");
        txtTelephone = new TextField();

        lblIdNumber = new Label("Αριθμός Ταυτότητας:");
        txtIdNumber = new TextField();

        lblFatherName = new Label("Όνομα Πατέρα:");
        txtFatherName = new TextField();

        lblDateOfBirth = new Label("Ημερομηνία Γέννησης:");
        txtDateOfBirth = new TextField();

        lblStreetAddress = new Label("Διεύθυνση:");
        txtStreetAddress = new TextField();

        lblStreetNumber = new Label("Αριθμός Διεύθυνσης:");
        txtStreetNumber = new TextField();

        lblComment = new Label("Σχόλια:");
        txtComment = new TextArea();
        txtComment.setPrefHeight(100);
        txtComment.setPrefWidth(250);
        txtComment.setWrapText(true);

        btnSave = new Button("Αποθήκευση");
        btnCancel = new Button("Ακύρωση");


        saveDeleteHbox = new HBox();
         saveDeleteHbox.getChildren().addAll(btnSave,btnCancel);
         saveDeleteHbox.setAlignment(Pos.BASELINE_CENTER);

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

        UUID uuid = UUID.randomUUID();


        btnSave.setOnAction(e -> {
            if (validateForm()) {
                try {
                    String insertQuery = "INSERT INTO returns (id, return_officer, return_last_name, return_first_name, return_date, return_time, return_telephone, return_id_number, return_father_name, return_date_of_birth, return_street_address, return_street_number,comment,uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStm = conn.prepareStatement(insertQuery);

                    insertStm.setLong(1, record.getId());
                    insertStm.setInt(2, Integer.parseInt(txtOfficer.getText()));
                    insertStm.setString(3, txtLastName.getText());
                    insertStm.setString(4, txtFirstName.getText());
                    insertStm.setString(5, String.valueOf(Date.valueOf(txtDate.getValue())));
                    insertStm.setString(6, String.valueOf(Time.valueOf(LocalTime.parse(txtTime.getText()).withSecond(0))));
                    insertStm.setString(7, txtTelephone.getText());
                    insertStm.setString(8, txtIdNumber.getText());
                    insertStm.setString(9, txtFatherName.getText());
                    insertStm.setObject(10, txtDateOfBirth.getText().isEmpty() ? null : Date.valueOf(txtDateOfBirth.getText()));
                    insertStm.setString(11, txtStreetAddress.getText());
                    insertStm.setString(12, txtStreetNumber.getText());
                    insertStm.setString(13,txtComment.getText().isEmpty()?"":txtComment.getText());
                    insertStm.setString(14,uuid.toString());
                    int rowsAffected = insertStm.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Return inserted successfully.");
                        new MessageBoxOk("Η επιστροφή έγινε με επιτυχία");
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
            errors += "Πρέπει να εισαχθεί το AM του υπεύθυνου.\n";
        }
        if (txtFirstName.getText().length() < 1 ) {
            errors += "Πρέπει να εισαχθεί το Όνομα.\n";
        }
        if (txtLastName.getText().length() < 1) {
            errors += "Πρέπει να εισαχθεί το Επώνυμο.\n";
        }

        if (!txtTime.getText().matches("^([01][0-9]|2[0-3]):[0-5][0-9]$")) {
            errors += "Η Ώρα πρέπει να είναι στη μορφή HH:mm.\n";
        }



        if (errors.isEmpty()) {
            return true;
        } else {
            new MessageBoxOk(errors);
            return false;
        }
    }

}
