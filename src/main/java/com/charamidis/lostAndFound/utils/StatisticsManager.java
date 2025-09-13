package com.charamidis.lostAndFound.utils;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class StatisticsManager {
    private static final Logger logger = Logger.getLogger(StatisticsManager.class.getName());
    private static StatisticsManager instance;
    private final Connection connection;
    private final List<StatisticsListener> listeners = new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    // Statistics data
    private int totalRecords = 0;
    private int totalReturns = 0;
    private int todayRecords = 0;
    private int todayReturns = 0;
    
    private StatisticsManager(Connection connection) {
        this.connection = connection;
        startPeriodicUpdate();
    }
    
    public static StatisticsManager getInstance(Connection connection) {
        if (instance == null) {
            instance = new StatisticsManager(connection);
        }
        return instance;
    }
    
    public static StatisticsManager getInstance() {
        return instance;
    }
    
    public interface StatisticsListener {
        void onStatisticsUpdated(int totalRecords, int totalReturns, int todayRecords, int todayReturns);
    }
    
    public void addListener(StatisticsListener listener) {
        listeners.add(listener);
        // Immediately notify with current data
        listener.onStatisticsUpdated(totalRecords, totalReturns, todayRecords, todayReturns);
    }
    
    public void removeListener(StatisticsListener listener) {
        listeners.remove(listener);
    }
    
    private void startPeriodicUpdate() {
        // Update statistics every 5 seconds
        scheduler.scheduleAtFixedRate(this::updateStatistics, 0, 5, TimeUnit.SECONDS);
    }
    
    private void updateStatistics() {
        try {
            Statement stmt = connection.createStatement();
            
            // Total records
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM records");
            int newTotalRecords = rs.next() ? rs.getInt("total") : 0;
            
            // Total returns
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM returns");
            int newTotalReturns = rs.next() ? rs.getInt("total") : 0;
            
            // Today's records
            rs = stmt.executeQuery("SELECT COUNT(*) as today FROM records WHERE date(record_datetime) = date('now')");
            int newTodayRecords = rs.next() ? rs.getInt("today") : 0;
            
            // Today's returns
            rs = stmt.executeQuery("SELECT COUNT(*) as today FROM returns WHERE date(return_date) = date('now')");
            int newTodayReturns = rs.next() ? rs.getInt("today") : 0;
            
            // Check if statistics have changed
            boolean hasChanged = (totalRecords != newTotalRecords) || 
                               (totalReturns != newTotalReturns) || 
                               (todayRecords != newTodayRecords) || 
                               (todayReturns != newTodayReturns);
            
            if (hasChanged) {
                totalRecords = newTotalRecords;
                totalReturns = newTotalReturns;
                todayRecords = newTodayRecords;
                todayReturns = newTodayReturns;
                
                // Notify listeners on JavaFX thread
                Platform.runLater(() -> {
                    for (StatisticsListener listener : listeners) {
                        listener.onStatisticsUpdated(totalRecords, totalReturns, todayRecords, todayReturns);
                    }
                });
            }
            
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error updating statistics", e);
        }
    }
    
    public void forceUpdate() {
        updateStatistics();
    }
    
    public void shutdown() {
        scheduler.shutdown();
    }
    
    // Getters for current statistics
    public int getTotalRecords() { return totalRecords; }
    public int getTotalReturns() { return totalReturns; }
    public int getTodayRecords() { return todayRecords; }
    public int getTodayReturns() { return todayReturns; }
}
