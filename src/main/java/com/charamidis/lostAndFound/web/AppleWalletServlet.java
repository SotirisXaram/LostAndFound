package com.charamidis.lostAndFound.web;

import com.charamidis.lostAndFound.models.Record;
import com.charamidis.lostAndFound.utils.AppleWalletPassGenerator;
import com.charamidis.lostAndFound.utils.SqliteDatabaseInitializer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppleWalletServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid record UID.");
            return;
        }
        
        String recordUid = pathInfo.substring(1); // Remove leading slash
        
        try (Connection conn = SqliteDatabaseInitializer.getConnection()) {
            Record record = getRecordByUid(conn, recordUid);
            
            if (record == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("Record not found.");
                return;
            }
            
            // Generate the Apple Wallet pass
            String baseUrl = getBaseUrl(request);
            byte[] passData = AppleWalletPassGenerator.generatePass(record, baseUrl);
            
            // Set response headers for Apple Wallet
            response.setContentType("application/vnd.apple.pkpass");
            response.setHeader("Content-Disposition", "attachment; filename=\"lost_item_" + recordUid + ".pkpass\"");
            response.setContentLength(passData.length);
            
            // Write the pass data
            response.getOutputStream().write(passData);
            response.getOutputStream().flush();
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error generating Apple Wallet pass: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private Record getRecordByUid(Connection conn, String uid) throws SQLException {
        String query = "SELECT r.*, u.first_name || ' ' || u.last_name as officer_name " +
                      "FROM records r " +
                      "LEFT JOIN users u ON r.officer_id = u.am " +
                      "WHERE r.uid = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, uid);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Record(
                    rs.getInt("id"),
                    rs.getString("uid"),
                    rs.getString("record_datetime"),
                    rs.getString("found_time"),
                    rs.getInt("officer_id"),
                    rs.getString("founder_last_name"),
                    rs.getString("founder_first_name"),
                    rs.getString("founder_id_number"),
                    rs.getString("founder_telephone"),
                    rs.getString("founder_street_address"),
                    rs.getString("founder_street_number"),
                    rs.getString("founder_father_name"),
                    rs.getString("founder_area_inhabitant"),
                    rs.getString("found_date"),
                    rs.getString("found_time"),
                    rs.getString("found_location"),
                    rs.getString("item_description"),
                    rs.getString("item_category"),
                    rs.getString("item_brand"),
                    rs.getString("item_model"),
                    rs.getString("item_color"),
                    rs.getString("item_serial_number"),
                    rs.getString("item_other_details"),
                    rs.getString("storage_location"),
                    rs.getString("status"),
                    rs.getString("picture_path")
                );
            }
        }
        
        return null;
    }
    
    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);
        
        if ((scheme.equals("http") && serverPort != 80) || 
            (scheme.equals("https") && serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        
        url.append(contextPath);
        
        return url.toString();
    }
}
