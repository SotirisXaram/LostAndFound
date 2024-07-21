package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.utils.MessageBoxOk;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteReturn {
    Connection finalConn;
    Button btnSave,btnCancel;
    VBox vBox;
    HBox hbox;
    Scene scene;
    Stage window;
    HBox hboxBtn ;
    Label lblId ;
    TextField txtId;




    public DeleteReturn(Connection conn){
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
            MessageBoxOk msg =  new MessageBoxOk("Θέλετε σίγουρα να διαγράψετε την επιστροφή? \n");
            boolean resp = msg.getRes();
            if(resp){
                if(txtId.getText().trim().isEmpty()){
                    new MessageBoxOk("Δώστε αριθμό");
                }else{
                    if(txtId.getText().trim().matches("[0-9]+")){
                        String deleteQuery = "DELETE FROM returns WHERE id = ?";
                        try {
                            PreparedStatement stm = finalConn.prepareStatement(deleteQuery);
                            stm.setInt(1, Integer.parseInt(txtId.getText().trim()));
                            stm.executeUpdate();
                            stm.close();
                            new MessageBoxOk("Η επιστροφή έχει διαγραφή.");
                            window.close();
                        } catch (SQLException ex) {
                            new MessageBoxOk("Σφάλμα κατά την διαγραφή.");
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
        window.setTitle("Διαγραφή Επιστροφής");
        window.setScene(scene);
        window.setHeight(400);
        window.setWidth(450);
        window.initModality(Modality.APPLICATION_MODAL);

        window.show();




    }
}
