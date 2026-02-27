package com.charamidis.lostAndFound.utils;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsManagerTest {

    @BeforeAll
    static void initJFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            new JFXPanel(); // Initializes JavaFX environment
            latch.countDown();
        });
        latch.await();
    }

    @Mock
    private Connection mockConnection;

    @Mock
    private Statement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws Exception {
        StatisticsManager.resetInstance();
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
    }

    @Test
    void testStatisticsUpdate() throws Exception {
        // Set up mock responses for different queries
        when(mockResultSet.next()).thenReturn(true);
        // Queries are: records total, returns total, records today, returns today
        when(mockResultSet.getInt(anyString())).thenReturn(10, 5, 2, 1);

        StatisticsManager statsManager = StatisticsManager.getInstance(mockConnection, false);
        statsManager.forceUpdate();

        assertEquals(10, statsManager.getTotalRecords());
        assertEquals(5, statsManager.getTotalReturns());
        assertEquals(2, statsManager.getTodayRecords());
        assertEquals(1, statsManager.getTodayReturns());
    }

    @Test
    void testListenerNotification() throws Exception {
        // Mocking values that are different from initial 0 to trigger hasChanged
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(anyString())).thenReturn(20, 10, 5, 2);

        StatisticsManager statsManager = StatisticsManager.getInstance(mockConnection, false);
        
        StatisticsManager.StatisticsListener mockListener = mock(StatisticsManager.StatisticsListener.class);
        statsManager.addListener(mockListener);
        
        // Initial notification on addListener (0,0,0,0)
        verify(mockListener).onStatisticsUpdated(0, 0, 0, 0);

        CountDownLatch latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(mockListener).onStatisticsUpdated(20, 10, 5, 2);

        statsManager.forceUpdate();
        
        // Wait for Platform.runLater
        latch.await(2, TimeUnit.SECONDS);
        
        verify(mockListener).onStatisticsUpdated(20, 10, 5, 2);
    }
}
