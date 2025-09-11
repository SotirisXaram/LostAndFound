package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.utils.MessageBoxOk;
import com.charamidis.lostAndFound.models.User;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.util.converter.IntegerStringConverter;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EditUsers {

    Scene scene ;
    Stage stage;
    TableView<User> tv ;
    Connection connection;
    public EditUsers(Connection conn){

        connection = conn;

        TableColumn<User,Integer> columnAm = new TableColumn<>("ΑΡΙΘΜΟΣ ΜΗΤΡΩΟΥ");
        columnAm.setCellValueFactory(new PropertyValueFactory<>("am"));
        columnAm.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        columnAm.setOnEditCommit(event -> event.getRowValue().setAm(event.getNewValue()));

        TableColumn<User,String> columnFirstName = new TableColumn<>("ONOMA");
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        columnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnFirstName.setOnEditCommit(event -> event.getRowValue().setFirstName(event.getNewValue()));

        TableColumn<User,String> columnLastName = new TableColumn<>("ΕΠΩΝΥΜΟ");
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        columnLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnLastName.setOnEditCommit(event -> event.getRowValue().setLastName(event.getNewValue()));

        TableColumn<User,String> columnBday = new TableColumn<>("ΗΜΕΡΟΜΗΝΙΑ ΓΕΝΝΗΣΗΣ");
        columnBday.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        columnBday.setCellFactory(TextFieldTableCell.forTableColumn());
        columnBday.setOnEditCommit(event -> event.getRowValue().setBirthday(event.getNewValue()));

        TableColumn<User,String> columnDateLoggedIn = new TableColumn<>("ΗΜΕΡΟΜΗΝΙΑ ΕΙΣΟΔΟΥ");
        columnDateLoggedIn.setCellValueFactory(new PropertyValueFactory<>("dateLoggedIn"));

        TableColumn<User,String> columnTimeLoggedIn = new TableColumn<>("ΩΡΑ ΕΙΣΟΔΟΥ");
        columnTimeLoggedIn.setCellValueFactory(new PropertyValueFactory<>("timeLoggedIn"));

        TableColumn<User,String> columnRole = new TableColumn<>("ΡΟΛΟΣ");
        columnRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        columnRole.setCellFactory(ComboBoxTableCell.forTableColumn("user","admin"));
        columnRole.setOnEditCommit(event -> event.getRowValue().setRole(event.getNewValue()));


        tv = new TableView<>();
        tv.setPrefWidth(800);
        tv.setEditable(true);
        tv.getColumns().addAll(columnAm,columnLastName,columnFirstName,columnBday,columnDateLoggedIn,columnTimeLoggedIn,columnRole);
        Statement stm = null;
        try{
            stm = connection.createStatement();
            ResultSet resultSet = stm.executeQuery("SELECT * FROM users ORDER BY last_name");
            while(resultSet.next()){
                User user = new User(resultSet.getInt("am"),resultSet.getString("first_name"),resultSet.getString("last_name"), resultSet.getString("date_of_birth"),LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),resultSet.getString("role"));
                tv.getItems().add(user);
            }
        }catch (SQLException exception){
            new MessageBoxOk(exception.getMessage());
            exception.printStackTrace();
        }





        scene = new Scene(tv);
        stage = new Stage();
        stage.setTitle("Edit Users");
        stage.setScene(scene);

        stage.setOnCloseRequest(e->{
            tv.getItems().forEach(user -> {
                try {
                    updateUserInDatabase(user);
                }catch (SQLException exception){
                    new MessageBoxOk("Failed to update user with AM: " + user.getAm()+"\n"+exception.getMessage());

                }

            });
        });

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void updateUserInDatabase(User user) throws SQLException {
        String updateSQL = "UPDATE users SET am = ?, first_name = ?, last_name = ?, date_of_birth = ?, role = ? WHERE am = ?";
        try (PreparedStatement stm = connection.prepareStatement(updateSQL)) {
            stm.setInt(1,user.getAm());
            stm.setString(2, user.getFirstName());
            stm.setString(3, user.getLastName());

            // Handle different date formats
            String birthday = user.getBirthday();
            java.sql.Date sqlDate;
            
            try {
                // Try parsing as yyyy-MM-dd first
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(birthday, formatter);
                sqlDate = java.sql.Date.valueOf(localDate);
            } catch (Exception e1) {
                try {
                    // Try parsing as dd/MM/yyyy
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate localDate = LocalDate.parse(birthday, formatter);
                    sqlDate = java.sql.Date.valueOf(localDate);
                } catch (Exception e2) {
                    try {
                        // Try parsing as timestamp (milliseconds)
                        long timestamp = Long.parseLong(birthday);
                        sqlDate = new java.sql.Date(timestamp);
                    } catch (Exception e3) {
                        // If all else fails, use current date
                        sqlDate = java.sql.Date.valueOf(LocalDate.now());
                    }
                }
            }
            stm.setDate(4, sqlDate);

            stm.setString(5, user.getRole());
            stm.setInt(6, user.getAm());
            stm.executeUpdate();
        }
    }


}
