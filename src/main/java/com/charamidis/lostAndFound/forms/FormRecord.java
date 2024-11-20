package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.states.EnumFormState;
import com.charamidis.lostAndFound.utils.AppLogger;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import com.charamidis.lostAndFound.utils.PrintReport;
import com.charamidis.lostAndFound.pages.ReturnScreen;
import com.charamidis.lostAndFound.models.User;
import com.charamidis.lostAndFound.models.Record;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.pdf417.PDF417Writer;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FormRecord {


    HBox hboxSaveCancel,buttonsCrud;
    Stage window ;
    Scene scene;
    ListView<Record> listView;
    Label lblId,lblDate,lblOfficerId,lblFoundTime,lblFounderLastName,lblFounderFirstName,lblFounderIdNumber,lblFounderTelephone,lblFounderStreetNumber,lblFounderStreetAddress,lblFounderFatherName,lblFounderAreaInhabitant,lblFoundDate,lblFoundLocation,lblItemDescription;
    TextField txtId,txtOfficerId,txtFounderLastName,txtFoundTime,txtFounderFirstName,txtFounderIdNumber,txtFounderTelephone,txtFounderStreetAddress,txtFounderStreetNumber,txtFounderFatherName,txtFounderAreaInhabitant,txtFoundLocation,txtItemDescription;
    DatePicker txtDate,txtFoundDate;
    GridPane gridPane ;
    Button btnNew,btnEdit,btnDelete,btnPrint,btnReturn ;
    EnumFormState enumFormState;
    User currUser;
    Connection connection;
    private static final Logger logger = AppLogger.getLogger();


    public FormRecord(Connection conn, User user){

        connection=conn;

        gridPane = new GridPane();
        gridPane.setHgap(3);
        gridPane.setVgap(5);

        gridPane.setPrefWidth(500);
        gridPane.setAlignment(Pos.TOP_CENTER);



        listView = new ListView<>();


        currUser = user;


        gridPane.add(listView,0,0);
        GridPane.setRowSpan(listView,gridPane.getRowCount()+1);
        GridPane.setHalignment(listView,HPos.LEFT);




        lblId = new Label("Αύξων αριθμός (A/A):");
        lblOfficerId = new Label("ΑΜ Αστυνομικού:");
        lblDate = new Label("Ημερομηνία Εγγραφής:");
        lblFoundDate = new Label("Ημερομηνία Εύρεσης:");
        lblFounderAreaInhabitant = new Label("Κατοικία ευρήτη:");
        lblFounderFatherName = new Label("Πατρώνυμο ευρέτη:");
        lblFounderFirstName = new Label("Ονομα ευρέτη:");
        lblFounderLastName = new Label("Επώνυμο ευρέτη:");
        lblFounderTelephone = new Label("Τηλέφωνο ευρέτη:");
        lblFoundLocation = new Label("Τοποθεσία Ανεύρεσης:");
        lblItemDescription = new Label("Περιγραφή αντικειμένου/ων:");
        lblFounderIdNumber = new Label("ΑΔΤ ευρέτη:");
        lblFounderStreetAddress= new Label("Διεύθυνση κατοικίας ευρέτη");
        lblFounderStreetNumber= new Label("Αριθμός διεύθυνσης ευρέτη");
        lblFoundTime = new Label("Ωρα Ευρεσης:");


        lblId.setFont(Font.font("System", javafx.scene.text.FontWeight.BOLD, 12));
        lblOfficerId.setFont(Font.font("System", javafx.scene.text.FontWeight.BOLD, 12));
        lblDate.setFont(Font.font("System", javafx.scene.text.FontWeight.BOLD, 12));
        lblFoundDate.setFont(Font.font("System", javafx.scene.text.FontWeight.BOLD, 12));
        lblFoundLocation.setFont(Font.font("System", javafx.scene.text.FontWeight.BOLD, 12));
        lblFounderLastName.setFont(Font.font("System", javafx.scene.text.FontWeight.BOLD, 12));
        lblFounderFirstName.setFont(Font.font("System", javafx.scene.text.FontWeight.BOLD, 12));
        lblItemDescription.setFont(Font.font("System", javafx.scene.text.FontWeight.BOLD, 12));
        lblFounderIdNumber.setFont(Font.font("System", javafx.scene.text.FontWeight.BOLD, 12));


        txtId = new TextField();
        txtOfficerId = new TextField();
        txtDate = new DatePicker();
        txtFoundDate = new DatePicker();
        txtFounderAreaInhabitant = new TextField();
        txtFounderFatherName = new TextField();
        txtFounderFirstName = new TextField();
        txtFounderLastName = new TextField();
        txtFounderTelephone = new TextField();
        txtFoundLocation = new TextField();
        txtItemDescription = new TextField();
        txtFounderIdNumber = new TextField();
        txtFounderStreetAddress=new TextField();
        txtFounderStreetNumber=new TextField();
        txtFoundTime = new TextField();


        gridPane.add(lblId,1,0);
        gridPane.add(txtId,2,0);

        gridPane.add(lblOfficerId,1,1);
        gridPane.add(txtOfficerId,2,1);

        gridPane.add(lblDate,1,2);
        gridPane.add(txtDate,2,2);


        gridPane.add(lblFounderLastName,1,3);
        gridPane.add(txtFounderLastName,2,3);

        gridPane.add(lblFounderFirstName,1,4);
        gridPane.add(txtFounderFirstName,2,4);

        gridPane.add(lblFounderIdNumber,1,5);
        gridPane.add(txtFounderIdNumber,2,5);

        gridPane.add(lblFounderFatherName,1,6);
        gridPane.add(txtFounderFatherName,2,6);

        gridPane.add(lblFounderAreaInhabitant,1,7);
        gridPane.add(txtFounderAreaInhabitant,2,7);

        gridPane.add(lblFounderStreetAddress,1,8);
        gridPane.add(txtFounderStreetAddress,2,8);


        gridPane.add(lblFounderStreetNumber,1,9);
        gridPane.add(txtFounderStreetNumber,2,9);

        gridPane.add(lblFounderTelephone,1,10);
        gridPane.add(txtFounderTelephone,2,10);

        gridPane.add(lblFoundLocation,1,11);
        gridPane.add(txtFoundLocation,2,11);


        gridPane.add(lblFoundDate,1,12);
        gridPane.add(txtFoundDate,2,12);


        gridPane.add(lblItemDescription,1,13);
        gridPane.add(txtItemDescription,2,13);


        gridPane.add(lblFoundTime,1,14);
        gridPane.add(txtFoundTime,2,14);






        GridPane.setRowSpan(listView,gridPane.getRowCount()+1);
        GridPane.setHalignment(listView,HPos.LEFT);

        gridPane.setPrefWidth(700);
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setPadding(new Insets(5));


        ColumnConstraints col0 = new ColumnConstraints();
        col0.setHgrow(Priority.SOMETIMES);
        col0.setPrefWidth(300);
        col0.setHalignment(HPos.LEFT);



        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        col1.setHalignment(HPos.RIGHT);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);

        Region rEmpty = new Region();
        gridPane.add(rEmpty,3,0);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setHgrow(Priority.SOMETIMES);


        gridPane.getColumnConstraints().addAll(col0,col1,col2,col3);




        GridPane.setHgrow(gridPane,Priority.ALWAYS);

        enumFormState = EnumFormState.VIEW;
        buttonsCrud = new HBox();
        btnNew = new Button();
        btnNew.setGraphic(new ImageView(new Image("add_10336805.png",16,16,true,true)));
        btnNew.setOnAction(e->{
            enumFormState = EnumFormState.ADD;
            changeState(enumFormState,null);
            txtFounderLastName.requestFocus();
        });
        btnEdit = new Button();
        btnEdit.setGraphic(new ImageView(new Image("edit_9333897.png",16,16,true,true)));
        btnEdit.setOnAction(e->{
            if(listView.getSelectionModel().getSelectedItem()!=null){
                enumFormState = EnumFormState.EDIT;
                changeState(enumFormState,listView.getSelectionModel().getSelectedItem());
                txtFounderLastName.requestFocus();
            }

        });

        btnDelete = new Button();

        btnDelete.setOnAction(e -> {
            if (listView.getSelectionModel().getSelectedItem() != null) {
                MessageBoxOk m = new MessageBoxOk("Θέλετε σίγουρα να διαγράψετε την εγγραφή?");
                boolean response = m.getRes();
                if (response) {
                    Record c = listView.getSelectionModel().getSelectedItem();
                    listView.getItems().remove(c);

                    if (listView.getItems().size() > 0) {
                        listView.getSelectionModel().select(0);
                    } else {
                        enumFormState = EnumFormState.ADD;
                        changeState(enumFormState, new Record());
                    }

                    try {
                        // Begin transaction
                        conn.setAutoCommit(false);

                        // Delete from records table
                        String delRecordsQuery = "DELETE FROM records WHERE id = ? AND item_description = ?";
                        try (PreparedStatement stmRecords = conn.prepareStatement(delRecordsQuery)) {
                            stmRecords.setInt(1, c.getId());
                            stmRecords.setString(2, c.getItem_description());
                            stmRecords.executeUpdate();
                        }

                        // Delete from returns table
                        String delReturnsQuery = "DELETE FROM returns WHERE id = ?";
                        try (PreparedStatement stmReturns = conn.prepareStatement(delReturnsQuery)) {
                            stmReturns.setInt(1, c.getId());
                            stmReturns.executeUpdate();
                        }

                        // Commit the transaction
                        conn.commit();
                    } catch (SQLException ex) {
                        // Rollback transaction on error
                        try {
                            conn.rollback();
                        } catch (SQLException rollbackEx) {
                            new MessageBoxOk("Rollback failed: " + rollbackEx.getMessage());
                            logger.log(Level.SEVERE, "Error during rollback", rollbackEx);
                        }
                        new MessageBoxOk(ex.getMessage());
                        logger.log(Level.FINE, "Error with form record to delete ", ex);
                    } finally {
                        try {
                            conn.setAutoCommit(true); // Restore auto-commit mode
                        } catch (SQLException finalEx) {
                            logger.log(Level.SEVERE, "Error resetting auto-commit", finalEx);
                        }
                    }
                }

                if (listView.getItems().isEmpty()) {
                    window.close();
                }
            }
        });


        btnPrint = new Button("Print");
        btnPrint.setGraphic(new ImageView(new Image("printer.png",16,16,true,true)));


        btnDelete.setGraphic(new ImageView(new Image("delete_12379137.png",16,16,true,true)));
        btnPrint.setOnAction(e->{
            Record record = listView.getSelectionModel().getSelectedItem();
            if(!listView.getItems().isEmpty()){
                new PrintReport(record,conn);
            }
        });

        btnReturn = new Button("Επιστροφή");
        btnReturn.setGraphic(new ImageView(new Image("product-return.png", 16, 16, true, true)));



        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnReturn.setDisable(false);

            if (newValue != null) {
                int itemId = newValue.getId();

                try {
                    String query = "SELECT id FROM returns WHERE id = ?";
                    PreparedStatement btnStm = conn.prepareStatement(query);
                    btnStm.setInt(1, itemId);
                    ResultSet set = btnStm.executeQuery();

                    if (set.next()) {
                        btnReturn.setDisable(true);
                    }

                    set.close();
                    btnStm.close();
                } catch (SQLException e) {
                    new MessageBoxOk(e.getMessage());
                    logger.log(Level.SEVERE,"Error with form record with sql conn ",e);
                }
            }
        });


        btnReturn.setOnAction(e->{

          if(listView.getItems().isEmpty()) return;

          int itemId = listView.getSelectionModel().getSelectedItem().getId();

          try{
              String query = "SELECT id FROM returns WHERE id = ?";
              PreparedStatement btnStm = conn.prepareStatement(query);
              btnStm.setInt(1, itemId);
              ResultSet set = btnStm.executeQuery();

              if(!set.next() && listView.getSelectionModel().getSelectedItem()!=null){
                  new ReturnScreen(conn,user,listView.getSelectionModel().getSelectedItem());
              }

              set.close();
              btnStm.close();

          } catch (SQLException ex) {
             logger.log(Level.SEVERE,"Cannot return items sql problem",ex);
          }

        });




//Barcode
        listView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.B && event.isShiftDown() && event.isControlDown()) {
                String uid = null;
                try{
                    String query = "SELECT uuid FROM records WHERE id = ? AND item_description = ?";
                    PreparedStatement stm = conn.prepareStatement(query);
                    stm.setInt(1, listView.getSelectionModel().getSelectedItem().getId());
                    stm.setString(2,listView.getSelectionModel().getSelectedItem().getItem_description());
                    ResultSet set = stm.executeQuery();
                    if(set.next()){
                        uid = set.getString("uuid");
                    }
                    String data = String.valueOf(uid);
                    generatedBarcode(data);
                }catch (SQLException e){
                    new MessageBoxOk(e.getMessage()+"\n"+"Error printing the barcode");
                    logger.log(Level.FINE,"Error with barcode ",e);

                }

            }
        });


        buttonsCrud.setSpacing(10);
        buttonsCrud.setAlignment(Pos.CENTER);
        if(user.getRole().equals("admin")){
            buttonsCrud.getChildren().addAll(btnNew, btnEdit, btnDelete,btnPrint,btnReturn);
        }else{
            buttonsCrud.getChildren().addAll(btnNew, btnEdit,btnPrint,btnReturn);

        }
        gridPane.add(buttonsCrud, 0, gridPane.getRowCount()+1);



        Button btnSave = new Button("Save");
        Button btnCancel = new Button("Cancel");




        btnSave.setOnAction(e->{
            if(validateForm()){

                if(enumFormState == EnumFormState.EDIT){
                    Record c = listView.getSelectionModel().getSelectedItem();
                    c.setId(Integer.valueOf(txtId.getText()));
                    c.setFound_location(txtFoundLocation.getText());
                    c.setFounder_last_name(txtFounderLastName.getText());
                    c.setFounder_first_name(txtFounderFirstName.getText());
                    c.setFounder_area_inhabitant(txtFounderAreaInhabitant.getText().equals("")?null:(txtFounderAreaInhabitant.getText()));
                    c.setFounder_father_name(txtFounderFatherName.getText().equals("")?null:(txtFounderFatherName.getText()));
                    c.setFounder_street_address(txtFounderStreetAddress.equals("")?null:(txtFounderStreetAddress.getText()));
                    c.setFounder_street_number(txtFounderStreetNumber.equals("")?null:(txtFounderStreetNumber.getText()));
                    c.setFounder_id_number(txtFounderIdNumber.getText());
                    c.setFound_date(String.valueOf(Date.valueOf(txtFoundDate.getValue())));
                    c.setFound_time(Time.valueOf(txtFoundTime.getText()+":00").toString());
                    c.setOfficer_id(Integer.valueOf(txtOfficerId.getText()));
                    c.setFounder_telephone(txtFounderTelephone.getText().equals("")?null: txtFounderTelephone.getText());
                    c.setItem_description(txtItemDescription.getText());

                    try {
                        // SQL UPDATE query
                        String updateQuery = "UPDATE records SET " +
                                "founder_last_name = ?,"+
                                "founder_first_name = ?,"+
                                "found_location = ?, " +
                                "founder_area_inhabitant = ?, " +
                                "founder_father_name = ?, " +
                                "founder_street_address = ?, " +
                                "founder_street_number = ?, "+
                                "founder_id_number = ?, " +
                                "officer_id = ?, " +
                                "founder_telephone = ?, " +
                                "item_description = ?, " +
                                "found_date = ?, " +
                                "found_time = ? "+
                                "WHERE id = ?";

                        // Prepare the statement
                        PreparedStatement updateStmt = connection.prepareStatement(updateQuery);

                        // Bind the parameters (using SQLite-compatible formats)
                        updateStmt.setString(1, txtFounderLastName.getText().trim());
                        updateStmt.setString(2, txtFounderFirstName.getText().trim());
                        updateStmt.setString(3, txtFoundLocation.getText().trim());
                        updateStmt.setString(4, txtFounderAreaInhabitant.getText().trim());
                        updateStmt.setString(5, txtFounderFatherName.getText().trim());
                        updateStmt.setString(6, txtFounderStreetAddress.getText().trim());
                        updateStmt.setString(7, txtFounderStreetNumber.getText().trim());
                        updateStmt.setString(8, txtFounderIdNumber.getText().trim());
                        updateStmt.setInt(9, Integer.parseInt(txtOfficerId.getText()));
                        updateStmt.setString(10, txtFounderTelephone.getText().trim());
                        updateStmt.setString(11, txtItemDescription.getText().trim());

                        // For dates and times, store them as TEXT in SQLite format (YYYY-MM-DD and HH:MM:SS)
                        updateStmt.setString(12, String.valueOf(txtFoundDate.getValue()));  // Date as string in format "YYYY-MM-DD"
                        updateStmt.setString(13, txtFoundTime.getText().trim() + ":00");  // Time as string in format "HH:MM:SS"

                        // Bind the ID as the last parameter
                        updateStmt.setInt(14, Integer.parseInt(txtId.getText()));

                        // Execute the update
                        int rowsAffected = updateStmt.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("Record updated successfully.");
                        } else {
                            System.out.println("No matching record found.");
                        }

                        // Close the statement
                        updateStmt.close();

                    } catch (SQLException ex) {
                        new MessageBoxOk(ex.getMessage());
                        logger.log(Level.FINE, "Error with form record with SQL", ex);

                    } finally {
                        enumFormState = EnumFormState.VIEW;
                        changeState(enumFormState, c);
                    }


                }
                else if(enumFormState==EnumFormState.ADD){
                    UUID uuid = UUID.randomUUID();
                    Record c = new Record();
                    c.setId(Integer.valueOf(txtId.getText()));
                    c.setFound_date(String.valueOf(Date.valueOf(txtFoundDate.getValue())));
                    c.setFound_location(txtFoundLocation.getText());
                    c.setFounder_last_name(txtFounderLastName.getText());
                    c.setFounder_first_name(txtFounderFirstName.getText());
                    c.setFounder_area_inhabitant(txtFounderAreaInhabitant.getText().equals("")?null:(txtFounderAreaInhabitant.getText()));
                    c.setFounder_father_name(txtFounderFatherName.getText().equals("")?null:(txtFounderFatherName.getText()));
                    c.setFounder_street_address(txtFounderStreetAddress.equals("")?null:(txtFounderStreetAddress.getText()));
                    c.setFounder_street_number(txtFounderStreetNumber.equals("")?null:(txtFounderStreetNumber.getText()));
                    c.setFounder_id_number(txtFounderIdNumber.getText());
                    c.setFound_time(Time.valueOf(txtFoundTime.getText()+":00").toString());
                    c.setOfficer_id(Integer.valueOf(txtOfficerId.getText()));
                    c.setFounder_telephone(txtFounderTelephone.getText().equals("")?null: txtFounderTelephone.getText());
                    c.setItem_description(txtItemDescription.getText());
                    c.setUuid(uuid.toString());
                    try {

                        String insertQuery = "INSERT INTO records (" +
                                "found_date, " +
                                "found_time, " +
                                "found_location, " +
                                "founder_area_inhabitant, " +
                                "founder_father_name, " +
                                "founder_street_address, " +
                                "founder_street_number, " +
                                "founder_last_name, " +
                                "founder_first_name, " +
                                "founder_id_number, " +
                                "officer_id, " +
                                "founder_telephone, " +
                                "item_description," +
                                "uuid"+
                                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";


                        PreparedStatement insertStm = conn.prepareStatement(insertQuery);

                        // Bind the parameters (adjusting for SQLite)
                        insertStm.setString(1, c.getFound_date().toString());  // Store date as TEXT in format "YYYY-MM-DD"
                        insertStm.setString(2, c.getFound_time().toString());  // Store time as TEXT in format "HH:MM:SS"
                        insertStm.setString(3, c.getFound_location());
                        insertStm.setObject(4, c.getFounder_area_inhabitant() != null ? c.getFounder_area_inhabitant() : null);
                        insertStm.setObject(5, c.getFounder_father_name() != null ? c.getFounder_father_name() : null);
                        insertStm.setObject(6, c.getFounder_street_address() != null ? c.getFounder_street_address() : null);
                        insertStm.setObject(7, c.getFounder_street_number() != null ? c.getFounder_street_number() : null);
                        insertStm.setString(8, c.getFounder_last_name());
                        insertStm.setString(9, c.getFounder_first_name());
                        insertStm.setString(10, c.getFounder_id_number());
                        insertStm.setInt(11, c.getOfficer_id());
                        insertStm.setObject(12, c.getFounder_telephone() != null ? c.getFounder_telephone() : null);
                        insertStm.setObject(13, c.getItem_description() != null ? c.getItem_description() : null);
                        insertStm.setString(14,c.getUuid());


                        insertStm.executeUpdate();

                        // Close the statement
                        insertStm.close();

                    } catch (SQLException throwables) {
                        new MessageBoxOk(throwables.getMessage());
                        logger.log(Level.SEVERE, "SQL Exception", throwables);

                    } finally {
                        enumFormState = EnumFormState.VIEW;
                        changeState(enumFormState, c);
                        listView.getItems().clear();
                        loadDB(conn);
                        listView.getSelectionModel().select(0);
                    }

   
                }   
            }
        });

        btnCancel.setOnAction(e->{

            if(listView.getItems().isEmpty()){
                enumFormState = EnumFormState.ADD;
                window.close();
            }else{
                enumFormState = EnumFormState.VIEW;
                changeState(enumFormState,listView.getSelectionModel().getSelectedItem());
            }


        });

        loadDB(conn);

        listView.getSelectionModel().selectedItemProperty().addListener((observable,oldVal,newVal) ->{
            load_form(newVal);

        } );



        btnNew.setPrefWidth(listView.getWidth()/3-3);
        btnEdit.setPrefWidth(listView.getWidth()/3-3);
        btnDelete.setPrefWidth(listView.getWidth()/3-3);


        //SAVE CANCEL BUTTON'S
        hboxSaveCancel = new HBox();
       // hboxSaveCancel.setVisible(false);
        hboxSaveCancel.getChildren().addAll(btnSave,btnCancel);
        hboxSaveCancel.setSpacing(10);
        hboxSaveCancel.setAlignment(Pos.CENTER);

        gridPane.add(hboxSaveCancel,2,15);
        GridPane.setRowSpan(hboxSaveCancel,2);

        GridPane.setColumnSpan(hboxSaveCancel, 2);
        GridPane.setHalignment(hboxSaveCancel, HPos.CENTER);
        gridPane.setPrefWidth(700);
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setPadding(new Insets(5));

        window = new Stage();
        window.setTitle("Records");

        scene = new Scene(gridPane);


        scene.heightProperty().addListener((observable,oldValue,newValue)->{
            gridPane.setPrefHeight(scene.getHeight());
            listView.setPrefHeight(scene.getHeight());
        });
        scene.widthProperty().addListener((observable,oldValue,newValue)->{
            gridPane.setPrefWidth(scene.getWidth());
            listView.setPrefWidth(scene.getWidth());
        });

        scene.widthProperty().addListener((ob,oldValue,newValue)->{
            gridPane.setPrefWidth(scene.getWidth());
        });



        window.setHeight(800);
        window.setWidth(700);
        window.setMinHeight(550);
        window.setMinWidth(450);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setX((Screen.getPrimary().getVisualBounds().getWidth()-700)/2);
        window.setY((Screen.getPrimary().getVisualBounds().getHeight()-600)/2);

        window.setScene(scene);

        listView.getSelectionModel().select(0);
        enumFormState = EnumFormState.VIEW;
        changeState(enumFormState,listView.getSelectionModel().getSelectedItem());

        window.show();

    }



    private void changeState(EnumFormState fs, Record c){
       switch (fs){
           case ADD :
               txtId.setEditable(false);
               txtOfficerId.setEditable(false);
               txtDate.setEditable(false);
               txtFoundDate.setEditable(true);
               txtFoundTime.setEditable(true);
               txtFounderAreaInhabitant.setEditable(true);
               txtFounderFatherName.setEditable(true);
               txtFounderFirstName.setEditable(true);
               txtFounderLastName.setEditable(true);
               txtFounderTelephone.setEditable(true);
               txtFoundLocation.setEditable(true);
               txtItemDescription.setEditable(true);
               txtFounderIdNumber.setEditable(true);
               txtFounderStreetNumber.setEditable(true);
               txtFounderStreetAddress.setEditable(true);


               Integer intLast = null;


               try {
                   // Query to get the last inserted row ID
                   Statement idStm = connection.createStatement();
                   ResultSet resultSet = idStm.executeQuery("SELECT last_insert_rowid()");

                   // If there's a result, set the last inserted ID
                   if (resultSet.next()) {
                       intLast = resultSet.getInt(1);
                   }

                   // Close the result set and statement
                   resultSet.close();
                   idStm.close();

               } catch (SQLException sqlException) {
                   new MessageBoxOk(sqlException.getMessage());
                   logger.log(Level.SEVERE, "SQL Exception", sqlException);
               }

// Handle the case where the last ID couldn't be retrieved
               if (intLast == null) {
                   logger.log(Level.SEVERE, "Couldn't retrieve the last inserted ID from records table.");
               } else {
                   logger.log(Level.INFO, "The last inserted ID is: " + intLast);
               }




               txtId.setText(String.valueOf((intLast+1)));
               txtOfficerId.setText(String.valueOf(currUser.getAm()));

               LocalDate currentDate = LocalDate.now();
               txtDate.setValue(currentDate);

               txtFoundDate.setValue(LocalDate.parse(currentDate.toString()));
               txtFoundTime.setText(LocalTime.now().withSecond(0).withNano(0).minusMinutes(30).toString());
               txtFounderAreaInhabitant.setText("");
               txtFounderFatherName.setText("");
               txtFounderFirstName.setText("");
               txtFounderLastName.setText("");
               txtFounderTelephone.setText("");
               txtFoundLocation.setText("");
               txtItemDescription.setText("");
               txtFounderIdNumber.setText("");
               txtFounderStreetAddress.setText("");
               txtFounderStreetNumber.setText("");

               hboxSaveCancel.setVisible(true);
               listView.setDisable(true);
               btnNew.setDisable(true);
               btnEdit.setDisable(true);
               btnDelete.setDisable(true);
               btnReturn.setDisable(true);
               btnPrint.setDisable(true);

               break;
           case EDIT:
               txtId.setEditable(false);
               txtOfficerId.setEditable(false);
               txtDate.setEditable(false);
               txtFoundDate.setEditable(true);
               txtFounderAreaInhabitant.setEditable(true);
               txtFounderFatherName.setEditable(true);
               txtFounderFirstName.setEditable(true);
               txtFounderLastName.setEditable(true);
               txtFounderTelephone.setEditable(true);
               txtFoundLocation.setEditable(true);
               txtItemDescription.setEditable(true);
               txtFounderIdNumber.setEditable(true);
               txtFounderStreetAddress.setEditable(true);
               txtFounderStreetNumber.setEditable(true);
               txtFoundTime.setEditable(true);

               load_form(c);

               listView.setDisable(true);
               hboxSaveCancel.setVisible(true);
               btnNew.setDisable(true);
               btnEdit.setDisable(true);
               btnDelete.setDisable(true);
               btnReturn.setDisable(true);
               btnPrint.setDisable(true);
               break;

           case VIEW:
               txtId.setEditable(false);
               txtOfficerId.setEditable(false);
               txtDate.setEditable(false);
               txtFoundDate.setEditable(false);
               txtFoundTime.setEditable(false);
               txtFounderAreaInhabitant.setEditable(false);
               txtFounderFatherName.setEditable(false);
               txtFounderFirstName.setEditable(false);
               txtFounderLastName.setEditable(false);
               txtFounderTelephone.setEditable(false);
               txtFoundLocation.setEditable(false);
               txtItemDescription.setEditable(false);
               txtFounderIdNumber.setEditable(false);
               txtFounderStreetNumber.setEditable(false);
               txtFounderStreetAddress.setEditable(false);

               load_form(c);
               hboxSaveCancel.setVisible(false);
               listView.setDisable(false);
               btnNew.setDisable(false);
               btnEdit.setDisable(false);
               btnDelete.setDisable(false);
               btnReturn.setDisable(false);
               btnPrint.setDisable(false);
               break;

       }
    }




    public void loadDB(Connection conn) {
        Statement stm = null;
        String limit;
        try {
            stm = conn.createStatement();

            // Set limit based on user role
            switch (currUser.getRole()) {
                case "user":
                    limit = " LIMIT 5000";
                    break;
                case "admin":
                    limit = " LIMIT 1500000"; // SQLite has no hard limit on max rows, but consider performance
                    break;
                default:
                    limit = "";
            }

            // Build the query with appropriate limit
            String query = "SELECT * FROM records ORDER BY id DESC" + limit;
            ResultSet resultSet = stm.executeQuery(query);

            // Process the result set
            while (resultSet.next()) {

                LocalDateTime recordDateTime = resultSet.getTimestamp("record_datetime").toLocalDateTime();
                String recordDate = recordDateTime.toLocalDate().toString();
                String recordTime = recordDateTime.toLocalTime().withSecond(0).toString();

                Record record = new Record(
                        resultSet.getString("uuid"),
                        resultSet.getInt("id"),
                        recordDate,
                        recordTime,
                        resultSet.getInt("officer_id"),
                        resultSet.getString("founder_last_name"),
                        resultSet.getString("founder_first_name"),
                        resultSet.getString("founder_id_number"),
                        resultSet.getString("founder_telephone"),
                        resultSet.getString("founder_street_address"),
                        resultSet.getString("founder_street_number"),
                        resultSet.getString("founder_father_name"),
                        resultSet.getString("founder_area_inhabitant"),
                        resultSet.getString("found_date"),
                        resultSet.getString("found_time"),
                        resultSet.getString("found_location"),
                        resultSet.getString("item_description")
                );

                listView.getItems().add(record);
            }
        } catch (SQLException e) {
            new MessageBoxOk(e.getMessage());
            logger.log(Level.SEVERE, "SQL Exception ", e);
        } finally {
            // Ensure resources are cleaned up
            try {
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Failed to close statement", e);
            }
        }
    }

    private void generatedBarcode(String data){
        int width = 400;
        int height = 200;
        String format = "png";
        String content = data;
        Map<EncodeHintType,Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, 4);

        try {
            PDF417Writer pdf417Writer = new PDF417Writer();
            BitMatrix bitMatrix = pdf417Writer.encode(content, BarcodeFormat.PDF_417, width, height, hintMap);
            File outputFile = new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+"export_data"+File.separator+"barcode_"+LocalDateTime.now()+".png");
            MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"BARCODE CREATED");
            alert.showAndWait();
            Desktop.getDesktop().open(outputFile);
        } catch (WriterException | IOException e) {
           new MessageBoxOk("Error: " + e.getMessage());
            logger.log(Level.SEVERE,"Error ",e);

        }
    }

    private void load_form(Record c){
        if(c!=null){
            txtId.setText(String.valueOf(c.getId()));

            //Extract the date only that made the record
            String recordDate = c.getRecord_date() != null ? c.getRecord_date() : LocalDate.now().toString();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");


            txtDate.setValue(LocalDate.parse(recordDate));

            txtOfficerId.setText(String.valueOf(c.getOfficer_id()));
            txtFounderLastName.setText(c.getFounder_last_name());
            txtFounderFirstName.setText(c.getFounder_first_name());
            txtFounderIdNumber.setText(c.getFounder_id_number());
            txtFounderTelephone.setText(c.getFounder_telephone()==null?"":c.getFounder_telephone());
            txtFounderStreetAddress.setText(c.getFounder_street_address()==null?"":c.getFounder_street_address());
            txtFounderStreetNumber.setText(c.getFounder_street_number()==null?"":c.getFounder_street_number());
            txtFounderFatherName.setText(c.getFounder_father_name()==null?"":c.getFounder_father_name());
            txtFounderAreaInhabitant.setText(c.getFounder_area_inhabitant()==null?"":c.getFounder_area_inhabitant());
            txtFoundDate.setValue(LocalDate.parse(c.getFound_date()));
            txtFoundLocation.setText(c.getFound_location());
            txtItemDescription.setText(c.getItem_description());
            txtFoundTime.setText(c.getFound_time().substring(0,c.getFound_time().lastIndexOf(":00")));

        }

    }


    public boolean validateForm(){
       String errors = "";

        if (txtFounderLastName.getText().isEmpty()) {
            errors += "Το επώνυμο πρέπει να συμπληρωθεί.\n";
        }
        if (txtFounderFirstName.getText().isEmpty()) {
            errors += "Το όνομα πρέπει να συμπληρωθεί.\n";
        }
        if (txtFounderIdNumber.getText().isEmpty()) {
            errors += "Ο αριθμός ταυτότητας του ιδρυτή πρέπει να συμπληρωθεί.\n";
        }
        if (txtFoundLocation.getText().isEmpty()) {
            errors += "Η τοποθεσία εύρεσης πρέπει να συμπληρωθεί.\n";
        }
        if (txtItemDescription.getText().isEmpty()) {
            errors += "Η περιγραφή του αντικειμένου πρέπει να συμπληρωθεί.\n";
        }
//        if (txtFoundDate.getValue() || !txtFoundDate.getText().matches("^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) {
//            errors += "Η ημερομηνία εύρεσης πρέπει να συμπληρωθεί και να είναι στη μορφή yyyy-MM-dd (π.χ. 2024-12-31).\n";
//        }

        if (txtDate.getValue() == null ) {
            errors += "Η ημερομηνία πρέπει να συμπληρωθεί.\n";
        }

        if (txtOfficerId.getText().isEmpty() || !txtOfficerId.getText().matches("\\d+")) {
            errors += "Το ID του αξιωματικού πρέπει να είναι αριθμός και να συμπληρωθεί.\n";
        }
        if (txtFoundTime.getText().isEmpty() || !txtFoundTime.getText().matches("^([01][0-9]|2[0-3]):[0-5][0-9]$")) {
            errors += "Η ώρα εύρεσης πρέπει να συμπληρωθεί και να είναι στη μορφή HH:mm.\n";
        }



        if (errors.isEmpty()) {
            return true;
        } else {
            new MessageBoxOk(errors);
            return false;
        }
    }



    }







