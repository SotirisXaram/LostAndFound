package com.charamidis.lostAndFound.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ConnectionStatusIndicator extends Region {
    private Circle circle;
    private Label label;

    public ConnectionStatusIndicator() {
        circle = new Circle(8);
        circle.setFill(Color.RED);

        label = new Label("Connection");
        label.setAlignment(Pos.CENTER);

        getChildren().addAll(circle, label);
    }

    public void setStatus(Status status) {
        switch (status) {
            case ONLINE:
                circle.setFill(Color.GREEN);
                label.setText("Online");
                break;
            case PENDING:
                circle.setFill(Color.YELLOW);
                label.setText("Pending");
                break;
            case OFFLINE:
                circle.setFill(Color.RED);
                label.setText("Offline");
                break;
        }
    }

    public enum Status {
        ONLINE, PENDING, OFFLINE
    }
}
