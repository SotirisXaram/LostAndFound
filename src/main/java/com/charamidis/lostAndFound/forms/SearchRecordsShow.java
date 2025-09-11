package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.models.Record;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class SearchRecordsShow {

    Scene scene;
    Stage stage;
    TableView<Record> tv;
    VBox vbox;
    Connection connection;

    public SearchRecordsShow(Connection conn) {
        connection = conn;

        // Define columns
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

        TableColumn<Record, String> columnFoundTime = new TableColumn<>("Found Time");
        columnFoundTime.setCellValueFactory(new PropertyValueFactory<>("found_time"));

        TableColumn<Record, String> columnFoundLocation = new TableColumn<>("Found Location");
        columnFoundLocation.setCellValueFactory(new PropertyValueFactory<>("found_location"));

        TableColumn<Record, String> columnItemDescription = new TableColumn<>("Item Description");
        columnItemDescription.setCellValueFactory(new PropertyValueFactory<>("item_description"));

        // Enable text wrapping for the Item Description column
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

        ObservableList<Record> data = FXCollections.observableArrayList();

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
                        resultSet.getString("item_description"),
                        resultSet.getString("item_category"),
                        resultSet.getString("item_brand"),
                        resultSet.getString("item_model"),
                        resultSet.getString("item_color"),
                        resultSet.getString("item_serial_number"),
                        resultSet.getString("storage_location"),
                        resultSet.getString("picture_path")

                );
                data.add(record);
            }
        } catch (SQLException exception) {
            new MessageBoxOk(exception.getMessage());
            exception.printStackTrace();
        }

        FilteredList<Record> filteredData = new FilteredList<>(data, p -> true);
        tv.setItems(filteredData);

        // Create search bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search records...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(record -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                // Search across all fields
                return Objects.toString(record.getId(), "").contains(lowerCaseFilter)
                        || Objects.toString(record.getRecord_date(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getRecord_time(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getOfficer_id(), "").contains(lowerCaseFilter)
                        || Objects.toString(record.getFounder_last_name(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getFounder_first_name(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getFounder_id_number(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getFounder_telephone(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getFounder_street_address(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getFounder_street_number(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getFounder_father_name(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getFounder_area_inhabitant(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getFound_date(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getFound_time(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getFound_location(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getItem_description(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getItem_category(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getItem_brand(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getItem_model(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getItem_color(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getItem_serial_number(), "").toLowerCase().contains(lowerCaseFilter)
                        || Objects.toString(record.getStorage_location(), "").toLowerCase().contains(lowerCaseFilter);
            });
        });

        VBox searchBar = new VBox();
        searchBar.getChildren().add(searchField);

        BorderPane layout = new BorderPane();
        layout.setTop(searchBar);
        layout.setCenter(tv);

        // Add column visibility context menu
        ContextMenu contextMenu = new ContextMenu();
        for (TableColumn<Record, ?> column : tv.getColumns()) {
            CheckMenuItem menuItem = new CheckMenuItem(column.getText());
            menuItem.setSelected(true);
            menuItem.setOnAction(event -> column.setVisible(menuItem.isSelected()));
            contextMenu.getItems().add(menuItem);
        }

        tv.setOnMouseClicked(event -> {
            if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                contextMenu.show(tv, event.getScreenX(), event.getScreenY());
            }
        });

        scene = new Scene(layout);
        stage = new Stage();
        stage.setTitle("Records");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}