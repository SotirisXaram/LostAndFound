package com.charamidis.lostAndFound;

import com.charamidis.lostAndFound.models.User;
import com.charamidis.lostAndFound.pages.MainScreen;
import com.charamidis.lostAndFound.utils.AppLogger;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import com.charamidis.lostAndFound.utils.Resources;
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

import java.io.File;
import java.net.URL;
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
    private static final Logger logger = AppLogger.getLogger();

    @Override
    public void start(Stage mainPage) {




        //Username field
        lblUserName = new Label("Χρήστης:");
        txtUsername = new TextField();
        txtUsername.setPromptText("Αριθμός μητρώου...");
        txtUsername.setFont(Font.font("Arial", FontWeight.BLACK,12));
        hboxUsername = new HBox();
        hboxUsername.getChildren().addAll(lblUserName,txtUsername);
        hboxUsername.setAlignment(Pos.CENTER);
        hboxUsername.setPadding(new Insets(10,0,10,0));


        //Password field
        lblPassword = new Label("Κωδικός:");
        txtPassword = new PasswordField();
        txtPassword.setPromptText("Κωδικός...");
        txtPassword.setFont(Font.font("Arial", FontWeight.LIGHT,12));
        hboxPassword = new HBox();
        hboxPassword.getChildren().addAll(lblPassword,txtPassword);
        hboxPassword.setAlignment(Pos.CENTER);

        btnEnter = new Button("Είσοδος");
        btnEnter.setDefaultButton(true);
        btnEnter.setOnAction(e->{

            if(validateForm()){
                    try{
                        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lostandfound", "sotirisxaram", "1234" );
                        connection = conn;
                        PreparedStatement stm = conn.prepareStatement("SELECT * FROM users WHERE am=?");
                        stm.setInt(1, Integer.parseInt(txtUsername.getText().trim()));
                        ResultSet resultSet = stm.executeQuery();
//BCrypt.checkpw(txtPassword.getText().trim(), hashedPassword)
                       if(resultSet.next()) {
                           String hashedPassword = resultSet.getString("password");
                           if (true) {
                               User user = new User(resultSet.getInt("am"), resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("date_of_birth"), LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), resultSet.getString("role"));
                               MainScreen mainScreen = new MainScreen(connection, user, mainPage);
                               Scene mainScene = mainScreen.getScene();
                               loginPage.setTitle("Lost And Found");
                               loginPage.setScene(mainScene);
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
        vbox.getChildren().addAll(logoLabel,hboxUsername,hboxPassword,btnEnter);
        vbox.setAlignment(Pos.CENTER);



        scene = new Scene(vbox);
        //scene.getStylesheets().add(getClass().getResource("main/java/com/charamidis/style.css").toExternalForm());
        try{
            scene.getStylesheets().add(getClass().getClassLoader().getResource("styles/style.css").toExternalForm());
        }catch (NullPointerException e){
            System.out.println("im here");
        }

        loginPage = new Stage();

        loginPage.setScene(scene);


        loginPage.setWidth(850);
        loginPage.setHeight(600);
        loginPage.setMinWidth(720);
        loginPage.setMinHeight(600);
        loginPage.setX((Screen.getPrimary().getVisualBounds().getWidth()-750)/2);
        loginPage.setY((Screen.getPrimary().getVisualBounds().getHeight()-600)/2);
        loginPage.setTitle("ΣΥΝΔΕΣΗ");
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