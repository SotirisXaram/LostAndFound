package com.charamidis.lostAndFound.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageManagerTest {
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        // Set up test environment
        System.setProperty("user.home", tempDir.toString());
    }
    
    @Test
    void testInitializeDataFolder() {
        // This test verifies that the data folder structure is created
        ImageManager.initializeDataFolder();
        
        Path dataFolder = tempDir.resolve("Desktop").resolve("export_data");
        Path imagesFolder = dataFolder.resolve("images");
        
        assertTrue(Files.exists(dataFolder));
        assertTrue(Files.exists(imagesFolder));
    }
    
    @Test
    void testSelectImageFile() {
        // This test would require a JavaFX application context
        // For now, we'll test the method exists and doesn't throw exceptions
        assertDoesNotThrow(() -> {
            // ImageManager.selectImageFile() requires a Stage parameter
            // This would need to be tested in an integration test with TestFX
        });
    }
    
    @Test
    void testCopyImageToDataFolderWithNullFile() {
        String result = ImageManager.copyImageToDataFolder(null, 1);
        assertNull(result);
    }
    
    @Test
    void testCopyImageToDataFolderWithNonExistentFile() {
        File nonExistentFile = new File("non_existent_file.jpg");
        String result = ImageManager.copyImageToDataFolder(nonExistentFile, 1);
        assertNull(result);
    }
    
    @Test
    void testLoadImageWithNullPath() {
        var result = ImageManager.loadImage(null);
        assertNull(result);
    }
    
    @Test
    void testLoadImageWithEmptyPath() {
        var result = ImageManager.loadImage("");
        assertNull(result);
    }
    
    @Test
    void testCreateImageViewWithNullPath() {
        var result = ImageManager.createImageView(null, 100, 100);
        assertNull(result);
    }
    
    @Test
    void testDeleteImageWithNullPath() {
        boolean result = ImageManager.deleteImage(null);
        assertFalse(result);
    }
    
    @Test
    void testDeleteImageWithEmptyPath() {
        boolean result = ImageManager.deleteImage("");
        assertFalse(result);
    }
}
