package com.charamidis.lostAndFound.pages;

import com.charamidis.lostAndFound.exporters.BinaryExporter;
import com.charamidis.lostAndFound.exporters.CsvExporter;
import com.charamidis.lostAndFound.exporters.ExcelExporter;
import com.charamidis.lostAndFound.forms.*;
import com.charamidis.lostAndFound.models.User;
import com.charamidis.lostAndFound.pages.ContentsPage;
import com.charamidis.lostAndFound.pages.ShowUsers;
import com.charamidis.lostAndFound.utils.AppLogger;
// AutoBackupManager import removed - class doesn't exist
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
import javafx.scene.shape.Circle;
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
        MenuItem statisticsDashboard = new MenuItem("Dashboard Στατιστικών");

        statisticsDashboard.setOnAction(e->{
            new StatisticsDashboard(finalConn);
        });

        diagrams.getItems().addAll(statisticsDashboard);

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

        Menu adminMenu = new Menu("_Admin");
        MenuItem adminSettings = new MenuItem("_Settings");
        adminSettings.setOnAction(e->{
            AdminSettings.showSettings();
        });
        adminMenu.getItems().addAll(adminSettings);

        if (user.getRole().equals("admin")) {
            menuBar.getMenus().addAll(manage, usersMenu,diagrams,exportData,utils,adminMenu,help);
        } else {
            menuBar.getMenus().addAll(manage,diagrams,exportData,help);
        }


        // Create main container
        VBox mainContainer = new VBox();
        mainContainer.setSpacing(0);
        mainContainer.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Header section
        HBox header = new HBox();
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setBackground(new Background(new BackgroundFill(Color.rgb(52, 73, 94), CornerRadii.EMPTY, Insets.EMPTY)));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);

        // Welcome text
        VBox welcomeSection = new VBox();
        welcomeSection.setSpacing(5);
        
        Text welcomeText = new Text("Καλώς ήρθατε");
        welcomeText.setFill(Color.WHITE);
        welcomeText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Text userInfo = new Text(user.getLastName() + " " + user.getFirstName());
        userInfo.setFill(Color.rgb(189, 195, 199));
        userInfo.setStyle("-fx-font-size: 16px;");
        
        Text userAm = new Text("AM: " + user.getAm());
        userAm.setFill(Color.rgb(189, 195, 199));
        userAm.setStyle("-fx-font-size: 14px;");
        
        welcomeSection.getChildren().addAll(welcomeText, userInfo, userAm);

        // Quick stats section
        HBox statsSection = new HBox();
        statsSection.setSpacing(30);
        statsSection.setAlignment(Pos.CENTER_RIGHT);
        
        // Get quick stats from database
        try {
            Statement stmt = finalConn.createStatement();
            
            // Total records
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM records");
            int totalRecords = rs.next() ? rs.getInt("total") : 0;
            
            // Pending returns
            rs = stmt.executeQuery("SELECT COUNT(*) as pending FROM records r LEFT JOIN returns ret ON r.id = ret.id WHERE ret.id IS NULL");
            int pendingReturns = rs.next() ? rs.getInt("pending") : 0;
            
            // This month records
            rs = stmt.executeQuery("SELECT COUNT(*) as thisMonth FROM records WHERE strftime('%Y-%m', record_datetime) = strftime('%Y-%m', 'now')");
            int thisMonthRecords = rs.next() ? rs.getInt("thisMonth") : 0;
            
            VBox totalRecordsBox = createStatBox("Σύνολο Εγγραφών", String.valueOf(totalRecords), Color.rgb(46, 204, 113));
            VBox pendingBox = createStatBox("Εκκρεμείς", String.valueOf(pendingReturns), Color.rgb(241, 196, 15));
            VBox monthlyBox = createStatBox("Αυτό το Μήνα", String.valueOf(thisMonthRecords), Color.rgb(52, 152, 219));
            
            statsSection.getChildren().addAll(totalRecordsBox, pendingBox, monthlyBox);
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting statistics", e);
        }

        header.getChildren().addAll(welcomeSection, statsSection);
        HBox.setHgrow(welcomeSection, Priority.ALWAYS);

        // Main content area
        VBox contentArea = new VBox();
        contentArea.setPadding(new Insets(40, 30, 40, 30));
        contentArea.setSpacing(30);
        contentArea.setAlignment(Pos.CENTER);

        // Quick actions title
        Text quickActionsTitle = new Text("Γρήγορες Ενέργειες");
        quickActionsTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-fill: #2c3e50;");
        contentArea.getChildren().add(quickActionsTitle);

        // Action buttons grid
        GridPane actionGrid = new GridPane();
        actionGrid.setHgap(30);
        actionGrid.setVgap(30);
        actionGrid.setAlignment(Pos.CENTER);

        // Create action buttons - using same functionality as menubar
        Button recordsBtn = createActionButton("Εγγραφές", "Διαχείριση εγγραφών", "open-book.png", Color.rgb(52, 152, 219));
        recordsBtn.setOnAction(e -> new RecordsShow(finalConn)); // Same as menubar

        Button returnsBtn = createActionButton("Επιστροφές", "Διαχείριση επιστροφών", "return-box.png", Color.rgb(46, 204, 113));
        returnsBtn.setOnAction(e -> new ReturnsShow(finalConn)); // Same as menubar

        Button searchBtn = createActionButton("Αναζήτηση", "Αναζήτηση εγγραφών", "paper.png", Color.rgb(155, 89, 182));
        searchBtn.setOnAction(e -> new ModernSearchForm(finalConn));

        Button newRecordBtn = createActionButton("Νέα Εγγραφή", "Δημιουργία νέας εγγραφής", "open-book.png", Color.rgb(231, 76, 60));
        newRecordBtn.setOnAction(e -> new FormRecord(finalConn, user)); // Same as menubar

        Button statisticsBtn = createActionButton("Στατιστικά", "Προβολή στατιστικών", "paper.png", Color.rgb(241, 196, 15));
        statisticsBtn.setOnAction(e -> new StatisticsDashboard(finalConn)); // Same as menubar

        Button profileBtn = createActionButton("Προφίλ", "Διαχείριση προφίλ", "open-book.png", Color.rgb(52, 73, 94));
        profileBtn.setOnAction(e -> {
            ProfileForm profileForm = new ProfileForm(finalConn, user);
            Scene profileScene = profileForm.getScene();
            Stage profileStage = new Stage();
            profileStage.setScene(profileScene);
            profileStage.setTitle("Profile");
            profileStage.setWidth(450);
            profileStage.setMaximized(false);
            profileStage.setMaxWidth(800);
            profileStage.setMinWidth(450);
            profileStage.setMinHeight(550);
            profileStage.setMaxHeight(700);
            profileStage.setHeight(550);
            profileStage.initModality(Modality.APPLICATION_MODAL);
            profileStage.show();
        });

        // Add buttons to grid
        actionGrid.add(recordsBtn, 0, 0);
        actionGrid.add(returnsBtn, 1, 0);
        actionGrid.add(searchBtn, 2, 0);
        actionGrid.add(newRecordBtn, 0, 1);
        actionGrid.add(statisticsBtn, 1, 1);
        actionGrid.add(profileBtn, 2, 1);

        contentArea.getChildren().add(actionGrid);

        // Footer
        HBox footer = new HBox();
        footer.setPadding(new Insets(20, 30, 20, 30));
        footer.setBackground(new Background(new BackgroundFill(Color.rgb(236, 240, 241), CornerRadii.EMPTY, Insets.EMPTY)));
        footer.setAlignment(Pos.CENTER);
        
        Text footerText = new Text("Lost & Found Management System v2.0.1");
        footerText.setStyle("-fx-font-size: 12px; -fx-fill: #7f8c8d;");
        footer.getChildren().add(footerText);

        mainContainer.getChildren().addAll(menuBar, header, contentArea, footer);

        scene = new Scene(mainContainer);



    }

    public Scene getScene(){
        return this.scene;
    }

    private VBox createStatBox(String title, String value, Color color) {
        VBox statBox = new VBox();
        statBox.setAlignment(Pos.CENTER);
        statBox.setSpacing(5);
        statBox.setPadding(new Insets(15, 20, 15, 20));
        statBox.setBackground(new Background(new BackgroundFill(color, new CornerRadii(8), Insets.EMPTY)));
        statBox.setMinWidth(120);

        Text valueText = new Text(value);
        valueText.setFill(Color.WHITE);
        valueText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Text titleText = new Text(title);
        titleText.setFill(Color.WHITE);
        titleText.setStyle("-fx-font-size: 12px;");

        statBox.getChildren().addAll(valueText, titleText);
        return statBox;
    }

    private Button createActionButton(String title, String description, String iconName, Color color) {
        Button button = new Button();
        button.setPrefSize(200, 120);
        button.setStyle("-fx-background-color: " + toHexColor(color) + "; " +
                       "-fx-background-radius: 12; " +
                       "-fx-border-radius: 12; " +
                       "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: " + toHexColor(color.darker()) + "; " +
                           "-fx-background-radius: 12; " +
                           "-fx-border-radius: 12; " +
                           "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 15, 0, 0, 3);");
        });
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: " + toHexColor(color) + "; " +
                           "-fx-background-radius: 12; " +
                           "-fx-border-radius: 12; " +
                           "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        });

        VBox buttonContent = new VBox();
        buttonContent.setAlignment(Pos.CENTER);
        buttonContent.setSpacing(10);

        // Icon
        try {
            ImageView icon = new ImageView(new Image(iconName, 40, 40, true, true));
            icon.setPreserveRatio(true);
            buttonContent.getChildren().add(icon);
        } catch (Exception e) {
            // If icon not found, create a simple circle
            Circle iconCircle = new Circle(20, color.brighter());
            buttonContent.getChildren().add(iconCircle);
        }

        // Title
        Text titleText = new Text(title);
        titleText.setFill(Color.WHITE);
        titleText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        titleText.setTextAlignment(TextAlignment.CENTER);

        // Description
        Text descText = new Text(description);
        descText.setFill(Color.WHITE);
        descText.setStyle("-fx-font-size: 11px;");
        descText.setTextAlignment(TextAlignment.CENTER);
        descText.setWrappingWidth(180);

        buttonContent.getChildren().addAll(titleText, descText);
        button.setGraphic(buttonContent);

        return button;
    }

    private String toHexColor(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }


}
