package com.charamidis.lostAndFound.web;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminApiIntegrationTest {

    private AdminApiServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new AdminApiServlet();
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void testGetStats() throws Exception {
        when(request.getPathInfo()).thenReturn("/stats");
        
        servlet.doGet(request, response);
        
        String jsonResponse = responseWriter.toString();
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        
        assertTrue(jsonObject.has("totalRecords"));
        assertTrue(jsonObject.has("totalReturns"));
        assertTrue(jsonObject.has("timestamp"));
    }

    @Test
    void testGetSystemStatus() throws Exception {
        when(request.getPathInfo()).thenReturn("/system-status");
        
        servlet.doGet(request, response);
        
        String jsonResponse = responseWriter.toString();
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        
        assertTrue(jsonObject.has("databaseConnected"));
        assertTrue(jsonObject.has("webServerEnabled"));
    }
}
