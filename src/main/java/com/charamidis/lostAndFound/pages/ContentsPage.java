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
        Tab ContactTab = new Tab("Contact");

        homeTab.setClosable(false);
        statisticsTab.setClosable(false);
        recordsTab.setClosable(false);
        Disclaimer.setClosable(false);
        ContactTab.setClosable(false);

        //Home Tab

        Text homeText = new Text(
                "" +
                        "Welcome to Lost And Found\n" +
                        "\n" +
                        "Thank you for using the Lost And Found app! This app is designed to help you manage lost items at the lost and found office's efficiently. Here's what you can do with the app:\n" +
                        "\n" +
                        "-Manage Lost Items (Records): Easily report and track lost items. If you find a lost item, you can also report it to help reunite it with its owner.\n" +
                        "- Statistics: View detailed statistics about lost and found items to understand trends and improve the management process.\n" +
                        "- Backup Data: Ensure all your data is safely backed up and easily accessible when needed.\n" +
                        "- Print BARCODE for each record using unique code\n"+
                        "- Search for lost items based on date, time, id, description, worker etc..\n"+
                        "\n" +" Language Translation\n" +
                        "\n" +
                        "Our Lost And Found app can be translated into any language to suit your needs. If you require the app in a different language, please contact our support team.\n"+
                        "Navigate through the tabs to explore the app's features and manage lost items with ease."
        );
        homeText.setWrappingWidth(400);
        VBox homeContent = new VBox(homeText);
        homeContent.setAlignment(Pos.CENTER);


        //STATISTICS TAB

        Text statisticsText = new Text("" +
                "Statistics: \n" +
                "\n" +
                "Our Lost And Found app provides comprehensive statistics to help you manage lost items effectively:\n" +
                "\n" +
                "- Annual Statistics:  Access statistics for lost items and their returns over the past year. This includes data on the number of items lost each month.\n" +
                "  \n" +
                "- User Statistics:  View statistics related to user activity. Track the number of records each user has made over the last month.\n" +
                "\n" +
                "These statistics are designed to give you valuable insights and help improve the efficiency of lost item management .");
        VBox statisticsContent = new VBox(statisticsText);
        statisticsText.setWrappingWidth(400);
        statisticsContent.setAlignment(Pos.CENTER);

        Text recordsText = new Text("Managing Records\n" +
                "\n" +
                "As a registered user (added by an admin), you can manage records for lost items efficiently. Follow these steps:\n" +
                "\n" +
                "1. Creating a Record:\n" +
                "   - Go to the “MANAGE”-“ΔΙΑΧΕΙΡHΣΗ” tab. (or press Ctrl + Shift + E) \n" +
                "   - Click on “+”\n" +
                "   - Fill in the required details about the lost item (description, location, date, etc.).\n" +
                "   - The user's information is automatically captured.\n" +
                "   - Click \"Save” to create the record.\n" +
                "\n" +
                "2. Returning an Item:\n" +
                "   - Select the item from the records list.\n" +
                "   - Click \"Return Item.\" - “ΕΠΙΣΤΡΟΦΗ”\n" +
                "   - Enter the details of the person claiming the item.\n" +
                "   - Add any comments if necessary.\n" +
                "   - Click “SAVE” to complete the process.\n" +
                "   \n" +
                "\n" +
                "3. Printing the Barcode:\n" +
                "   - Select the item from the list.\n" +
                "   - Press `Ctrl + Shift + B` to print the barcode for the item.\n" +
                "\n" +
                "4. Printing the Receipt:\n" +
                "   - Select the record from the list.\n" +
                "   - Click \"Print.\"\n" +
                "   - The receipt verifies that the item was handed over to the office.\n" +
                "   - This receipt should be printed and signed by both the person who found the item and the worker handling the item.\n" +
                "\n" +
                "5.Save files\n" +
                "  -All files (barcodes, receipts etc..) are save in the Desktop in the folder export_data that the app is make .\n" +
                "\n" +
                "\n" +
                "\n" +
                "These steps help ensure that all lost items are properly recorded and managed, making it easier to return them to their rightful owners.\n");
        VBox recordsContent = new VBox(recordsText);
        recordsText.setWrappingWidth(400);
        recordsContent.setAlignment(Pos.CENTER);

        //Disclaimer Tab
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

        //Contact Tab

        Text contactText = new Text("Contact Us\n" +
                "\n" +
                "If you need assistance with the app, have any questions, or would like to request a translation, please don't hesitate to reach out to us. We're here to help!\n" +
                "\n" +
                "Email: sotirisxaram@icloud.com");
        contactText.setWrappingWidth(400);
        VBox contactContent = new VBox(contactText);
        contactContent.setAlignment(Pos.CENTER);

        homeTab.setContent(homeContent);
        statisticsTab.setContent(statisticsContent);
        recordsTab.setContent(recordsContent);
        ContactTab.setContent(contactContent);
        Disclaimer.setContent(disclaimer);

        homeTab.setTooltip(new Tooltip("Home page description"));
        statisticsTab.setTooltip(new Tooltip("Statistics page description"));
        recordsTab.setTooltip(new Tooltip("Records page description"));
        ContactTab.setTooltip(new Tooltip("Contact page"));
        Disclaimer.setTooltip(new Tooltip("Disclaimer description"));

        tabPane = new TabPane();
        tabPane.getTabs().addAll(homeTab, statisticsTab, recordsTab, Disclaimer,ContactTab);

        BorderPane layout = new BorderPane();
        layout.setCenter(tabPane);

        Scene scene = new Scene(layout, 400, 450);

        primaryStage.setTitle("Contents");
        primaryStage.setWidth(550);
        primaryStage.setHeight(700);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }
}
