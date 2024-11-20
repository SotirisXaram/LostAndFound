package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.utils.AppLogger;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AddUser {

    Stage window;
    Scene scene;
    VBox vbox;
    HBox hbox;
    Button btnSave, btnCancel;
    TextField txtFirstName, txtLastName, txtAm, txtDateOfBirth, txtPassword;
    ComboBox<String> comboBox;
    DatePicker datePicker;
    Label lblFirstName, lblLastName, lblAm, lblDateOfBirth, lblPassword, lblRole;
    Connection connection;
    private static final Logger logger = AppLogger.getLogger();

    public AddUser(Connection conn) {
        connection = conn;
        txtFirstName = new TextField();
        txtLastName = new TextField();
        txtAm = new TextField();
        txtPassword = new TextField();
        comboBox = new ComboBox<>();
        comboBox.getItems().addAll("user", "admin");
        comboBox.setValue("user");


        txtDateOfBirth = new TextField();
        lblDateOfBirth = new Label(" ΗΜΕΡ/ΝΙΑ ΓΕΝΝΗΣΗΣ: ");
        datePicker = new DatePicker();

        txtFirstName.setPromptText(" ΟΝΟΜΑ ");
        txtLastName.setPromptText(" ΕΠΩΝΥΜΟ ");
        txtPassword.setPromptText(" ΚΩΔΙΚΟΣ ");
        txtAm.setPromptText(" ΑΡΙΘΜΟΣ ΜΗΤΡΩΟΥ ");
        btnCancel = new Button("Cancel");
        btnSave = new Button("Save");




        hbox = new HBox(10);
        hbox.getChildren().addAll(btnSave, btnCancel);
        hbox.setAlignment(Pos.CENTER);

        vbox = new VBox(15);
        vbox.getChildren().addAll(txtAm, txtLastName, txtFirstName, txtPassword, lblDateOfBirth, datePicker, comboBox, hbox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        scene = new Scene(vbox, 450, 600);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("styles/style.css").toExternalForm());

        window = new Stage();
        window.setTitle("Προσθήκη Χρήστη");
        window.initModality(Modality.APPLICATION_MODAL);

        btnCancel.setOnAction(e->{
            window.close();
        });

        btnSave.setOnAction(e->{
            if(!txtAm.getText().matches("^\\d+$") || txtFirstName.getText().equals("") || txtLastName.getText().equals("") || txtAm.getText().equals("") || txtPassword.getText().equals("") || comboBox.getValue().equals("")){
                new MessageBoxOk("ΤΑ ΠΕΔΙΑ ΠΡΕΠΕΙ ΝΑ ΣΥΜΠΛΗΡΩΘΟΥΝ ΚΑΙ Ο ΑΡΙΘΜΟΣ ΜΗΤΡΩΟΥ ΠΡΕΠΕΙ ΝΑ ΕΙΝΑΙ ΑΚΕΡΑΙΟΣ");
            }else {
                try{
                    String hashedPassword = BCrypt.hashpw(txtPassword.getText(), BCrypt.gensalt());
                    PreparedStatement stm = connection.prepareStatement("INSERT INTO users (am,first_name,last_name,date_of_birth,role,password) VALUES (?,?,?,?,?,?)");
                    stm.setInt(1,Integer.parseInt(txtAm.getText()));
                    stm.setString(2,txtFirstName.getText());
                    stm.setString(3,txtLastName.getText());
                    stm.setString(4, String.valueOf(Date.valueOf(datePicker.getValue().toString())));
                    stm.setString(5,comboBox.getValue());
                    stm.setString(6,hashedPassword);
                    stm.executeUpdate();
                    stm.close();

                    new MessageBoxOk("User added successfully");
                    window.close();

                }catch (SQLException exception){
                    new MessageBoxOk(exception.getMessage());
                    logger.log(Level.SEVERE,"Cant add user ",exception);
                }
            }

        });


        window.setWidth(450);
        window.setHeight(600);
        window.setScene(scene);

        window.show();

    }

}