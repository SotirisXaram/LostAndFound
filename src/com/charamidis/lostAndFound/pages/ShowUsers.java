package com.charamidis.lostAndFound.pages;

import com.charamidis.lostAndFound.models.User;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ShowUsers {

    Scene scene ;
    Stage stage;
    TableView<User> tv ;
    VBox vbox;
    Connection connection;
    public ShowUsers(Connection conn){

        connection = conn;

        TableColumn<User,Integer> columnAm = new TableColumn<>("ΑΡΙΘΜΟΣ ΜΗΤΡΩΟΥ");
        columnAm.setCellValueFactory(new PropertyValueFactory<>("am"));

        TableColumn<User,String> columnFirstName = new TableColumn<>("ONOMA");
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<User,String> columnLastName = new TableColumn<>("ΕΠΩΝΥΜΟ");
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<User,String> columnBday = new TableColumn<>("ΗΜΕΡΟΜΗΝΙΑ ΓΕΝΝΗΣΗΣ");
        columnBday.setCellValueFactory(new PropertyValueFactory<>("birthday"));

        TableColumn<User,String> columnDateLoggedIn = new TableColumn<>("ΗΜΕΡΟΜΗΝΙΑ ΕΙΣΟΔΟΥ");
        columnDateLoggedIn.setCellValueFactory(new PropertyValueFactory<>("dateLoggedIn"));

        TableColumn<User,String> columnTimeLoggedIn = new TableColumn<>("ΩΡΑ ΕΙΣΟΔΟΥ");
        columnTimeLoggedIn.setCellValueFactory(new PropertyValueFactory<>("timeLoggedIn"));

        TableColumn<User,String> columnRole = new TableColumn<>("ΡΟΛΟΣ");
        columnRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        tv = new TableView<>();
        tv.setPrefWidth(800);
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

        //vbox = new VBox();
      //  vbox.getChildren().add(tv);
      //  VBox.setVgrow(vbox,Priority.ALWAYS);
        scene = new Scene(tv);
        stage = new Stage();
        stage.setTitle("Users");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }




}
