package com.charamidis.lostAndFound.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class StaticFileServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getPathInfo();
        
        if (path == null || path.equals("/") || path.equals("/index.html")) {
            // Redirect to admin dashboard
            response.sendRedirect("/admin");
            return;
        }
        
        // For now, just return a simple 404 for other static files
        // In a production app, you'd serve actual static files
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>404 Not Found</title></head>");
        out.println("<body><h1>404 - File Not Found</h1>");
        out.println("<p>The requested file was not found.</p>");
        out.println("<p><a href='/admin'>Go to Admin Dashboard</a></p>");
        out.println("</body></html>");
    }
}
