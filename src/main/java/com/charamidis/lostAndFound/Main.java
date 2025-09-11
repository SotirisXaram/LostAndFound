package com.charamidis.lostAndFound;

import com.charamidis.lostAndFound.models.User;
import com.charamidis.lostAndFound.pages.MainScreen;
import com.charamidis.lostAndFound.utils.AppLogger;
import com.charamidis.lostAndFound.utils.ConnectionStatusIndicator;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import com.charamidis.lostAndFound.utils.Resources;
import com.charamidis.lostAndFound.utils.AutoBackupManager;
import com.charamidis.lostAndFound.utils.SqliteDatabaseInitializer;
import com.charamidis.lostAndFound.utils.ImageManager;
import com.charamidis.lostAndFound.web.WebServerManager;
import javafx.application.*;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import org.mindrot.jbcrypt.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application{
    //Declare the items in the screen
    Label lblUserName,lblPassword;
    TextField txtUsername;
    PasswordField txtPassword;
    Stage loginPage;
    Scene scene;
    VBox vbox ;
    Button btnEnter ;
    HBox hboxUsername,hboxPassword;
    Connection connection;
    StackPane stackPane;
    private static final Logger logger = AppLogger.getLogger();
    ConnectionStatusIndicator connectionStatusIndicator;

    @Override
    public void start(Stage mainPage) {
        
        // Initialize SQLite database
        SqliteDatabaseInitializer.initializeDatabase();
        // Initialize data folder structure
        ImageManager.initializeDataFolder();
        // Start automatic backup
        AutoBackupManager.startAutoBackup();
        // Initialize web server (optional)
        WebServerManager.initialize();

        
        //Username field
        lblUserName = new Label("Χρήστης:");
        lblUserName.getStyleClass().add("form-label");
        txtUsername = new TextField();
        txtUsername.setPromptText("Αριθμός μητρώου...");
        txtUsername.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        
        VBox usernameContainer = new VBox();
        usernameContainer.getChildren().addAll(lblUserName, txtUsername);
        usernameContainer.setSpacing(8);
        usernameContainer.setAlignment(Pos.CENTER_LEFT);

        //Password field
        lblPassword = new Label("Κωδικός:");
        lblPassword.getStyleClass().add("form-label");
        txtPassword = new PasswordField();
        txtPassword.setPromptText("Κωδικός...");
        txtPassword.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        
        VBox passwordContainer = new VBox();
        passwordContainer.getChildren().addAll(lblPassword, txtPassword);
        passwordContainer.setSpacing(8);
        passwordContainer.setAlignment(Pos.CENTER_LEFT);

        btnEnter = new Button("Είσοδος");
        btnEnter.setDefaultButton(true);
        btnEnter.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        btnEnter.setOnAction(e->{

            if(validateForm()){
                    try{
                        Connection conn = SqliteDatabaseInitializer.getConnection();
                        connection = conn;
                        PreparedStatement stm = conn.prepareStatement("SELECT * FROM users WHERE am=?");
                        stm.setInt(1, Integer.parseInt(txtUsername.getText().trim()));
                        ResultSet resultSet = stm.executeQuery();
                       if(resultSet.next()) {
                           String hashedPassword = resultSet.getString("password");
                           if (BCrypt.checkpw(txtPassword.getText().trim(), hashedPassword)) {
                               User user = new User(resultSet.getInt("am"), resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("date_of_birth"), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), resultSet.getString("role"));
                               MainScreen mainScreen = new MainScreen(connection, user, mainPage);
                               Scene mainScene = mainScreen.getScene();
                               loginPage.setTitle("Lost And Found");
                               loginPage.setScene(mainScene);
                               // Make main window responsive
                               Screen screen = Screen.getPrimary();
                               double screenWidth = screen.getVisualBounds().getWidth();
                               double screenHeight = screen.getVisualBounds().getHeight();
                               
                               double windowWidth = Math.min(1200, screenWidth * 0.9);
                               double windowHeight = Math.min(800, screenHeight * 0.9);
                               
                               loginPage.setWidth(windowWidth);
                               loginPage.setHeight(windowHeight);
                               loginPage.setMinWidth(1000);
                               loginPage.setMinHeight(700);
                               loginPage.setMaxWidth(screenWidth * 0.95);
                               loginPage.setMaxHeight(screenHeight * 0.95);
                               loginPage.setResizable(true);
                               loginPage.setX((screenWidth - windowWidth) / 2);
                               loginPage.setY((screenHeight - windowHeight) / 2);
                           } else {
                               new MessageBoxOk("Λάθος ΟΝΟΜΑ ή ΚΩΔΙΚΟΣ\n");

                           }


                           resultSet.close();
                       }else{
                           new MessageBoxOk("Λάθος ΟΝΟΜΑ ή ΚΩΔΙΚΟΣ\n");

                       }

                    }catch (SQLException exception){
                        new MessageBoxOk("Error connection to database");
                        logger.log(Level.SEVERE,"SQL Exception",exception);
                    }

            }
        });


        Label logoLabel = new Label("Lost & Found");
        logoLabel.setId("logo");

        vbox = new VBox();
        vbox.getChildren().addAll(logoLabel, usernameContainer, passwordContainer, btnEnter);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        vbox.getStyleClass().add("login-container");


        scene = new Scene(vbox);
        try{
            scene.getStylesheets().add(getClass().getClassLoader().getResource("styles/style.css").toExternalForm());
        }catch (NullPointerException e){
           logger.log(Level.FINE,"Couldn't load the css file in main",e);
        }

        loginPage = new Stage();

        loginPage.setScene(scene);


        // Make login window responsive
        Screen loginScreen = Screen.getPrimary();
        double loginScreenWidth = loginScreen.getVisualBounds().getWidth();
        double loginScreenHeight = loginScreen.getVisualBounds().getHeight();
        
        double loginWindowWidth = Math.min(600, loginScreenWidth * 0.4);
        double loginWindowHeight = Math.min(700, loginScreenHeight * 0.7);
        
        loginPage.setWidth(loginWindowWidth);
        loginPage.setHeight(loginWindowHeight);
        loginPage.setMinWidth(500);
        loginPage.setMinHeight(600);
        loginPage.setMaxWidth(loginScreenWidth * 0.5);
        loginPage.setMaxHeight(loginScreenHeight * 0.8);
        loginPage.setResizable(true);
        loginPage.setX((loginScreenWidth - loginWindowWidth) / 2);
        loginPage.setY((loginScreenHeight - loginWindowHeight) / 2);
        loginPage.setTitle("Lost & Found - Σύνδεση");
        loginPage.setMaximized(false);

        loginPage.show();


    }

    public Scene getScene(){
        return this.scene;
    }





    public boolean validateForm(){
        String errors = "";

        if(txtUsername.getText().trim().length()<1 || !txtUsername.getText().trim().matches("^[0-9]+$")){
            errors+="Ο Αριθμός Μητρώου πρέπει να συμπληρωθεί \n";
        }
        if(txtPassword.getText().trim().length()<1){
            errors+="Ο κωδικός πρέπει να συμπληρωθεί \n";
        }


        if(errors.equals("")){
            return true;
        }else{
            new MessageBoxOk(errors);
            return false;
        }
    }

    public void stop() {
        try {
            if ( connection!= null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE,"Error closing the connection:",exception);
        }
    }



    public static void main(String[] args){
        launch(args);
    }
}
