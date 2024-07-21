package com.charamidis.lostAndFound.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ConnectionStatusIndicator extends Region {
    private Circle circle;

    public ConnectionStatusIndicator() {
        circle = new Circle(8);
        circle.setFill(Color.RED);

        getChildren().addAll(circle);
    }

    public void setStatus(Status status) {
        switch (status) {
            case ONLINE:
                circle.setFill(Color.GREEN);
                break;
            case PENDING:
                circle.setFill(Color.YELLOW);
                break;
            case OFFLINE:
                circle.setFill(Color.RED);
                break;
        }
    }

    public enum Status {
        ONLINE, PENDING, OFFLINE
    }
}
