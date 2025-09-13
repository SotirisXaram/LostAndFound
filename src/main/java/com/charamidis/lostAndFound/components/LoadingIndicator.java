package com.charamidis.lostAndFound.components;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class LoadingIndicator {
    private Stage loadingStage;
    private Circle[] dots;
    private Label messageLabel;
    private Timeline animation;
    
    public LoadingIndicator() {
        createLoadingStage();
    }
    
    private void createLoadingStage() {
        loadingStage = new Stage();
        loadingStage.initStyle(StageStyle.UNDECORATED);
        loadingStage.initModality(Modality.APPLICATION_MODAL);
        loadingStage.setResizable(false);
        
        // Create modern loading dots
        createLoadingDots();
        
        // Create message label
        messageLabel = new Label("Φόρτωση...");
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        messageLabel.setTextFill(Color.rgb(52, 73, 94));
        
        // Create container
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new javafx.geometry.Insets(40, 30, 40, 30));
        container.getChildren().addAll(createDotsContainer(), messageLabel);
        
        // Create main layout with modern design
        StackPane root = new StackPane();
        root.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
            "-fx-background-radius: 20; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 20, 0, 0, 5);"
        );
        root.getChildren().add(container);
        
        // Create scene
        Scene scene = new Scene(root, 280, 180);
        scene.setFill(Color.TRANSPARENT);
        
        loadingStage.setScene(scene);
        
        // Create modern animation
        createModernAnimation();
    }
    
    private void createLoadingDots() {
        dots = new Circle[3];
        for (int i = 0; i < 3; i++) {
            dots[i] = new Circle(8);
            dots[i].setFill(Color.WHITE);
            dots[i].setOpacity(0.3);
        }
    }
    
    private HBox createDotsContainer() {
        HBox dotsContainer = new HBox(15);
        dotsContainer.setAlignment(Pos.CENTER);
        
        for (Circle dot : dots) {
            dotsContainer.getChildren().add(dot);
        }
        
        return dotsContainer;
    }
    
    private void createModernAnimation() {
        animation = new Timeline();
        
        // Simple pulsing animation for all dots
        KeyFrame startFrame = new KeyFrame(Duration.ZERO,
            new KeyValue(dots[0].opacityProperty(), 0.3),
            new KeyValue(dots[1].opacityProperty(), 0.3),
            new KeyValue(dots[2].opacityProperty(), 0.3)
        );
        
        KeyFrame middleFrame = new KeyFrame(Duration.seconds(0.5),
            new KeyValue(dots[0].opacityProperty(), 1.0),
            new KeyValue(dots[1].opacityProperty(), 1.0),
            new KeyValue(dots[2].opacityProperty(), 1.0)
        );
        
        KeyFrame endFrame = new KeyFrame(Duration.seconds(1.0),
            new KeyValue(dots[0].opacityProperty(), 0.3),
            new KeyValue(dots[1].opacityProperty(), 0.3),
            new KeyValue(dots[2].opacityProperty(), 0.3)
        );
        
        animation.getKeyFrames().addAll(startFrame, middleFrame, endFrame);
        animation.setCycleCount(Animation.INDEFINITE);
    }
    
    public void show(String message) {
        Platform.runLater(() -> {
            messageLabel.setText(message);
            loadingStage.centerOnScreen();
            loadingStage.show();
            animation.play();
        });
    }
    
    public void show() {
        show("Φόρτωση...");
    }
    
    public void hide() {
        Platform.runLater(() -> {
            if (animation != null) {
                animation.stop();
            }
            if (loadingStage != null) {
                loadingStage.hide();
            }
        });
    }
    
    public void updateMessage(String message) {
        Platform.runLater(() -> {
            if (messageLabel != null) {
                messageLabel.setText(message);
            }
        });
    }
    
    public boolean isShowing() {
        return loadingStage != null && loadingStage.isShowing();
    }
    
    // Static methods for easy access
    private static LoadingIndicator instance;
    
    public static void showLoading(String message) {
        if (instance == null) {
            instance = new LoadingIndicator();
        }
        instance.show(message);
    }
    
    public static void showLoading() {
        showLoading("Φόρτωση...");
    }
    
    public static void hideLoading() {
        if (instance != null) {
            instance.hide();
        }
    }
    
    public static void updateLoadingMessage(String message) {
        if (instance != null) {
            instance.updateMessage(message);
        }
    }
    
    public static boolean isLoading() {
        return instance != null && instance.isShowing();
    }
    
    // Method to force hide loading (in case it gets stuck)
    public static void forceHideLoading() {
        if (instance != null) {
            Platform.runLater(() -> {
                if (instance.animation != null) {
                    instance.animation.stop();
                }
                if (instance.loadingStage != null) {
                    instance.loadingStage.hide();
                }
                instance = null; // Reset instance
            });
        }
    }
}
