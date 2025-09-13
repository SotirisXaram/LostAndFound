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
                        "Lost & Found Management System v2.0\n" +
                        "\n" +
                        "Welcome to the comprehensive Lost & Found Management System. This professional application streamlines the management of lost and found items with advanced features and modern technology.\n" +
                        "\n" +
                        "Key Features:\n" +
                        "• Complete Record Management: Create, edit, and track lost items with detailed information\n" +
                        "• Digital Asset Management: Attach and manage photos of lost items\n" +
                        "• Advanced Search & Filtering: Find records quickly using multiple criteria\n" +
                        "• Return Processing: Streamlined item return workflow with validation\n" +
                        "• Barcode & QR Code Generation: Unique identification for each item\n" +
                        "• Real-time Statistics: Live dashboard with comprehensive analytics\n" +
                        "• Web Dashboard: Access system remotely via web interface\n" +
                        "• Data Export: Multiple export formats (Excel, CSV, Binary)\n" +
                        "• Automated Backup: Secure data protection and recovery\n" +
                        "• User Management: Role-based access control\n" +
                        "\n" +
                        "System Requirements:\n" +
                        "• Java 11 or higher\n" +
                        "• SQLite database\n" +
                        "• Network access for web features\n" +
                        "\n" +
                        "Navigate through the tabs to explore all features and maximize your efficiency."
        );
        homeText.setWrappingWidth(400);
        VBox homeContent = new VBox(homeText);
        homeContent.setAlignment(Pos.CENTER);


        //STATISTICS TAB

        Text statisticsText = new Text("" +
                "Analytics & Reporting\n" +
                "\n" +
                "The system provides comprehensive analytics and real-time reporting capabilities:\n" +
                "\n" +
                "Dashboard Metrics:\n" +
                "• Total Records: Complete count of all lost items in the system\n" +
                "• Return Statistics: Track successful item returns and return rates\n" +
                "• Daily Activity: Monitor real-time daily operations and trends\n" +
                "• Monthly Analytics: Analyze patterns and seasonal variations\n" +
                "• Status Tracking: Monitor item status distribution (stored, returned, disposed)\n" +
                "\n" +
                "Real-time Updates:\n" +
                "• Live Statistics: All metrics update automatically without system restart\n" +
                "• Instant Notifications: Immediate updates when records are created or modified\n" +
                "• Performance Monitoring: Track system efficiency and user activity\n" +
                "\n" +
                "Export Capabilities:\n" +
                "• Excel Reports: Detailed spreadsheets for analysis\n" +
                "• CSV Data: Raw data export for external analysis\n" +
                "• Binary Backup: Complete system backup for data protection\n" +
                "\n" +
                "These insights enable data-driven decision making and operational optimization.");
        VBox statisticsContent = new VBox(statisticsText);
        statisticsText.setWrappingWidth(400);
        statisticsContent.setAlignment(Pos.CENTER);

        Text recordsText = new Text("Record Management System\n" +
                "\n" +
                "Comprehensive workflow for managing lost and found items with advanced features:\n" +
                "\n" +
                "Creating Records:\n" +
                "• Access via MANAGE menu or Ctrl+Shift+E shortcut\n" +
                "• Click 'New Record' to start the creation process\n" +
                "• Complete all required fields with validation\n" +
                "• Attach photos for visual identification\n" +
                "• Auto-population of officer information\n" +
                "• Real-time form validation (date/time formats)\n" +
                "\n" +
                "Return Processing:\n" +
                "• Select record and click 'Return Item'\n" +
                "• Complete claimant information with validation\n" +
                "• Date format validation (DD/MM/YYYY)\n" +
                "• Automatic status update to 'returned'\n" +
                "• Prevention of duplicate returns\n" +
                "• Complete audit trail maintenance\n" +
                "\n" +
                "Identification & Tracking:\n" +
                "• Unique barcode generation for each record\n" +
                "• QR code creation for public access\n" +
                "• Mobile-accessible QR codes\n" +
                "• Barcode-based search functionality\n" +
                "• UUID-based unique identification\n" +
                "\n" +
                "Search & Filtering:\n" +
                "• Advanced search across all fields\n" +
                "• Real-time filtering capabilities\n" +
                "• Multiple search criteria support\n" +
                "• Barcode/QR code search\n" +
                "• Date range filtering\n" +
                "\n" +
                "Data Management:\n" +
                "• Multiple export formats (Excel, CSV, Binary)\n" +
                "• Automated backup system\n" +
                "• Data integrity validation\n" +
                "• Cross-platform compatibility\n" +
                "• Secure data storage\n" +
                "\n" +
                "Web Integration:\n" +
                "• Remote access via web dashboard\n" +
                "• Mobile-friendly interface\n" +
                "• Public record viewing via QR codes\n" +
                "• Real-time data synchronization\n" +
                "• Network accessibility\n" +
                "\n" +
                "User Management:\n" +
                "• Role-based access control\n" +
                "• Admin user management\n" +
                "• Activity logging and tracking\n" +
                "• Secure authentication\n" +
                "• Permission management\n" +
                "\n" +
                "System Features:\n" +
                "• Real-time statistics updates\n" +
                "• Form validation and error handling\n" +
                "• Image management and compression\n" +
                "• Keyboard shortcuts for efficiency\n" +
                "• Professional UI/UX design\n" +
                "\n" +
                "All files are automatically saved to Desktop/export_data folder for easy access and organization.");
        VBox recordsContent = new VBox(recordsText);
        recordsText.setWrappingWidth(400);
        recordsContent.setAlignment(Pos.CENTER);

        //Disclaimer Tab
        Text text = new Text("Terms of Use & Disclaimer\n" +
                "\n" +
                "Software License Agreement:\n" +
                "This Lost & Found Management System is proprietary software provided under license. The software is designed for professional use in lost and found operations.\n" +
                "\n" +
                "Limitation of Liability:\n" +
                "The software is provided \"as is\" without warranties of any kind. While we strive for accuracy and reliability, we cannot guarantee error-free operation. Users assume all risks associated with software use.\n" +
                "\n" +
                "Data Security:\n" +
                "Users are responsible for maintaining appropriate data security measures, including regular backups and access controls. The software includes built-in security features, but users must implement proper operational procedures.\n" +
                "\n" +
                "System Requirements:\n" +
                "• Java 11 or higher\n" +
                "• SQLite database compatibility\n" +
                "• Network access for web features\n" +
                "• Adequate system resources for optimal performance\n" +
                "\n" +
                "Support & Updates:\n" +
                "Technical support is available for licensed users. Software updates may be provided to improve functionality and security. Users are encouraged to maintain current versions.\n" +
                "\n" +
                "Compliance:\n" +
                "Users must comply with applicable data protection regulations and organizational policies when using this system. Proper data handling procedures must be maintained.\n" +
                "\n" +
                "Copyright:\n" +
                "© 2024 Charamidis Sotirios. All rights reserved. This software is intended for authorized use in lost and found operations.\n" +
                "\n" +
                "By using this application, you acknowledge acceptance of these terms and conditions.");
        text.setWrappingWidth(400);
        VBox disclaimer = new VBox(text);
        disclaimer.setAlignment(Pos.CENTER);

        //Contact Tab

        Text contactText = new Text("Technical Support & Contact\n" +
                "\n" +
                "Professional Support Services:\n" +
                "Our technical support team is available to assist with system implementation, troubleshooting, and optimization.\n" +
                "\n" +
                "Support Services:\n" +
                "• System installation and configuration\n" +
                "• User training and documentation\n" +
                "• Technical troubleshooting\n" +
                "• Feature customization requests\n" +
                "• Performance optimization\n" +
                "• Security consultation\n" +
                "\n" +
                "Contact Information:\n" +
                "Primary Support: sotirisxaram@icloud.com\n" +
                "\n" +
                "Response Time:\n" +
                "• Critical issues: 24 hours\n" +
                "• General inquiries: 48-72 hours\n" +
                "• Feature requests: 1-2 weeks\n" +
                "\n" +
                "Documentation:\n" +
                "Comprehensive user guides and technical documentation are available through the help system.\n" +
                "\n" +
                "Training:\n" +
                "Custom training sessions can be arranged for organizations implementing the system.\n" +
                "\n" +
                "We are committed to providing professional support to ensure optimal system performance and user satisfaction.");
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
