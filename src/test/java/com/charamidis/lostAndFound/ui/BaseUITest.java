package com.charamidis.lostAndFound.ui;

import com.charamidis.lostAndFound.Main;
import com.charamidis.lostAndFound.utils.SqliteDatabaseInitializer;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.concurrent.TimeoutException;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 * Base class for UI tests providing common setup and utilities
 */
public abstract class BaseUITest {
    
    protected FxRobot robot;
    protected Stage stage;
    
    @Start
    void start(Stage stage) throws Exception {
        // Initialize database before starting the application
        SqliteDatabaseInitializer.initializeDatabase();
        
        // Start the JavaFX application
        new Main().start(stage);
        this.stage = stage;
    }
    
    @BeforeEach
    void setUp() {
        robot = new FxRobot();
    }
    
    @AfterEach
    void tearDown() throws TimeoutException {
        FxToolkit.cleanupStages();
    }
    
    /**
     * Helper method to wait for a node to be visible
     */
    protected void waitForNode(String query) {
        robot.waitFor(query, 5);
    }
    
    /**
     * Helper method to verify a node exists and is visible
     */
    protected void verifyNodeVisible(String query) {
        verifyThat(query, isVisible());
    }
    
    /**
     * Helper method to verify a node has specific text
     */
    protected void verifyNodeText(String query, String expectedText) {
        verifyThat(query, hasText(expectedText));
    }
    
    /**
     * Helper method to click on a node
     */
    protected void clickOn(String query) {
        robot.clickOn(query);
    }
    
    /**
     * Helper method to type text into a node
     */
    protected void typeText(String query, String text) {
        robot.clickOn(query);
        robot.write(text);
    }
    
    /**
     * Helper method to clear text from a node
     */
    protected void clearText(String query) {
        robot.clickOn(query);
        robot.push(javafx.scene.input.KeyCode.CONTROL, javafx.scene.input.KeyCode.A);
        robot.push(javafx.scene.input.KeyCode.DELETE);
    }
    
    /**
     * Helper method to verify a node is enabled
     */
    protected void verifyNodeEnabled(String query) {
        Node node = robot.lookup(query).query();
        assert node.isDisabled() == false : "Node should be enabled";
    }
    
    /**
     * Helper method to verify a node is disabled
     */
    protected void verifyNodeDisabled(String query) {
        Node node = robot.lookup(query).query();
        assert node.isDisabled() == true : "Node should be disabled";
    }
}
