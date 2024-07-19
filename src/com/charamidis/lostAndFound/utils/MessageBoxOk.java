package com.charamidis.lostAndFound.utils;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class MessageBoxOk {

    Label lblMessage;
    Button btnOK;
    VBox vbox;
    Scene scene;
    Stage errorWindow;
    private boolean resp;

    public MessageBoxOk(String errors) {


        lblMessage = new Label(errors);

        btnOK = new Button("OK");
        btnOK.setDefaultButton(true);

        btnOK.setOnAction(e -> {
            setResp(true);
            errorWindow.close();
        });

        vbox = new VBox();
        vbox.getChildren().addAll(lblMessage, btnOK);
        vbox.setAlignment(Pos.CENTER);

        scene = new Scene(vbox);

        errorWindow = new Stage();
        errorWindow.initModality(Modality.APPLICATION_MODAL);
        errorWindow.setScene(scene);
        errorWindow.showAndWait();
    }

    public boolean getRes(){
        return this.resp;
    }

    protected void setResp(boolean resp){
        this.resp = resp;
    }

}
