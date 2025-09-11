package com.charamidis.lostAndFound.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityLoggerTest {
    
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    
    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);
    }
    
    @Test
    void testLogRecordAddActivity() throws SQLException {
        try (MockedStatic<SqliteDatabaseInitializer> mockedStatic = 
             Mockito.mockStatic(SqliteDatabaseInitializer.class)) {
            
            mockedStatic.when(SqliteDatabaseInitializer::getConnection)
                       .thenReturn(mockConnection);
            
            assertDoesNotThrow(() -> {
                ActivityLogger.logRecordAddActivity("John Doe", "123", "Lost wallet");
            });
            
            verify(mockStatement, times(1)).setString(1, "John Doe");
            verify(mockStatement, times(1)).setString(2, "Record Add");
            verify(mockStatement, times(1)).setString(3, "Record ID: 123 - Lost wallet");
            verify(mockStatement, times(1)).executeUpdate();
        }
    }
    
    @Test
    void testLogRecordEditActivity() throws SQLException {
        try (MockedStatic<SqliteDatabaseInitializer> mockedStatic = 
             Mockito.mockStatic(SqliteDatabaseInitializer.class)) {
            
            mockedStatic.when(SqliteDatabaseInitializer::getConnection)
                       .thenReturn(mockConnection);
            
            assertDoesNotThrow(() -> {
                ActivityLogger.logRecordEditActivity("John Doe", "123", "Lost wallet");
            });
            
            verify(mockStatement, times(1)).setString(1, "John Doe");
            verify(mockStatement, times(1)).setString(2, "Record Edit");
            verify(mockStatement, times(1)).setString(3, "Record ID: 123 - Lost wallet");
            verify(mockStatement, times(1)).executeUpdate();
        }
    }
    
    @Test
    void testLogRecordDeleteActivity() throws SQLException {
        try (MockedStatic<SqliteDatabaseInitializer> mockedStatic = 
             Mockito.mockStatic(SqliteDatabaseInitializer.class)) {
            
            mockedStatic.when(SqliteDatabaseInitializer::getConnection)
                       .thenReturn(mockConnection);
            
            assertDoesNotThrow(() -> {
                ActivityLogger.logRecordDeleteActivity("John Doe", "123", "Lost wallet");
            });
            
            verify(mockStatement, times(1)).setString(1, "John Doe");
            verify(mockStatement, times(1)).setString(2, "Record Delete");
            verify(mockStatement, times(1)).setString(3, "Record ID: 123 - Lost wallet");
            verify(mockStatement, times(1)).executeUpdate();
        }
    }
    
    @Test
    void testLogReturnActivity() throws SQLException {
        try (MockedStatic<SqliteDatabaseInitializer> mockedStatic = 
             Mockito.mockStatic(SqliteDatabaseInitializer.class)) {
            
            mockedStatic.when(SqliteDatabaseInitializer::getConnection)
                       .thenReturn(mockConnection);
            
            assertDoesNotThrow(() -> {
                ActivityLogger.logReturnActivity("John Doe", "123", "Lost wallet");
            });
            
            verify(mockStatement, times(1)).setString(1, "John Doe");
            verify(mockStatement, times(1)).setString(2, "Item Return");
            verify(mockStatement, times(1)).setString(3, "Record ID: 123 - Lost wallet");
            verify(mockStatement, times(1)).executeUpdate();
        }
    }
    
    @Test
    void testLogLoginActivity() throws SQLException {
        try (MockedStatic<SqliteDatabaseInitializer> mockedStatic = 
             Mockito.mockStatic(SqliteDatabaseInitializer.class)) {
            
            mockedStatic.when(SqliteDatabaseInitializer::getConnection)
                       .thenReturn(mockConnection);
            
            assertDoesNotThrow(() -> {
                ActivityLogger.logLoginActivity("John Doe", "192.168.1.1");
            });
            
            verify(mockStatement, times(1)).setString(1, "John Doe");
            verify(mockStatement, times(1)).setString(2, "LOGIN");
            verify(mockStatement, times(1)).setString(3, "User logged in");
            verify(mockStatement, times(1)).executeUpdate();
        }
    }
    
    @Test
    void testLogLogoutActivity() throws SQLException {
        try (MockedStatic<SqliteDatabaseInitializer> mockedStatic = 
             Mockito.mockStatic(SqliteDatabaseInitializer.class)) {
            
            mockedStatic.when(SqliteDatabaseInitializer::getConnection)
                       .thenReturn(mockConnection);
            
            assertDoesNotThrow(() -> {
                ActivityLogger.logLogoutActivity("John Doe");
            });
            
            verify(mockStatement, times(1)).setString(1, "John Doe");
            verify(mockStatement, times(1)).setString(2, "LOGOUT");
            verify(mockStatement, times(1)).setString(3, "User logged out");
            verify(mockStatement, times(1)).executeUpdate();
        }
    }
    
    @Test
    void testLogPasswordChangeActivity() throws SQLException {
        try (MockedStatic<SqliteDatabaseInitializer> mockedStatic = 
             Mockito.mockStatic(SqliteDatabaseInitializer.class)) {
            
            mockedStatic.when(SqliteDatabaseInitializer::getConnection)
                       .thenReturn(mockConnection);
            
            assertDoesNotThrow(() -> {
                ActivityLogger.logPasswordChangeActivity("John Doe");
            });
            
            verify(mockStatement, times(1)).setString(1, "John Doe");
            verify(mockStatement, times(1)).setString(2, "PASSWORD_CHANGE");
            verify(mockStatement, times(1)).setString(3, "User changed password");
            verify(mockStatement, times(1)).executeUpdate();
        }
    }
    
    @Test
    void testLogBackupActivity() throws SQLException {
        try (MockedStatic<SqliteDatabaseInitializer> mockedStatic = 
             Mockito.mockStatic(SqliteDatabaseInitializer.class)) {
            
            mockedStatic.when(SqliteDatabaseInitializer::getConnection)
                       .thenReturn(mockConnection);
            
            assertDoesNotThrow(() -> {
                ActivityLogger.logBackupActivity("John Doe", "backup.db");
            });
            
            verify(mockStatement, times(1)).setString(1, "John Doe");
            verify(mockStatement, times(1)).setString(2, "Backup");
            verify(mockStatement, times(1)).setString(3, "Database backup created: backup.db");
            verify(mockStatement, times(1)).executeUpdate();
        }
    }
    
    @Test
    void testLogExportActivity() throws SQLException {
        try (MockedStatic<SqliteDatabaseInitializer> mockedStatic = 
             Mockito.mockStatic(SqliteDatabaseInitializer.class)) {
            
            mockedStatic.when(SqliteDatabaseInitializer::getConnection)
                       .thenReturn(mockConnection);
            
            assertDoesNotThrow(() -> {
                ActivityLogger.logExportActivity("John Doe", "Excel");
            });
            
            verify(mockStatement, times(1)).setString(1, "John Doe");
            verify(mockStatement, times(1)).setString(2, "EXPORT_EXCEL");
            verify(mockStatement, times(1)).setString(3, "Data exported: Excel");
            verify(mockStatement, times(1)).executeUpdate();
        }
    }
    
    @Test
    void testLogSettingsChangeActivity() throws SQLException {
        try (MockedStatic<SqliteDatabaseInitializer> mockedStatic = 
             Mockito.mockStatic(SqliteDatabaseInitializer.class)) {
            
            mockedStatic.when(SqliteDatabaseInitializer::getConnection)
                       .thenReturn(mockConnection);
            
            assertDoesNotThrow(() -> {
                ActivityLogger.logSettingsChangeActivity("John Doe", "theme", "light", "dark");
            });
            
            verify(mockStatement, times(1)).setString(1, "John Doe");
            verify(mockStatement, times(1)).setString(2, "SETTINGS_CHANGE");
            verify(mockStatement, times(1)).setString(3, "Setting 'theme' changed from 'light' to 'dark'");
            verify(mockStatement, times(1)).executeUpdate();
        }
    }
}
