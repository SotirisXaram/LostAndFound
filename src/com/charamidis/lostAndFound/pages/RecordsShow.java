package com.charamidis.lostAndFound.pages;

import com.charamidis.lostAndFound.models.Record;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.sql.Time;

import java.sql.*;

public class RecordsShow {

    Scene scene;
    Stage stage;
    TableView<Record> tv;
    VBox vbox;
    Connection connection;

    public RecordsShow(Connection conn) {
        connection = conn;

        TableColumn<Record, Integer> columnId = new TableColumn<>("ID");
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Record, String> columnRecordDate = new TableColumn<>("Record Date");
        columnRecordDate.setCellValueFactory(new PropertyValueFactory<>("record_date"));

        TableColumn<Record, String> columnRecordTime = new TableColumn<>("Record Time");
        columnRecordTime.setCellValueFactory(new PropertyValueFactory<>("record_time"));

        TableColumn<Record, Integer> columnOfficerId = new TableColumn<>("Officer ID");
        columnOfficerId.setCellValueFactory(new PropertyValueFactory<>("officer_id"));

        TableColumn<Record, String> columnLastName = new TableColumn<>("Last Name");
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("founder_last_name"));

        TableColumn<Record, String> columnFirstName = new TableColumn<>("First Name");
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("founder_first_name"));

        TableColumn<Record, String> columnIdNumber = new TableColumn<>("ID Number");
        columnIdNumber.setCellValueFactory(new PropertyValueFactory<>("founder_id_number"));

        TableColumn<Record, String> columnTelephone = new TableColumn<>("Telephone");
        columnTelephone.setCellValueFactory(new PropertyValueFactory<>("founder_telephone"));

        TableColumn<Record, String> columnStreetAddress = new TableColumn<>("Street Address");
        columnStreetAddress.setCellValueFactory(new PropertyValueFactory<>("founder_street_address"));

        TableColumn<Record, String> columnStreetNumber = new TableColumn<>("Street Number");
        columnStreetNumber.setCellValueFactory(new PropertyValueFactory<>("founder_street_number"));

        TableColumn<Record, String> columnFatherName = new TableColumn<>("Father's Name");
        columnFatherName.setCellValueFactory(new PropertyValueFactory<>("founder_father_name"));

        TableColumn<Record, String> columnAreaInhabitant = new TableColumn<>("Area Inhabitant");
        columnAreaInhabitant.setCellValueFactory(new PropertyValueFactory<>("founder_area_inhabitant"));

        TableColumn<Record, String> columnFoundDate = new TableColumn<>("Found Date");
        columnFoundDate.setCellValueFactory(new PropertyValueFactory<>("found_date"));

        TableColumn<Record, Time> columnFoundTime = new TableColumn<>("Found Time");
        columnFoundTime.setCellValueFactory(new PropertyValueFactory<>("found_time"));

        TableColumn<Record, String> columnFoundLocation = new TableColumn<>("Found Location");
        columnFoundLocation.setCellValueFactory(new PropertyValueFactory<>("found_location"));

        TableColumn<Record, String> columnItemDescription = new TableColumn<>("Item Description");
        columnItemDescription.setCellValueFactory(new PropertyValueFactory<>("item_description"));

        columnItemDescription.setCellFactory(tc -> {
            TableCell<Record, String> cell = new TableCell<Record, String>() {
                private Text text = new Text();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        text.setText(item);
                        text.wrappingWidthProperty().bind(getTableColumn().widthProperty());
                        setGraphic(text);
                    }
                }
            };
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            return cell;
        });


        tv = new TableView<>();
        tv.setPrefWidth(1200);
        tv.getColumns().addAll(columnId, columnRecordDate, columnRecordTime, columnOfficerId, columnLastName,
                columnFirstName, columnIdNumber, columnTelephone, columnStreetAddress, columnStreetNumber,
                columnFatherName, columnAreaInhabitant, columnFoundDate, columnFoundTime, columnFoundLocation, columnItemDescription);


        try (Statement stm = connection.createStatement()) {
            ResultSet resultSet = stm.executeQuery("SELECT * FROM records ORDER BY id");
            while (resultSet.next()) {
                Record record = new Record(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("record_datetime").toLocalDateTime().toLocalDate().toString(),
                        resultSet.getTimestamp("record_datetime").toLocalDateTime().toLocalTime().withSecond(0).toString(),
                        resultSet.getInt("officer_id"),
                        resultSet.getString("founder_last_name"),
                        resultSet.getString("founder_first_name"),
                        resultSet.getString("founder_id_number"),
                        resultSet.getString("founder_telephone"),
                        resultSet.getString("founder_street_address"),
                        resultSet.getString("founder_street_number"),
                        resultSet.getString("founder_father_name"),
                        resultSet.getString("founder_area_inhabitant"),
                        resultSet.getDate("found_date"),
                        resultSet.getTime("found_time"),
                        resultSet.getString("found_location"),
                        resultSet.getString("item_description")

                );
                tv.getItems().add(record);
            }
        } catch (SQLException exception) {
            new MessageBoxOk(exception.getMessage());
            exception.printStackTrace();
        }

        scene = new Scene(tv);
        stage = new Stage();
        stage.setTitle("Records");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}
