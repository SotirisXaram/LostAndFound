package com.charamidis.lostAndFound.pages;

import com.charamidis.lostAndFound.models.Return;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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

        // Create main container with minimal padding for full width/height
        VBox mainContainer = new VBox(5);
        mainContainer.setPadding(new Insets(10));
        mainContainer.setStyle("-fx-background-color: #f8f9fa;");
        
        // Header section with minimal spacing
        VBox headerSection = new VBox(5);
        headerSection.setAlignment(Pos.CENTER);
        headerSection.setPadding(new Insets(5, 0, 10, 0));
        
        Label titleLabel = new Label("Returns Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.rgb(52, 73, 94));
        
        Label subtitleLabel = new Label("View and manage all return records");
        subtitleLabel.setFont(Font.font("Arial", 12));
        subtitleLabel.setTextFill(Color.rgb(108, 117, 125));
        
        headerSection.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Table section with minimal padding
        VBox tableSection = new VBox(5);
        tableSection.setPadding(new Insets(5));
        tableSection.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);");
        
        // Create table columns with modern styling
        TableColumn<Return, Integer> columnId = new TableColumn<>("ID");
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnId.setPrefWidth(60);
        columnId.setStyle("-fx-alignment: CENTER;");

        TableColumn<Return, String> columnFirstName = new TableColumn<>("First Name");
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("returnFirstName"));
        columnFirstName.setPrefWidth(120);

        TableColumn<Return, String> columnLastName = new TableColumn<>("Last Name");
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("returnLastName"));
        columnLastName.setPrefWidth(120);

        TableColumn<Return, String> columnReturnDate = new TableColumn<>("Return Date");
        columnReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        columnReturnDate.setPrefWidth(100);

        TableColumn<Return, String> columnReturnTime = new TableColumn<>("Time");
        columnReturnTime.setCellValueFactory(new PropertyValueFactory<>("returnTime"));
        columnReturnTime.setPrefWidth(80);

        TableColumn<Return, String> columnTelephone = new TableColumn<>("Telephone");
        columnTelephone.setCellValueFactory(new PropertyValueFactory<>("returnTelephone"));
        columnTelephone.setPrefWidth(120);

        TableColumn<Return, String> columnIdNumber = new TableColumn<>("ID Number");
        columnIdNumber.setCellValueFactory(new PropertyValueFactory<>("returnIdNumber"));
        columnIdNumber.setPrefWidth(100);

        TableColumn<Return, String> columnComment = new TableColumn<>("Comment");
        columnComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        columnComment.setPrefWidth(200);

        tv = new TableView<>();
        tv.setMaxWidth(Double.MAX_VALUE);
        tv.setMaxHeight(Double.MAX_VALUE);
        tv.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5;");
        
        // Set table header style
        tv.setStyle(tv.getStyle() + 
            " -fx-table-header-border-color: #dee2e6;" +
            " -fx-table-cell-border-color: #f1f3f4;");
        
        // Configure comment column for text wrapping
        columnComment.setPrefWidth(300);
        columnComment.setCellFactory(tc -> {
            TableCell<Return, String> cell = new TableCell<Return, String>() {
                private Text text;
                
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        if (text == null) {
                            text = new Text();
                            text.wrappingWidthProperty().bind(columnComment.widthProperty().subtract(10));
                            text.setStyle("-fx-font-size: 12px;");
                        }
                        text.setText(item);
                        setGraphic(text);
                    }
                }
            };
            return cell;
        });
        
        tv.getColumns().addAll(columnId, columnFirstName, columnLastName, columnReturnDate,
                columnReturnTime, columnTelephone, columnIdNumber, columnComment);
        
        // Search section
        HBox searchSection = new HBox(15);
        searchSection.setAlignment(Pos.CENTER);
        searchSection.setPadding(new Insets(10, 0, 20, 0));
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search by ID, name, phone, address, date...");
        searchField.setPrefWidth(350);
        searchField.setPrefHeight(35);
        searchField.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8 12; -fx-font-size: 14;");
        
        Button searchBtn = new Button("Search");
        searchBtn.setPrefWidth(100);
        searchBtn.setPrefHeight(35);
        searchBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 14;");
        
        Button clearBtn = new Button("Clear");
        clearBtn.setPrefWidth(100);
        clearBtn.setPrefHeight(35);
        clearBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 14;");
        
        searchSection.getChildren().addAll(searchField, searchBtn, clearBtn);
        
        // Add search functionality
        searchBtn.setOnAction(e -> searchReturns(searchField.getText()));
        clearBtn.setOnAction(e -> {
            searchField.clear();
            loadAllReturns();
        });
        
        // Add search on Enter key
        searchField.setOnAction(e -> searchReturns(searchField.getText()));
        
        // Add table to section and make it grow
        tableSection.getChildren().addAll(searchSection, tv);
        VBox.setVgrow(tableSection, Priority.ALWAYS);
        VBox.setVgrow(tv, Priority.ALWAYS);
        
        mainContainer.getChildren().addAll(headerSection, tableSection);
        
        // Load all returns initially
        loadAllReturns();

        scene = new Scene(mainContainer);
        stage = new Stage();
        stage.setTitle("Returns Management");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setWidth(1400);
        stage.setHeight(800);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.centerOnScreen();
        stage.setMaximized(true);
        stage.show();
    }
    
    private void loadAllReturns() {
        loadReturns("SELECT * FROM returns ORDER BY id");
    }
    
    private void searchReturns(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            loadAllReturns();
            return;
        }
        
        String searchQuery = "SELECT * FROM returns WHERE " +
                           "CAST(id AS TEXT) LIKE ? OR " +
                           "return_last_name LIKE ? OR " +
                           "return_first_name LIKE ? OR " +
                           "return_telephone LIKE ? OR " +
                           "return_id_number LIKE ? OR " +
                           "return_father_name LIKE ? OR " +
                           "return_street_address LIKE ? OR " +
                           "return_street_number LIKE ? OR " +
                           "return_date LIKE ? OR " +
                           "return_time LIKE ? OR " +
                           "return_date_of_birth LIKE ? " +
                           "ORDER BY id";
        
        loadReturns(searchQuery, searchTerm);
    }
    
    private void loadReturns(String query, String... searchTerms) {
        tv.getItems().clear();
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Set search parameters if provided
            if (searchTerms.length > 0) {
                String searchPattern = "%" + searchTerms[0] + "%";
                for (int i = 0; i < 11; i++) { // 11 search fields (including ID, excluding comment)
                    stmt.setString(i + 1, searchPattern);
                }
            }
            
            ResultSet resultSet = stmt.executeQuery();
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
            new MessageBoxOk("Error loading returns: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}
