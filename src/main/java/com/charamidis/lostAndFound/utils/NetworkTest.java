package com.charamidis.lostAndFound.utils;

import java.net.*;
import java.util.Enumeration;

public class NetworkTest {
    
    public static void testNetworkConnectivity() {
        System.out.println("\n🔍 NETWORK CONNECTIVITY TEST");
        System.out.println("═══════════════════════════════════════");
        
        try {
            // Test 1: Get all network interfaces
            System.out.println("📡 Available Network Interfaces:");
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }
                
                System.out.println("  Interface: " + networkInterface.getName() + 
                                 " (Display: " + networkInterface.getDisplayName() + ")");
                
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    
                    if (address.isLoopbackAddress()) {
                        continue;
                    }
                    
                    String hostAddress = address.getHostAddress();
                    System.out.println("    IP: " + hostAddress + 
                                     " (Type: " + (address instanceof Inet4Address ? "IPv4" : "IPv6") + ")");
                    
                    // Test if this is a private network IP
                    if (isPrivateNetworkIP(hostAddress)) {
                        System.out.println("    ✅ Private network IP - Good for mobile access");
                        
                        // Test if we can bind to this IP
                        try (ServerSocket testSocket = new ServerSocket(0, 1, address)) {
                            System.out.println("    ✅ Can bind to this IP");
                        } catch (Exception e) {
                            System.out.println("    ❌ Cannot bind to this IP: " + e.getMessage());
                        }
                    } else {
                        System.out.println("    ⚠️  Public IP - May not work for local mobile access");
                    }
                }
            }
            
            // Test 2: Check if web server port is accessible
            System.out.println("\n🌐 Web Server Port Test:");
            try (ServerSocket testSocket = new ServerSocket(8080)) {
                System.out.println("❌ Port 8080 is available (web server not running)");
            } catch (BindException e) {
                System.out.println("✅ Port 8080 is in use (web server is running)");
            } catch (Exception e) {
                System.out.println("⚠️  Error testing port 8080: " + e.getMessage());
            }
            
            // Test 3: Try to get the best local IP
            System.out.println("\n🎯 Recommended IP for QR Codes:");
            String bestIP = getBestLocalIP();
            if (bestIP != null) {
                System.out.println("✅ Best IP: " + bestIP);
                System.out.println("📱 Mobile URL: http://" + bestIP + ":8080/public/record/[UID]");
                System.out.println("🔧 Admin URL: http://" + bestIP + ":8080/admin");
            } else {
                System.out.println("❌ No suitable local IP found");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during network test: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("═══════════════════════════════════════\n");
    }
    
    private static boolean isPrivateNetworkIP(String ip) {
        return ip.startsWith("192.168.") || 
               ip.startsWith("10.") || 
               (ip.startsWith("172.") && isInPrivateRange(ip));
    }
    
    private static boolean isInPrivateRange(String ip) {
        try {
            String[] parts = ip.split("\\.");
            if (parts.length == 4) {
                int secondOctet = Integer.parseInt(parts[1]);
                return secondOctet >= 16 && secondOctet <= 31;
            }
        } catch (Exception e) {
            // Ignore parsing errors
        }
        return false;
    }
    
    private static String getBestLocalIP() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }
                
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    
                    if (address.isLoopbackAddress() || !(address instanceof Inet4Address)) {
                        continue;
                    }
                    
                    String hostAddress = address.getHostAddress();
                    
                    if (isPrivateNetworkIP(hostAddress)) {
                        return hostAddress;
                    }
                }
            }
        } catch (Exception e) {
            // Ignore errors
        }
        return null;
    }
}
