package com.charamidis.lostAndFound.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
// WebSocket configuration will be simplified

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebServerManager {
    private static Server server;
    private static boolean isEnabled = false;
    private static int port = 8080;
    private static String adminPassword = "admin123"; // Default password
    private static ScheduledExecutorService scheduler;
    
    private static final String CONFIG_FILE = "web_config.properties";

    public static void initialize() {
        loadConfiguration();
        System.out.println("🌐 Web Server Configuration: enabled=" + isEnabled + ", port=" + port);
        if (isEnabled) {
            startWebServer();
        } else {
            System.out.println("🌐 Web Server is disabled in configuration");
        }
    }

    public static void loadConfiguration() {
        Properties props = new Properties();
        try {
            if (Files.exists(Paths.get(CONFIG_FILE))) {
                props.load(Files.newInputStream(Paths.get(CONFIG_FILE)));
            }
        } catch (IOException e) {
            System.out.println("Could not load web config, using defaults");
        }
        
        isEnabled = Boolean.parseBoolean(props.getProperty("web.enabled", "false"));
        port = Integer.parseInt(props.getProperty("web.port", "8080"));
        adminPassword = props.getProperty("web.admin.password", "admin123");
    }

    public static void saveConfiguration() {
        Properties props = new Properties();
        props.setProperty("web.enabled", String.valueOf(isEnabled));
        props.setProperty("web.port", String.valueOf(port));
        props.setProperty("web.admin.password", adminPassword);
        
        try {
            props.store(Files.newOutputStream(Paths.get(CONFIG_FILE)), "Web Server Configuration");
        } catch (IOException e) {
            System.err.println("Could not save web config: " + e.getMessage());
        }
    }

    public static void startWebServer() {
        if (server != null && server.isRunning()) {
            System.out.println("🌐 Web Server is already running");
            return;
        }
        
        System.out.println("🌐 Starting Web Server on port " + port + "...");
        try {
            // Create server first
            server = new Server();
            
            // Configure server to bind to all interfaces (0.0.0.0) instead of just localhost
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(port);
            connector.setHost("0.0.0.0"); // Bind to all interfaces
            
            server.addConnector(connector);
            
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            
            
            // Add servlets
            context.addServlet(new ServletHolder(new AdminDashboardServlet()), "/admin");
            context.addServlet(new ServletHolder(new AdminApiServlet()), "/api/*");
            context.addServlet(new ServletHolder(new PublicRecordServlet()), "/public/record/*");
            context.addServlet(new ServletHolder(new AppleWalletServlet()), "/wallet/*");
            context.addServlet(new ServletHolder(new StaticFileServlet()), "/*");
            
            server.setHandler(context);
            
            // Start server in a separate thread to prevent blocking
            Thread serverThread = new Thread(() -> {
                try {
                    server.start();
                    System.out.println("🌐 Web Admin Dashboard started at: http://localhost:" + port + "/admin");
                    System.out.println("📱 Mobile-friendly interface available");
                    System.out.println("🔐 Admin password: " + adminPassword);
                    System.out.println("🔒 Basic authentication enabled for security");
                    
                    // Display network information for remote access
                    displayNetworkInfo();
                    
                    // Start periodic status updates
                    startStatusUpdates();
                    
                    // Keep the server running
                    server.join();
                } catch (Exception e) {
                    System.err.println("Web server error: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            
            serverThread.setDaemon(true); // Make it a daemon thread so it doesn't prevent JVM exit
            serverThread.start();
            
        } catch (Exception e) {
            System.err.println("Failed to start web server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void stopWebServer() {
        if (server != null && server.isRunning()) {
            try {
                server.stop();
                System.out.println("Web server stopped");
            } catch (Exception e) {
                System.err.println("Error stopping web server: " + e.getMessage());
            }
        }
        
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    public static void enableWebServer() {
        isEnabled = true;
        saveConfiguration();
        startWebServer();
    }

    public static void disableWebServer() {
        isEnabled = false;
        saveConfiguration();
        stopWebServer();
    }

    public static void setPort(int newPort) {
        port = newPort;
        saveConfiguration();
        if (isEnabled) {
            stopWebServer();
            startWebServer();
        }
    }

    public static void setAdminPassword(String newPassword) {
        adminPassword = newPassword;
        saveConfiguration();
    }

    public static boolean isEnabled() {
        return isEnabled;
    }

    public static int getPort() {
        return port;
    }

    public static String getAdminPassword() {
        return adminPassword;
    }

    private static void startStatusUpdates() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            if (isEnabled && server != null && server.isRunning()) {
                // Send periodic status updates
                // System status broadcast removed
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    // WebSocket broadcast methods removed - functionality not implemented

    private static void displayNetworkInfo() {
        try {
            System.out.println("\n🌍 NETWORK ACCESS INFORMATION:");
            System.out.println("═══════════════════════════════════════");
            
            // Get local IP addresses
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (!address.isLoopbackAddress() && address.getHostAddress().contains(".")) {
                            System.out.println("📱 Local Access: http://" + address.getHostAddress() + ":" + port + "/admin");
                        }
                    }
                }
            }
            
            // Try to get public IP
            try {
                String publicIP = getPublicIP();
                System.out.println("🌐 Public IP: " + publicIP);
                System.out.println("🌍 Remote Access: http://" + publicIP + ":" + port + "/admin");
                System.out.println("   (Requires port forwarding on your router)");
            } catch (Exception e) {
                System.out.println("🌐 Public IP: Could not determine automatically");
            }
            
            System.out.println("\n📋 REMOTE ACCESS SETUP:");
            System.out.println("1. Find your router's admin panel (usually 192.168.1.1 or 192.168.0.1)");
            System.out.println("2. Configure port forwarding: External Port " + port + " → Internal Port " + port);
            System.out.println("3. Use the 'Remote Access' URL above from anywhere");
            System.out.println("4. Login with: admin / " + adminPassword);
            System.out.println("═══════════════════════════════════════\n");
            
        } catch (SocketException e) {
            System.out.println("⚠️  Could not detect network interfaces");
        }
    }
    
    private static String getPublicIP() {
        try {
            // Try multiple services to get public IP
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
