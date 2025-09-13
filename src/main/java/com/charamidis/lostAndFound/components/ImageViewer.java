package com.charamidis.lostAndFound.components;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ImageViewer {
    private Stage viewerStage;
    private ImageView imageView;
    private Image originalImage;
    private double currentScale = 1.0;
    private double minScale = 0.1;
    private double maxScale = 5.0;
    private double scaleFactor = 1.2;
    
    public ImageViewer(Image image) {
        this.originalImage = image;
        createViewer();
    }
    
    private void createViewer() {
        // Create the main stage
        viewerStage = new Stage();
        viewerStage.initStyle(StageStyle.UNDECORATED);
        viewerStage.initModality(Modality.APPLICATION_MODAL);
        viewerStage.setTitle("Image Viewer");
        
        // Create the image view
        imageView = new ImageView(originalImage);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        
        // Set initial size to fit screen
        fitToScreen();
        
        // Create control buttons
        Button zoomInBtn = createButton("+", "Zoom In");
        Button zoomOutBtn = createButton("-", "Zoom Out");
        Button fitToScreenBtn = createButton("⌂", "Fit to Screen");
        Button closeBtn = createButton("✕", "Close");
        
        // Set button actions
        zoomInBtn.setOnAction(e -> zoomIn());
        zoomOutBtn.setOnAction(e -> zoomOut());
        fitToScreenBtn.setOnAction(e -> fitToScreen());
        closeBtn.setOnAction(e -> closeViewer());
        
        // Create control panel
        HBox controlPanel = new HBox(10);
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setPadding(new Insets(10));
        controlPanel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        controlPanel.getChildren().addAll(zoomInBtn, zoomOutBtn, fitToScreenBtn, closeBtn);
        
        // Create main layout
        BorderPane root = new BorderPane();
        root.setCenter(imageView);
        root.setBottom(controlPanel);
        root.setStyle("-fx-background-color: black;");
        
        // Create scene
        Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.BLACK);
        
        // Add keyboard shortcuts
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                closeViewer();
            } else if (e.getCode() == KeyCode.PLUS || e.getCode() == KeyCode.EQUALS) {
                zoomIn();
            } else if (e.getCode() == KeyCode.MINUS) {
                zoomOut();
            } else if (e.getCode() == KeyCode.F) {
                fitToScreen();
            }
        });
        
        // Add mouse wheel zoom
        imageView.setOnScroll(e -> {
            if (e.getDeltaY() > 0) {
                zoomIn();
            } else {
                zoomOut();
            }
        });
        
        // Add double-click to fit to screen
        imageView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                fitToScreen();
            }
        });
        
        // Add drag functionality
        setupDragFunctionality();
        
        viewerStage.setScene(scene);
        
        // Center the window
        viewerStage.centerOnScreen();
    }
    
    private Button createButton(String text, String tooltip) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.2);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 12;" +
            "-fx-background-radius: 4;" +
            "-fx-border-radius: 4;" +
            "-fx-cursor: hand;"
        );
        button.setTooltip(new javafx.scene.control.Tooltip(tooltip));
        
        // Hover effects
        button.setOnMouseEntered(e -> 
            button.setStyle(button.getStyle() + "-fx-background-color: rgba(255, 255, 255, 0.4);")
        );
        button.setOnMouseExited(e -> 
            button.setStyle(button.getStyle().replace("-fx-background-color: rgba(255, 255, 255, 0.4);", 
                "-fx-background-color: rgba(255, 255, 255, 0.2);"))
        );
        
        return button;
    }
    
    private void setupDragFunctionality() {
        final double[] lastX = new double[1];
        final double[] lastY = new double[1];
        final boolean[] isDragging = {false};
        
        imageView.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                lastX[0] = e.getSceneX();
                lastY[0] = e.getSceneY();
                isDragging[0] = true;
            }
        });
        
        imageView.setOnMouseDragged(e -> {
            if (isDragging[0]) {
                double deltaX = e.getSceneX() - lastX[0];
                double deltaY = e.getSceneY() - lastY[0];
                
                imageView.setTranslateX(imageView.getTranslateX() + deltaX);
                imageView.setTranslateY(imageView.getTranslateY() + deltaY);
                
                lastX[0] = e.getSceneX();
                lastY[0] = e.getSceneY();
            }
        });
        
        imageView.setOnMouseReleased(e -> {
            isDragging[0] = false;
        });
    }
    
    private void zoomIn() {
        if (currentScale < maxScale) {
            currentScale *= scaleFactor;
            applyZoom();
        }
    }
    
    private void zoomOut() {
        if (currentScale > minScale) {
            currentScale /= scaleFactor;
            applyZoom();
        }
    }
    
    private void applyZoom() {
        double newWidth = originalImage.getWidth() * currentScale;
        double newHeight = originalImage.getHeight() * currentScale;
        
        imageView.setFitWidth(newWidth);
        imageView.setFitHeight(newHeight);
    }
    
    private void fitToScreen() {
        currentScale = 1.0;
        imageView.setTranslateX(0);
        imageView.setTranslateY(0);
        
        // Calculate the scale to fit the screen
        double screenWidth = viewerStage.getWidth() - 20; // Account for padding
        double screenHeight = viewerStage.getHeight() - 100; // Account for control panel
        
        double scaleX = screenWidth / originalImage.getWidth();
        double scaleY = screenHeight / originalImage.getHeight();
        double scale = Math.min(scaleX, scaleY);
        
        imageView.setFitWidth(originalImage.getWidth() * scale);
        imageView.setFitHeight(originalImage.getHeight() * scale);
        currentScale = scale;
    }
    
    private void closeViewer() {
        if (viewerStage != null) {
            viewerStage.close();
        }
    }
    
    public void show() {
        if (viewerStage != null) {
            viewerStage.show();
        }
    }
    
    public static void showImage(Image image) {
        if (image != null) {
            Platform.runLater(() -> {
                ImageViewer viewer = new ImageViewer(image);
                viewer.show();
            });
        }
    }
}
