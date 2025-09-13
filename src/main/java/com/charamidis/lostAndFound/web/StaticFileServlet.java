package com.charamidis.lostAndFound.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        
        // Handle image files
        if (path.startsWith("/images/") || path.endsWith(".jpg") || path.endsWith(".jpeg") || 
            path.endsWith(".png") || path.endsWith(".gif") || path.endsWith(".bmp")) {
            
            // Remove leading slash and construct file path
            String filePath = path.startsWith("/") ? path.substring(1) : path;
            
            // Cross-platform path handling
            String userHome = System.getProperty("user.home");
            String desktopPath = userHome + File.separator + "Desktop" + File.separator + "export_data";
            String imagesPath = desktopPath + File.separator + "images";
            
            // Try different possible image locations
            String[] possiblePaths = {
                // For paths like "images/record_XXX.jpg" -> "~/Desktop/export_data/images/record_XXX.jpg"
                desktopPath + File.separator + filePath,
                // For paths like "record_XXX.jpg" -> "~/Desktop/export_data/images/record_XXX.jpg"
                imagesPath + File.separator + filePath.replace("images/", ""),
                // Direct path
                filePath,
                // Relative path
                "images" + File.separator + filePath
            };
            
            File imageFile = null;
            for (String possiblePath : possiblePaths) {
                File testFile = new File(possiblePath);
                if (testFile.exists() && testFile.isFile()) {
                    imageFile = testFile;
                    break;
                }
            }
            
            if (imageFile != null && imageFile.exists()) {
                // Set appropriate content type
                String contentType = getContentType(imageFile.getName());
                response.setContentType(contentType);
                response.setContentLengthLong(imageFile.length());
                
                // Copy file to response
                try (FileInputStream fis = new FileInputStream(imageFile);
                     OutputStream os = response.getOutputStream()) {
                    
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
                return;
            }
        }
        
        // For other files, return 404
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>404 Not Found</title></head>");
        out.println("<body><h1>404 - File Not Found</h1>");
        out.println("<p>The requested file was not found.</p>");
        out.println("<p><a href='/admin'>Go to Admin Dashboard</a></p>");
        out.println("</body></html>");
    }
    
    private String getContentType(String fileName) {
        String extension = fileName.toLowerCase();
        if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (extension.endsWith(".png")) {
            return "image/png";
        } else if (extension.endsWith(".gif")) {
            return "image/gif";
        } else if (extension.endsWith(".bmp")) {
            return "image/bmp";
        } else {
            return "application/octet-stream";
        }
    }
}
