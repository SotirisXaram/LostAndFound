package com.charamidis.lostAndFound.utils;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ImageManager {
    private static final String DESKTOP_PATH = System.getProperty("user.home") + File.separator + "Desktop";
    private static final String DATA_FOLDER = DESKTOP_PATH + File.separator + "export_data";
    private static final String IMAGES_FOLDER = "images";
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final int MAX_IMAGE_WIDTH = 800; // Maximum width for compressed images
    private static final int MAX_IMAGE_HEIGHT = 600; // Maximum height for compressed images
    private static final float JPEG_QUALITY = 0.8f; // JPEG compression quality (0.0 to 1.0)

    /**
     * Initialize the data folder structure
     */
    public static void initializeDataFolder() {
        try {
            Path dataPath = Paths.get(DATA_FOLDER);
            Path imagesPath = Paths.get(DATA_FOLDER, IMAGES_FOLDER);
            
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
                System.out.println("Created data folder: " + dataPath.toAbsolutePath());
            }
            
            if (!Files.exists(imagesPath)) {
                Files.createDirectories(imagesPath);
                System.out.println("Created images folder: " + imagesPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error creating data folder structure: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Show file chooser for image selection
     */
    public static File selectImageFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Item Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        
        return fileChooser.showOpenDialog(stage);
    }

    /**
     * Compress and resize image
     */
    private static BufferedImage compressImage(BufferedImage originalImage) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        // Calculate new dimensions while maintaining aspect ratio
        int newWidth = originalWidth;
        int newHeight = originalHeight;
        
        if (originalWidth > MAX_IMAGE_WIDTH || originalHeight > MAX_IMAGE_HEIGHT) {
            double widthRatio = (double) MAX_IMAGE_WIDTH / originalWidth;
            double heightRatio = (double) MAX_IMAGE_HEIGHT / originalHeight;
            double ratio = Math.min(widthRatio, heightRatio);
            
            newWidth = (int) (originalWidth * ratio);
            newHeight = (int) (originalHeight * ratio);
        }
        
        // Create compressed image
        BufferedImage compressedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = compressedImage.createGraphics();
        
        // Enable high-quality rendering
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw the resized image
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        
        return compressedImage;
    }

    /**
     * Copy image file to data/images folder and return the relative path
     */
    public static String copyImageToDataFolder(File sourceFile, Integer recordId) {
        if (sourceFile == null || !sourceFile.exists()) {
            return null;
        }

        try {
            // Validate file extension
            String fileName = sourceFile.getName().toLowerCase();
            boolean validExtension = ALLOWED_EXTENSIONS.stream().anyMatch(fileName::endsWith);
            if (!validExtension) {
                showError("Invalid file type", "Please select a valid image file (JPG, PNG, GIF, BMP)");
                return null;
            }

            // Validate file size
            if (sourceFile.length() > MAX_FILE_SIZE) {
                showError("File too large", "Image file must be smaller than 10MB");
                return null;
            }

            // Generate unique filename (always save as JPG for compression)
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String newFileName = String.format("record_%d_%s.jpg", recordId, timestamp);
            
            // Create destination path
            Path destinationPath = Paths.get(DATA_FOLDER, IMAGES_FOLDER, newFileName);
            
            // Load and compress image
            BufferedImage originalImage = ImageIO.read(sourceFile);
            BufferedImage compressedImage = compressImage(originalImage);
            
            // Save compressed image as JPEG
            ImageIO.write(compressedImage, "jpg", destinationPath.toFile());
            
            // Return relative path for database storage
            return Paths.get(IMAGES_FOLDER, newFileName).toString().replace("\\", "/");
            
        } catch (IOException e) {
            System.err.println("Error copying image file: " + e.getMessage());
            e.printStackTrace();
            showError("Error", "Failed to copy image file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Load image from data folder
     */
    public static Image loadImage(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return null;
        }

        try {
            Path fullPath = Paths.get(DATA_FOLDER, imagePath);
            if (Files.exists(fullPath)) {
                return new Image(fullPath.toUri().toString());
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Create ImageView with proper sizing
     */
    public static ImageView createImageView(String imagePath, double maxWidth, double maxHeight) {
        Image image = loadImage(imagePath);
        if (image == null) {
            return null;
        }

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        
        // Scale image to fit within max dimensions
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        double scaleX = maxWidth / imageWidth;
        double scaleY = maxHeight / imageHeight;
        double scale = Math.min(scaleX, scaleY);
        
        imageView.setFitWidth(imageWidth * scale);
        imageView.setFitHeight(imageHeight * scale);
        
        return imageView;
    }

    /**
     * Delete image file from data folder
     */
    public static boolean deleteImage(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return true;
        }

        try {
            Path fullPath = Paths.get(DATA_FOLDER, imagePath);
            if (Files.exists(fullPath)) {
                Files.delete(fullPath);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error deleting image: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Get file extension
     */
    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex);
        }
        return "";
    }

    /**
     * Show error alert
     */
    private static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Get the absolute path to the data folder
     */
    public static String getDataFolderPath() {
        return Paths.get(DATA_FOLDER).toAbsolutePath().toString();
    }
}
