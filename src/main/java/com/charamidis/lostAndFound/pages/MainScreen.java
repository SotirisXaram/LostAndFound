package com.charamidis.lostAndFound.pages;

import com.charamidis.lostAndFound.exporters.BinaryExporter;
import com.charamidis.lostAndFound.exporters.CsvExporter;
import com.charamidis.lostAndFound.exporters.ExcelExporter;
import com.charamidis.lostAndFound.forms.*;
import com.charamidis.lostAndFound.models.User;
import com.charamidis.lostAndFound.utils.AppLogger;
import com.charamidis.lostAndFound.utils.Database;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import com.charamidis.lostAndFound.utils.Resources;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.layout.StackPane;


public class MainScreen {
    //MENU BAR
    Scene scene;
    MenuBar menuBar ;
    Stage stage ;
    Connection finalConn ;
    private static final Logger logger = AppLogger.getLogger();

    public MainScreen(Connection conn, User user, Stage mainStage){



        Resources.initData();
        menuBar = new MenuBar();
        finalConn = conn;


        Menu manage = new Menu("_Διαχείρηση");
        manage.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));

        MenuItem records = new MenuItem("_Eγγραφές");
        records.setAccelerator(new KeyCodeCombination(KeyCode.E,KeyCombination.CONTROL_DOWN,KeyCombination.SHIFT_DOWN));


        records.setOnAction(e->{
            new FormRecord(finalConn,user);
        });

        MenuItem returns = new MenuItem("Επιστ_ροφές");
        returns.setAccelerator(new KeyCodeCombination(KeyCode.R,KeyCombination.CONTROL_DOWN));
        returns.setOnAction(e->{
            new ReturnsShow(finalConn);
        });

        MenuItem profile = new MenuItem("_Προφιλ Χρήστη");
        profile.setOnAction(e->{

            ProfileForm profileForm = new ProfileForm(finalConn,user);
            Scene profileScene = profileForm.getScene();
            stage = new Stage();
            stage.setScene(profileScene);
            stage.setTitle("Profile");
            stage.setWidth(450);
            stage.setMaximized(false);
            stage.setMaxWidth(800);
            stage.setMinWidth(450);
            stage.setMinHeight(550);
            stage.setMaxHeight(700);
            stage.setHeight(550);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        });
        profile.setAccelerator(new KeyCodeCombination(KeyCode.I,KeyCombination.CONTROL_DOWN,KeyCombination.SHIFT_DOWN));
        MenuItem logOut = new MenuItem("_Κλείσιμο");
        logOut.setAccelerator(new KeyCodeCombination(KeyCode.L,KeyCombination.CONTROL_DOWN,KeyCombination.SHIFT_DOWN));
        logOut.setOnAction(e->{
            try {
                if (finalConn != null) {
                    finalConn.close();
                }
                user.clear();
                System.exit(1);

            } catch (SQLException ex) {
                logger.log(Level.SEVERE,"SQLException",ex);
            }

        });

        manage.getItems().addAll(records,returns,profile,logOut);



        Menu diagrams = new Menu("_Στατιστικά");
        MenuItem itemsLastYear = new MenuItem("Εγγραφές Τελευταίου Χρόνου");
        MenuItem returnsLastYear = new MenuItem("Επιστροφές Τελευταίου Χρόνου");
//        MenuItem itemsOfMonth = new MenuItem("Μηνιαία Στατιστικά");
        MenuItem itemsPerOfficers = new MenuItem("Στατιστικά χρηστών");

        itemsLastYear.setOnAction(e->{
            new ItemsLastYear(finalConn);
        });

        returnsLastYear.setOnAction(e->{
            new ReturnsLastYear(finalConn);
        });

//        itemsOfMonth.setOnAction(e->{
//            new ItemsOfMonth(finalConn);
//        });

        itemsPerOfficers.setOnAction(e->{
            new ItemsPerOfficers(finalConn);
        });


        diagrams.getItems().addAll(itemsLastYear,returnsLastYear ,new SeparatorMenuItem(),itemsPerOfficers);

        Menu exportData = new Menu("Εξαγωγή");
        MenuItem exportExcel = new MenuItem("Excel");
        MenuItem exportBinary = new MenuItem("Binary");
        MenuItem exportCsv = new MenuItem("Csv");
        MenuItem backup = new MenuItem("Αντίγραφο Ασφαλείας");
        backup.setOnAction(e -> {
            try {
                Database.backupDatabase("lostandfound");
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Backup Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while performing the backup. Please try again.");
                alert.showAndWait();
                logger.log(Level.SEVERE,"SQL BACKUP DB ERROR",ex);
            }
        });

        exportExcel.setOnAction(e->{
         new ExcelExporter(finalConn);
        });

        exportBinary.setOnAction(e->{
            new BinaryExporter(finalConn);
        });
        exportCsv.setOnAction(e->{
            new CsvExporter(finalConn);
        });



        exportData.getItems().addAll(exportExcel,exportBinary,exportCsv,backup);

        Menu utils = new Menu("Εργαλεία");
        MenuItem resetId = new MenuItem("Reset ID");
        MenuItem deleteReturn = new MenuItem("Διαγραφή Επιστροφής");

        utils.getItems().addAll(resetId,deleteReturn);
        resetId.setOnAction(e->{
           new ResetId(finalConn);
        });
        deleteReturn.setOnAction(e->{
            new DeleteReturn(finalConn);
        });


        Menu help = new Menu("Βοήθεια");
        MenuItem contents = new MenuItem("Περιεχόμενα");
        contents.setOnAction(e->{
            new ContentsPage(mainStage);
        });
        MenuItem about = new MenuItem("Σχετικά");
        about.setOnAction(e->{
            new AboutForm();
        });
        help.getItems().addAll(contents,new SeparatorMenuItem(),about);

        Menu usersMenu = new Menu("_Χρήστες");
        MenuItem showUsers = new MenuItem("Προβολή");
        showUsers.setOnAction(e->{
            new ShowUsers(finalConn);
        });
        MenuItem editUsers = new MenuItem("Διαχείριση");
        editUsers.setOnAction(e->{
            new EditUsers(finalConn);
        });
        MenuItem addUser = new MenuItem("Προσθήκη");
        addUser.setOnAction(e->{
            new AddUser(finalConn);
        });
        MenuItem deleteUser = new MenuItem("Διαγραφή");
        deleteUser.setOnAction(e->{
            new DeleteUser(finalConn);
        });

        usersMenu.getItems().addAll(showUsers,new SeparatorMenuItem(),editUsers,addUser,deleteUser);



        if (user.getRole().equals("admin")) {
            menuBar.getMenus().addAll(manage, usersMenu,diagrams,exportData,utils,help);
        } else {
            menuBar.getMenus().addAll(manage,diagrams,exportData,help);
        }


        StackPane stackPane = new StackPane();

        Image imgBackground = new Image("background.png");

        ImageView view = new ImageView(imgBackground);
        view.setFitWidth(750);
        view.setFitHeight(600);
        view.setPreserveRatio(true);

        HBox btnMain = new HBox();
        btnMain.setAlignment(Pos.BOTTOM_CENTER);
        btnMain.setSpacing(20);

        Button openBook = new Button();
        openBook.setBackground(Background.EMPTY);
        openBook.setGraphic(new ImageView(new Image("open-book.png",120,120,true,true)));
        openBook.setOnAction(e->{
            new RecordsShow(finalConn);
        });

        Button returnBox = new Button();
        returnBox.setBackground(Background.EMPTY);
        returnBox.setGraphic(new ImageView(new Image("return-box.png",120,120,true,true)));
        returnBox.setOnAction(e->{
            new ReturnsShow(finalConn);
        });

        Button paper = new Button();
        paper.setBackground(Background.EMPTY);
        paper.setGraphic(new ImageView(new Image("paper.png",120,120,true,true)));
        paper.setOnAction(e->{
            new SearchRecordsShow(finalConn);
        });

        btnMain.getChildren().addAll(openBook,returnBox,paper);


        Text txtWelcome = new Text(user.getLastName()+" "+user.getFirstName()+"\n"+String.valueOf(user.getAm()));



        StackPane.setAlignment(txtWelcome, Pos.BOTTOM_RIGHT);
        txtWelcome.setY(510);
        txtWelcome.setTextAlignment(TextAlignment.RIGHT);

        stackPane.getChildren().add(view);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(txtWelcome);
        stackPane.setPrefHeight(1080);
        stackPane.getChildren().add(btnMain);

        VBox vbox = new VBox();
        vbox.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE,CornerRadii.EMPTY,Insets.EMPTY)));

        vbox.getChildren().addAll(menuBar,stackPane);


        scene = new Scene(vbox);



    }

    public Scene getScene(){
        return this.scene;
    }


}
