package com.charamidis.lostAndFound.web;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CopyOnWriteArrayList;

@WebSocket
public class AdminWebSocket {
    private static final CopyOnWriteArrayList<Session> sessions = new CopyOnWriteArrayList<>();
    private static final Gson gson = new Gson();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        sessions.add(session);
        System.out.println("Admin WebSocket connected: " + session.getRemoteAddress());
        
        // Send welcome message
        JsonObject welcome = new JsonObject();
        welcome.addProperty("type", "welcome");
        welcome.addProperty("message", "Connected to Lost & Found Admin Dashboard");
        welcome.addProperty("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        try {
            session.getRemote().sendString(gson.toJson(welcome));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.remove(session);
        System.out.println("Admin WebSocket disconnected: " + session.getRemoteAddress());
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        // Handle incoming messages from admin dashboard
        System.out.println("Received from admin: " + message);
    }

    public static void broadcastRecordChange(String action, String recordId, String details) {
        JsonObject message = new JsonObject();
        message.addProperty("type", "record_change");
        message.addProperty("action", action);
        message.addProperty("recordId", recordId);
        message.addProperty("details", details);
        message.addProperty("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        String jsonMessage = gson.toJson(message);
        
        for (Session session : sessions) {
            if (session.isOpen()) {
                try {
                    session.getRemote().sendString(jsonMessage);
                } catch (IOException e) {
                    System.err.println("Error sending message to admin: " + e.getMessage());
                    sessions.remove(session);
                }
            }
        }
    }

    public static void broadcastUserActivity(String user, String activity, String details) {
        JsonObject message = new JsonObject();
        message.addProperty("type", "user_activity");
        message.addProperty("user", user);
        message.addProperty("activity", activity);
        message.addProperty("details", details);
        message.addProperty("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        String jsonMessage = gson.toJson(message);
        
        for (Session session : sessions) {
            if (session.isOpen()) {
                try {
                    session.getRemote().sendString(jsonMessage);
                } catch (IOException e) {
                    System.err.println("Error sending message to admin: " + e.getMessage());
                    sessions.remove(session);
                }
            }
        }
    }

    public static void broadcastSystemAlert(String alertType, String message) {
        JsonObject alert = new JsonObject();
        alert.addProperty("type", "system_alert");
        alert.addProperty("alertType", alertType);
        alert.addProperty("message", message);
        alert.addProperty("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        String jsonMessage = gson.toJson(alert);
        
        for (Session session : sessions) {
            if (session.isOpen()) {
                try {
                    session.getRemote().sendString(jsonMessage);
                } catch (IOException e) {
                    System.err.println("Error sending alert to admin: " + e.getMessage());
                    sessions.remove(session);
                }
            }
        }
    }

    public static void broadcastActivity(String user, String action, String details) {
        JsonObject activity = new JsonObject();
        activity.addProperty("type", "activity");
        activity.addProperty("user", user);
        activity.addProperty("action", action);
        activity.addProperty("details", details);
        activity.addProperty("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        String jsonMessage = gson.toJson(activity);
        
        for (Session session : sessions) {
            if (session.isOpen()) {
                try {
                    session.getRemote().sendString(jsonMessage);
                } catch (IOException e) {
                    System.err.println("Error sending activity to admin: " + e.getMessage());
                    sessions.remove(session);
                }
            }
        }
    }
}
