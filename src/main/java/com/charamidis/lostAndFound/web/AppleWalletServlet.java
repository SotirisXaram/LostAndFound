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
            
            // For now, redirect to the public record page with Apple Wallet information
            // Apple Wallet passes require Apple Developer certificates for proper signing
            String baseUrl = getBaseUrl(request);
            String publicRecordUrl = baseUrl + "/public/record/" + recordUid;
            
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println(generateAppleWalletInfoPage(record, publicRecordUrl));
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error: " + e.getMessage());
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
    
    private String generateAppleWalletInfoPage(Record record, String publicRecordUrl) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"en\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>Apple Wallet - Lost & Found</title>\n");
        html.append("    <style>\n");
        html.append("        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; margin: 0; padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; display: flex; align-items: center; justify-content: center; }\n");
        html.append("        .container { max-width: 600px; background: white; border-radius: 20px; box-shadow: 0 20px 40px rgba(0,0,0,0.3); overflow: hidden; }\n");
        html.append("        .header { background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%); color: white; padding: 40px; text-align: center; }\n");
        html.append("        .header h1 { margin: 0; font-size: 2.5em; font-weight: 300; }\n");
        html.append("        .header p { margin: 10px 0 0 0; opacity: 0.8; font-size: 1.1em; }\n");
        html.append("        .content { padding: 40px; }\n");
        html.append("        .info-box { background: #f8f9fa; padding: 20px; border-radius: 15px; margin: 20px 0; border-left: 5px solid #3498db; }\n");
        html.append("        .info-box h3 { margin: 0 0 15px 0; color: #2c3e50; }\n");
        html.append("        .info-box p { margin: 0; color: #555; line-height: 1.6; }\n");
        html.append("        .record-info { background: #e8f4fd; padding: 20px; border-radius: 15px; margin: 20px 0; }\n");
        html.append("        .record-info h3 { margin: 0 0 15px 0; color: #2c3e50; }\n");
        html.append("        .record-details { display: grid; grid-template-columns: 1fr 1fr; gap: 15px; }\n");
        html.append("        .record-details div { padding: 10px; background: white; border-radius: 8px; }\n");
        html.append("        .record-details strong { color: #2c3e50; }\n");
        html.append("        .buttons { text-align: center; margin: 30px 0; }\n");
        html.append("        .btn { display: inline-block; padding: 15px 30px; margin: 10px; border-radius: 10px; text-decoration: none; font-weight: 600; transition: all 0.3s ease; }\n");
        html.append("        .btn-primary { background: #3498db; color: white; }\n");
        html.append("        .btn-primary:hover { background: #2980b9; transform: translateY(-2px); }\n");
        html.append("        .btn-secondary { background: #95a5a6; color: white; }\n");
        html.append("        .btn-secondary:hover { background: #7f8c8d; transform: translateY(-2px); }\n");
        html.append("        .warning { background: #fff3cd; border: 1px solid #ffeaa7; color: #856404; padding: 15px; border-radius: 10px; margin: 20px 0; }\n");
        html.append("        .warning strong { color: #856404; }\n");
        html.append("        @media (max-width: 768px) { .record-details { grid-template-columns: 1fr; } }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"container\">\n");
        html.append("        <div class=\"header\">\n");
        html.append("            <h1>📱 Apple Wallet</h1>\n");
        html.append("            <p>Lost & Found Record</p>\n");
        html.append("        </div>\n");
        html.append("        \n");
        html.append("        <div class=\"content\">\n");
        html.append("            <div class=\"warning\">\n");
        html.append("                <strong>⚠️ Apple Wallet Pass Not Available</strong><br>\n");
        html.append("                Apple Wallet passes require Apple Developer certificates for proper signing. This feature is currently not available.\n");
        html.append("            </div>\n");
        html.append("            \n");
        html.append("            <div class=\"info-box\">\n");
        html.append("                <h3>📋 Record Information</h3>\n");
        html.append("                <p>You can view the complete record details and contact information below:</p>\n");
        html.append("            </div>\n");
        html.append("            \n");
        html.append("            <div class=\"record-info\">\n");
        html.append("                <h3>📝 Item Details</h3>\n");
        html.append("                <div class=\"record-details\">\n");
        html.append("                    <div><strong>Item:</strong> ").append(escapeHtml(record.getItem_description() != null ? record.getItem_description() : "Not specified")).append("</div>\n");
        html.append("                    <div><strong>Record ID:</strong> ").append(record.getId()).append("</div>\n");
        html.append("                    <div><strong>Found Date:</strong> ").append(record.getFound_date() != null ? record.getFound_date() : "Not specified").append("</div>\n");
        html.append("                    <div><strong>Found Time:</strong> ").append(record.getFound_time() != null ? record.getFound_time() : "Not specified").append("</div>\n");
        html.append("                    <div><strong>Location:</strong> ").append(escapeHtml(record.getFound_location() != null ? record.getFound_location() : "Not specified")).append("</div>\n");
        html.append("                    <div><strong>Status:</strong> ").append(record.getStatus() != null ? record.getStatus() : "stored").append("</div>\n");
        html.append("                </div>\n");
        html.append("            </div>\n");
        html.append("            \n");
        html.append("            <div class=\"buttons\">\n");
        html.append("                <a href=\"").append(publicRecordUrl).append("\" class=\"btn btn-primary\">📄 View Full Record</a>\n");
        html.append("                <a href=\"javascript:history.back()\" class=\"btn btn-secondary\">← Go Back</a>\n");
        html.append("            </div>\n");
        html.append("            \n");
        html.append("            <div class=\"info-box\">\n");
        html.append("                <h3>💡 Alternative Solutions</h3>\n");
        html.append("                <p>• <strong>Bookmark the record:</strong> Save the record URL in your browser bookmarks<br>\n");
        html.append("                • <strong>Take a screenshot:</strong> Capture the record details for offline access<br>\n");
        html.append("                • <strong>Contact the office:</strong> Call or visit the Lost & Found office for assistance</p>\n");
        html.append("            </div>\n");
        html.append("        </div>\n");
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>\n");
        
        return html.toString();
    }
    
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
}
