package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.utils.AutoBackupManager;
import com.charamidis.lostAndFound.utils.DatabaseLoader;
import com.charamidis.lostAndFound.utils.MessageBoxOk;
import com.charamidis.lostAndFound.web.WebServerManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminSettings {
    
    public static void showSettings() {
        Stage settingsStage = new Stage();
        settingsStage.setTitle("Admin Settings");
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.setResizable(false);
        
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        
        // Backup Settings Section
        Label backupTitle = new Label("Automatic Backup Settings");
        backupTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        Label backupIntervalLabel = new Label("Backup Interval (hours):");
        Spinner<Integer> backupIntervalSpinner = new Spinner<>(1, 24, AutoBackupManager.getBackupInterval());
        backupIntervalSpinner.setEditable(true);
        
        CheckBox enableAutoBackup = new CheckBox("Enable Automatic Backup");
        enableAutoBackup.setSelected(true);
        
        Button saveBackupSettings = new Button("Save Backup Settings");
        saveBackupSettings.setOnAction(e -> {
            int interval = backupIntervalSpinner.getValue();
            AutoBackupManager.setBackupInterval(interval);
            if (enableAutoBackup.isSelected()) {
                AutoBackupManager.startAutoBackup();
            } else {
                AutoBackupManager.stopAutoBackup();
            }
            new MessageBoxOk("Backup settings saved!");
        });
        
        // Database Management Section
        Label dbTitle = new Label("Database Management");
        dbTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        Button loadDatabaseBtn = new Button("Load Database File");
        loadDatabaseBtn.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white;");
        loadDatabaseBtn.setOnAction(e -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Load Database");
            confirmAlert.setHeaderText("Load Database File");
            confirmAlert.setContentText("This will replace the current database with the selected file.\nA backup will be created automatically.\n\nAre you sure you want to continue?");
            
            if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                DatabaseLoader.loadDatabase();
            }
        });
        
        Button manualBackupBtn = new Button("Create Manual Backup");
        manualBackupBtn.setStyle("-fx-background-color: #4ecdc4; -fx-text-fill: white;");
        manualBackupBtn.setOnAction(e -> {
            com.charamidis.lostAndFound.utils.Database.backupDatabase("lostandfound");
        });
        
        // Layout
        GridPane backupGrid = new GridPane();
        backupGrid.setHgap(10);
        backupGrid.setVgap(10);
        backupGrid.add(backupIntervalLabel, 0, 0);
        backupGrid.add(backupIntervalSpinner, 1, 0);
        backupGrid.add(enableAutoBackup, 0, 1, 2, 1);
        backupGrid.add(saveBackupSettings, 0, 2, 2, 1);
        
        VBox dbLayout = new VBox(10);
        dbLayout.getChildren().addAll(loadDatabaseBtn, manualBackupBtn);
        
        // Web Dashboard Section
        Label webTitle = new Label("Web Admin Dashboard");
        webTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        CheckBox enableWebDashboard = new CheckBox("Enable Web Dashboard");
        enableWebDashboard.setSelected(WebServerManager.isEnabled());
        
        Label portLabel = new Label("Port:");
        Spinner<Integer> portSpinner = new Spinner<>(1000, 65535, WebServerManager.getPort());
        portSpinner.setEditable(true);
        
        Label passwordLabel = new Label("Admin Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setText(WebServerManager.getAdminPassword());
        
        Button saveWebSettings = new Button("Save Web Settings");
        saveWebSettings.setOnAction(e -> {
            int port = portSpinner.getValue();
            String password = passwordField.getText();
            
            if (password.isEmpty()) {
                new MessageBoxOk("Password cannot be empty!");
                return;
            }
            
            WebServerManager.setPort(port);
            WebServerManager.setAdminPassword(password);
            
            if (enableWebDashboard.isSelected()) {
                WebServerManager.enableWebServer();
                new MessageBoxOk("Web dashboard enabled!\n\nAccess at: http://localhost:" + port + "/admin\nPassword: " + password);
            } else {
                WebServerManager.disableWebServer();
                new MessageBoxOk("Web dashboard disabled!");
            }
        });
        
        Button openDashboardBtn = new Button("Open Dashboard");
        openDashboardBtn.setStyle("-fx-background-color: #667eea; -fx-text-fill: white;");
        openDashboardBtn.setOnAction(e -> {
            if (WebServerManager.isEnabled()) {
                try {
                    java.awt.Desktop.getDesktop().browse(
                        java.net.URI.create("http://localhost:" + WebServerManager.getPort() + "/admin")
                    );
                } catch (Exception ex) {
                    new MessageBoxOk("Could not open browser. Please navigate to:\nhttp://localhost:" + WebServerManager.getPort() + "/admin");
                }
            } else {
                new MessageBoxOk("Web dashboard is not enabled!");
            }
        });
        
        Button showNetworkInfoBtn = new Button("Show Network Info");
        showNetworkInfoBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        showNetworkInfoBtn.setOnAction(e -> {
            if (WebServerManager.isEnabled()) {
                showNetworkInfoDialog();
            } else {
                new MessageBoxOk("Web dashboard is not enabled!");
            }
        });
        
        GridPane webGrid = new GridPane();
        webGrid.setHgap(10);
        webGrid.setVgap(10);
        webGrid.add(portLabel, 0, 0);
        webGrid.add(portSpinner, 1, 0);
        webGrid.add(passwordLabel, 0, 1);
        webGrid.add(passwordField, 1, 1);
        webGrid.add(enableWebDashboard, 0, 2, 2, 1);
        webGrid.add(saveWebSettings, 0, 3, 2, 1);
        
        VBox webLayout = new VBox(10);
        webLayout.getChildren().addAll(webGrid, openDashboardBtn, showNetworkInfoBtn);
        
        HBox buttonLayout = new HBox(10);
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> settingsStage.close());
        buttonLayout.getChildren().add(closeBtn);
        buttonLayout.setAlignment(Pos.CENTER_RIGHT);
        
        mainLayout.getChildren().addAll(
            backupTitle,
            backupGrid,
            new Separator(),
            dbTitle,
            dbLayout,
            new Separator(),
            webTitle,
            webLayout,
            new Separator(),
            buttonLayout
        );
        
        Scene scene = new Scene(mainLayout, 600, 600);
        settingsStage.setScene(scene);
        settingsStage.show();
    }
    
    private static void showNetworkInfoDialog() {
        Stage networkStage = new Stage();
        networkStage.setTitle("Network Access Information");
        networkStage.initModality(Modality.APPLICATION_MODAL);
        networkStage.setResizable(true);
        
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        
        Label titleLabel = new Label("🌍 Network Access Information");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        TextArea networkInfo = new TextArea();
        networkInfo.setEditable(false);
        networkInfo.setPrefRowCount(15);
        networkInfo.setPrefColumnCount(60);
        networkInfo.setWrapText(true);
        
        // Get network information
        StringBuilder info = new StringBuilder();
        info.append("🌐 WEB DASHBOARD ACCESS INFORMATION\n");
        info.append("═══════════════════════════════════════\n\n");
        
        try {
            // Get local IP addresses
            java.net.NetworkInterface.getNetworkInterfaces().asIterator().forEachRemaining(networkInterface -> {
                try {
                    if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                        networkInterface.getInetAddresses().asIterator().forEachRemaining(address -> {
                            if (!address.isLoopbackAddress() && address.getHostAddress().contains(".")) {
                                info.append("📱 Local Access: http://").append(address.getHostAddress())
                                    .append(":").append(WebServerManager.getPort()).append("/admin\n");
                            }
                        });
                    }
                } catch (Exception e) {
                    // Skip this interface
                }
            });
            
            // Try to get public IP
            info.append("\n🌐 Public IP: ").append(getPublicIP()).append("\n");
            info.append("🌍 Remote Access: http://").append(getPublicIP())
                .append(":").append(WebServerManager.getPort()).append("/admin\n");
            info.append("   (Requires port forwarding on your router)\n");
            
        } catch (Exception e) {
            info.append("⚠️  Could not detect network interfaces\n");
        }
        
        info.append("\n📋 REMOTE ACCESS SETUP INSTRUCTIONS:\n");
        info.append("1. Find your router's admin panel (usually 192.168.1.1 or 192.168.0.1)\n");
        info.append("2. Login with admin credentials (check router sticker)\n");
        info.append("3. Find 'Port Forwarding' or 'Virtual Server' section\n");
        info.append("4. Add rule: External Port ").append(WebServerManager.getPort())
            .append(" → Internal Port ").append(WebServerManager.getPort()).append("\n");
        info.append("5. Use the 'Remote Access' URL above from anywhere\n");
        info.append("6. Login with: admin / ").append(WebServerManager.getAdminPassword()).append("\n\n");
        info.append("🔒 SECURITY NOTES:\n");
        info.append("• Change the default password for better security\n");
        info.append("• Only enable when you need remote access\n");
        info.append("• Monitor access logs regularly\n");
        info.append("• Consider using VPN for additional security\n");
        
        networkInfo.setText(info.toString());
        
        Button copyBtn = new Button("Copy to Clipboard");
        copyBtn.setOnAction(e -> {
            javafx.scene.input.Clipboard.getSystemClipboard().setContent(
                new javafx.scene.input.ClipboardContent() {{
                    putString(networkInfo.getText());
                }}
            );
            new MessageBoxOk("Network information copied to clipboard!");
        });
        
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> networkStage.close());
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(copyBtn, closeBtn);
        buttonBox.setAlignment(Pos.CENTER);
        
        mainLayout.getChildren().addAll(titleLabel, networkInfo, buttonBox);
        
        Scene scene = new Scene(mainLayout, 700, 500);
        networkStage.setScene(scene);
        networkStage.show();
    }
    
    private static String getPublicIP() {
        try {
            String[] services = {
                "http://checkip.amazonaws.com",
                "http://icanhazip.com",
                "http://ifconfig.me/ip"
            };
            
            for (String service : services) {
                try {
                    java.net.URL url = new java.net.URL(service);
                    java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestMethod("GET");
                    
                    java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(connection.getInputStream())
                    );
                    String ip = reader.readLine().trim();
                    reader.close();
                    
                    if (ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                        return ip;
                    }
                } catch (Exception e) {
                    // Try next service
                }
            }
        } catch (Exception e) {
            // Fallback
        }
        return "Unknown";
    }
}
