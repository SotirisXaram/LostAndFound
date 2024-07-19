package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.ss.formula.functions.T;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResetId {
    Connection finalConn;
    Button btnSave,btnCancel;
    VBox vBox;
    HBox hbox;
    Scene scene;
    Stage window;
    HBox hboxBtn ;
    Label lblId ;
    TextField txtId;




    public  ResetId(Connection conn){
        finalConn = conn;
        btnCancel = new Button("Ακύρωση");
        btnSave = new Button("Αλλαγή");
        hboxBtn = new HBox();
        hboxBtn.getChildren().addAll(btnSave,btnCancel);
        hbox = new HBox();
        lblId = new Label("ID:");
        txtId = new TextField();
        vBox = new VBox();

        hbox.setAlignment(Pos.CENTER);
        hboxBtn.setAlignment(Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(lblId,txtId);

        vBox.getChildren().addAll(hbox,hboxBtn);

        btnCancel.setOnAction(e->{
            window.close();
        });

        btnSave.setOnAction(e->{
           MessageBoxOk msg =  new MessageBoxOk("Θέλετε σίγουρα να αλλάξετε το ID ? \n Παρακαλούμε να λάβετε υπόψη ότι η χειροκίνητη αλλαγή αναγνωριστικών βάσης δεδομένων μπορεί να έχει σοβαρές συνέπειες και πρέπει να προσεγγίζεται με προσοχή.");
           boolean resp = msg.getRes();
           if(resp){
               if(txtId.getText().trim().isEmpty()){
                   new MessageBoxOk("Δώστε αριθμό");
               }else{
                    if(txtId.getText().trim().matches("[0-9]+")){
                        String resetQuery = "ALTER SEQUENCE records_id_seq RESTART WITH "+txtId.getText().trim();
                        try {
                            PreparedStatement pstmt = finalConn.prepareStatement(resetQuery);
                            pstmt.executeUpdate();
                            pstmt.close();
                            new MessageBoxOk("Το ID έχει επαναφερθεί.");
                            window.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            MessageBoxOk errorMsg = new MessageBoxOk("Σφάλμα κατά την επαναφορά του ID.");
                        }

                    }else {
                        new MessageBoxOk("Δώστε ακέραιο αριθμό ");
                    }
               }

           }else{
               window.close();
           }
        });



        scene = new Scene(vBox);
        window = new Stage();
        window.setTitle("Αλλαγή ID");
        window.setScene(scene);
        window.setHeight(400);
        window.setWidth(450);
        window.initModality(Modality.APPLICATION_MODAL);

        window.show();




    }
}
