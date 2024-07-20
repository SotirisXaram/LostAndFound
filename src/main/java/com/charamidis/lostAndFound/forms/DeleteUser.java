package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;

import java.sql.*;

public class DeleteUser {
    Connection finalConn;
    TextField textFieldAm;
    Label lblAm;
    HBox hBox;
    VBox vBox;
    HBox hboxBtns;
    Scene scene;
    Button btnDelete,btnCancel;
    Stage stage;
    public DeleteUser(Connection conn){
        finalConn = conn;
        textFieldAm = new TextField();
        textFieldAm.setPromptText("Δώστε τον αριθμό μητρώου χρήστη");
        lblAm = new Label("AM:");
        btnDelete = new Button("Διαγραφή");
        btnDelete.setDefaultButton(true);
        btnDelete.setOnAction(e->{
            PreparedStatement stm = null;
            if(!textFieldAm.getText().isEmpty()) {
                try {
                    stm = conn.prepareStatement("DELETE FROM users WHERE am = ?");
                    stm.setInt(1,Integer.parseInt(textFieldAm.getText()));
                    int rows =  stm.executeUpdate();
                    stm.close();
                    if(rows>0){
                        new MessageBoxOk("Ο χρήστης διαγράφηκε");
                        stage.close();
                    }else{
                        new MessageBoxOk("Ο χρήστης δεν βρέθηκε");
                    }

                } catch (SQLException |  NumberFormatException ex ) {
                    new MessageBoxOk("Δωστε σε μορφή ακέραιου αριθμού"+"\n"+ex.getMessage());
                }
            }else{
                new MessageBoxOk("Δώστε τον αριθμό μητρώου");
            }


        });
        btnCancel = new Button("Ακύρωση");
        btnCancel.setOnAction(e->{
            stage.close();
        });

        hBox = new HBox();
        hboxBtns = new HBox();

        hBox.getChildren().addAll(lblAm,textFieldAm);
        hboxBtns.getChildren().addAll(btnDelete,btnCancel);
        hboxBtns.setAlignment(Pos.CENTER);
        hboxBtns.setSpacing(5);
        hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER);

        vBox = new VBox();
        vBox.getChildren().addAll(hBox,hboxBtns);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);

        scene = new Scene(vBox);


        stage = new Stage();
        stage.setTitle("Διαγραφή Χρήστη");
        stage.setWidth(400);
        stage.setHeight(300);

        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();

    }
}
