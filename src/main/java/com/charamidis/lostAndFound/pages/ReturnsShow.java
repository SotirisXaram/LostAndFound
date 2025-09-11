package com.charamidis.lostAndFound.pages;

import com.charamidis.lostAndFound.models.Return;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.sql.*;

public class ReturnsShow {

    Scene scene;
    Stage stage;
    TableView<Return> tv;
    VBox vbox;
    Connection connection;

    public ReturnsShow(Connection conn) {

        connection = conn;

        TableColumn<Return, Integer> columnId = new TableColumn<>("ID");
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Return, Integer> columnOfficer = new TableColumn<>("Return Officer");
        columnOfficer.setCellValueFactory(new PropertyValueFactory<>("returnOfficer"));

        TableColumn<Return, String> columnLastName = new TableColumn<>("Last Name");
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("returnLastName"));

        TableColumn<Return, String> columnFirstName = new TableColumn<>("First Name");
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("returnFirstName"));

        TableColumn<Return, Date> columnReturnDate = new TableColumn<>("Return Date");
        columnReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        TableColumn<Return, Time> columnReturnTime = new TableColumn<>("Return Time");
        columnReturnTime.setCellValueFactory(new PropertyValueFactory<>("returnTime"));

        TableColumn<Return, String> columnTelephone = new TableColumn<>("Telephone");
        columnTelephone.setCellValueFactory(new PropertyValueFactory<>("returnTelephone"));

        TableColumn<Return, String> columnIdNumber = new TableColumn<>("ID Number");
        columnIdNumber.setCellValueFactory(new PropertyValueFactory<>("returnIdNumber"));

        TableColumn<Return, String> columnFatherName = new TableColumn<>("Father's Name");
        columnFatherName.setCellValueFactory(new PropertyValueFactory<>("returnFatherName"));

        TableColumn<Return, Date> columnDateOfBirth = new TableColumn<>("Date of Birth");
        columnDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("returnDateOfBirth"));

        TableColumn<Return, String> columnStreetAddress = new TableColumn<>("Street Address");
        columnStreetAddress.setCellValueFactory(new PropertyValueFactory<>("returnStreetAddress"));

        TableColumn<Return, String> columnStreetNumber = new TableColumn<>("Street Number");
        columnStreetNumber.setCellValueFactory(new PropertyValueFactory<>("returnStreetNumber"));

        TableColumn<Return, Date> columnTimestamp = new TableColumn<>("Timestamp");
        columnTimestamp.setCellValueFactory(new PropertyValueFactory<>("returnTimestamp"));

        TableColumn<Return, String> columnComment = new TableColumn<>("Comment");
        columnComment.setCellValueFactory(new PropertyValueFactory<>("comment"));


        tv = new TableView<>();
        tv.setPrefWidth(1200);
        tv.getColumns().addAll(columnId, columnOfficer, columnLastName, columnFirstName, columnReturnDate,
                columnReturnTime, columnTelephone, columnIdNumber, columnFatherName, columnDateOfBirth,
                columnStreetAddress, columnStreetNumber,columnComment, columnTimestamp);
        Statement stm = null;
        try {
            stm = connection.createStatement();
            ResultSet resultSet = stm.executeQuery("SELECT * FROM returns ORDER BY id");
            while (resultSet.next()) {
                // Safe date parsing
                String returnDate = resultSet.getString("return_date");
                String returnTime = resultSet.getString("return_time");
                String returnDateOfBirth = resultSet.getString("return_date_of_birth");
                String returnTimestamp = resultSet.getString("return_timestamp");
                
                // Handle null values
                if (returnTime == null) returnTime = "";
                if (returnDateOfBirth == null) returnDateOfBirth = "";
                if (returnTimestamp == null) returnTimestamp = "";
                
                Return ret = new Return(resultSet.getInt("id"), resultSet.getInt("return_officer"),
                        resultSet.getString("return_last_name"), resultSet.getString("return_first_name"),
                        returnDate, returnTime,
                        resultSet.getString("return_telephone"), resultSet.getString("return_id_number"),
                        resultSet.getString("return_father_name"), returnDateOfBirth,
                        resultSet.getString("return_street_address"), resultSet.getString("return_street_number"),
                        returnTimestamp, resultSet.getString("comment"));
                tv.getItems().add(ret);
            }
        } catch (SQLException exception) {
            new MessageBoxOk(exception.getMessage());
            exception.printStackTrace();
        }

        scene = new Scene(tv);
        stage = new Stage();
        stage.setTitle("Returns");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}
