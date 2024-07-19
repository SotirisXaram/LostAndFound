package com.charamidis.lostAndFound.pages;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ContentsPage {

    private Stage primaryStage;
    private TabPane tabPane;

    public ContentsPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createContents();
    }

    private void createContents() {
        Tab homeTab = new Tab("Home");
        Tab statisticsTab = new Tab("Statistics");
        Tab recordsTab = new Tab("Records");
        Tab Disclaimer = new Tab("Disclaimer");

        homeTab.setClosable(false);
        statisticsTab.setClosable(false);
        recordsTab.setClosable(false);
        Disclaimer.setClosable(false);

        VBox homeContent = new VBox(new Label("Home page content goes here."));
        VBox statisticsContent = new VBox(new Label("Statistics page content goes here."));
        VBox recordsContent = new VBox(new Label("Records page content goes here."));
        Text text = new Text("Disclaimer:\n" +
                "This application is provided \"as is\" without any representations or warranties, express or implied. The creators of this application make no representations or warranties regarding this application or the information and materials provided within it.\n" +
                "While we strive to ensure the accuracy, reliability, and completeness of the information and materials provided by this application, we cannot guarantee that they will always be up-to-date, accurate, or error-free. The use of this application and the information contained within it is at your own risk.\n" +
                "We disclaim all liability for any loss or damage arising from the use of, or reliance on, this application or any information or materials provided within it. This includes, but is not limited to, direct, indirect, consequential, or incidental loss or damage, whether arising from negligence, breach of contract, or otherwise, even if foreseeable.\n" +
                "We also reserve the right to modify, suspend, or discontinue any aspect of this application at any time, including its functionality, features, or availability, without notice or liability.\n" +
                "By using this application, you acknowledge and agree to the terms of this disclaimer. If you do not agree with these terms, please refrain from using this application.\n" +
                "\n" +
                "This application, created by Charamidis Sotirios  year: 2024, is intended for the purpose of aiding the lost and found office at the airport.\n");
        text.setWrappingWidth(400);
        VBox disclaimer = new VBox(text);
        disclaimer.setAlignment(Pos.CENTER);

        homeTab.setContent(homeContent);
        statisticsTab.setContent(statisticsContent);
        recordsTab.setContent(recordsContent);
        Disclaimer.setContent(disclaimer);

        homeTab.setTooltip(new Tooltip("Home page description"));
        statisticsTab.setTooltip(new Tooltip("Statistics page description"));
        recordsTab.setTooltip(new Tooltip("Records page description"));
        Disclaimer.setTooltip(new Tooltip("Disclaimer description"));

        tabPane = new TabPane();
        tabPane.getTabs().addAll(homeTab, statisticsTab, recordsTab, Disclaimer);

        BorderPane layout = new BorderPane();
        layout.setCenter(tabPane);

        Scene scene = new Scene(layout, 400, 450);

        primaryStage.setTitle("Contents Page");
        primaryStage.setWidth(450);
        primaryStage.setHeight(550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
