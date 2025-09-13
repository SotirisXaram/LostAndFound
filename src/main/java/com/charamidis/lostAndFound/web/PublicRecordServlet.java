package com.charamidis.lostAndFound.web;

import com.charamidis.lostAndFound.models.Record;
import com.charamidis.lostAndFound.models.User;
import com.charamidis.lostAndFound.utils.SqliteDatabaseInitializer;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PublicRecordServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if QR code functionality is enabled
        if (!isQRCodeEnabled()) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println(generateErrorPage("QR Code functionality is currently disabled."));
            return;
        }
        
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println(generateErrorPage("Invalid record ID."));
            return;
        }
        
        String recordUid = pathInfo.substring(1); // Remove leading slash
        
        try (Connection conn = SqliteDatabaseInitializer.getConnection()) {
            Record record = getRecordByUid(conn, recordUid);
            
            if (record == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().println(generateErrorPage("Record not found."));
                return;
            }
            
            // Get officer name
            String officerName = getOfficerName(conn, record.getOfficer_id());
            
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println(generateRecordPage(record, officerName));
            
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println(generateErrorPage("Database error occurred."));
            e.printStackTrace();
        }
    }
    
    private Record getRecordByUid(Connection conn, String uid) throws SQLException {
        String query = "SELECT * FROM records WHERE uid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, uid);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Record record = new Record();
                record.setId(rs.getInt("id"));
                record.setUid(rs.getString("uid"));
                record.setOfficer_id(rs.getInt("officer_id"));
                record.setFounder_last_name(rs.getString("founder_last_name"));
                record.setFounder_first_name(rs.getString("founder_first_name"));
                record.setFounder_id_number(rs.getString("founder_id_number"));
                record.setFounder_telephone(rs.getString("founder_telephone"));
                record.setFounder_street_address(rs.getString("founder_street_address"));
                record.setFounder_street_number(rs.getString("founder_street_number"));
                record.setFounder_father_name(rs.getString("founder_father_name"));
                record.setFounder_area_inhabitant(rs.getString("founder_area_inhabitant"));
                record.setFound_date(rs.getString("found_date"));
                record.setFound_time(rs.getString("found_time"));
                record.setFound_location(rs.getString("found_location"));
                record.setItem_description(rs.getString("item_description"));
                record.setItem_category(rs.getString("item_category"));
                record.setItem_brand(rs.getString("item_brand"));
                record.setItem_model(rs.getString("item_model"));
                record.setItem_color(rs.getString("item_color"));
                record.setItem_serial_number(rs.getString("item_serial_number"));
                record.setItem_other_details(rs.getString("item_other_details"));
                record.setStorage_location(rs.getString("storage_location"));
                record.setStatus(rs.getString("status"));
                record.setPicture_path(rs.getString("picture_path"));
                return record;
            }
        }
        return null;
    }
    
    private String getOfficerName(Connection conn, int officerId) throws SQLException {
        String query = "SELECT first_name, last_name FROM users WHERE am = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, officerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("first_name") + " " + rs.getString("last_name");
            }
        }
        return "Unknown Officer";
    }
    
    private boolean isQRCodeEnabled() {
        // Check if QR code functionality is enabled in settings
        // For now, we'll assume it's always enabled, but this can be made configurable
        return true; // TODO: Make this configurable via admin settings
    }
    
    private String generateRecordPage(Record record, String officerName) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"el\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>Record Details - Lost & Found</title>\n");
        html.append("    <style>\n");
        html.append("        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }\n");
        html.append("        .container { max-width: 800px; margin: 0 auto; background: white; border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.3); overflow: hidden; }\n");
        html.append("        .header { background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%); color: white; padding: 30px; text-align: center; }\n");
        html.append("        .header h1 { margin: 0; font-size: 2.5em; font-weight: 300; }\n");
        html.append("        .header p { margin: 10px 0 0 0; opacity: 0.8; font-size: 1.1em; }\n");
        html.append("        .content { padding: 40px; }\n");
        html.append("        .record-info { display: grid; grid-template-columns: 1fr 1fr; gap: 30px; margin-bottom: 30px; }\n");
        html.append("        .info-section h3 { color: #2c3e50; margin-bottom: 15px; padding-bottom: 10px; border-bottom: 2px solid #3498db; font-size: 1.3em; }\n");
        html.append("        .info-item { margin-bottom: 12px; }\n");
        html.append("        .info-label { font-weight: bold; color: #555; display: inline-block; width: 140px; }\n");
        html.append("        .info-value { color: #333; }\n");
        html.append("        .item-image { text-align: center; margin: 30px 0; padding: 20px; background: #f8f9fa; border-radius: 15px; border: 2px dashed #dee2e6; }\n");
        html.append("        .item-image img { max-width: 100%; max-height: 500px; border-radius: 10px; box-shadow: 0 5px 15px rgba(0,0,0,0.2); transition: transform 0.3s ease; cursor: pointer; }\n");
        html.append("        .item-image img:hover { transform: scale(1.02); }\n");
        html.append("        .no-image { color: #7f8c8d; font-style: italic; text-align: center; padding: 20px; }\n");
        html.append("        .status-badge { display: inline-block; padding: 8px 16px; border-radius: 20px; font-weight: bold; text-transform: uppercase; font-size: 0.9em; }\n");
        html.append("        .status-stored { background: #e8f5e8; color: #27ae60; }\n");
        html.append("        .status-returned { background: #e8f4fd; color: #3498db; }\n");
        html.append("        .status-disposed { background: #fdf2e8; color: #e67e22; }\n");
        html.append("        .footer { background: #f8f9fa; padding: 20px; text-align: center; color: #666; border-top: 1px solid #eee; }\n");
        html.append("        .qr-info { background: #e8f4fd; padding: 20px; border-radius: 10px; margin: 20px 0; text-align: center; }\n");
        html.append("        .qr-info h4 { margin: 0 0 10px 0; color: #2c3e50; }\n");
        html.append("        .qr-info p { margin: 0; color: #555; }\n");
        html.append("        @media (max-width: 768px) { .record-info { grid-template-columns: 1fr; } }\n");
        html.append("        /* Image Modal Styles */\n");
        html.append("        .image-modal { display: none; position: fixed; z-index: 1000; left: 0; top: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.9); }\n");
        html.append("        .image-modal-content { margin: auto; display: block; width: 80%; max-width: 700px; max-height: 80%; object-fit: contain; }\n");
        html.append("        .image-modal-close { position: absolute; top: 15px; right: 35px; color: #f1f1f1; font-size: 40px; font-weight: bold; cursor: pointer; }\n");
        html.append("        .image-modal-close:hover { color: #bbb; }\n");
        html.append("        /* Apple Wallet Button Styles */\n");
        html.append("        .wallet-section { background: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #eee; }\n");
        html.append("        .wallet-button-container { max-width: 400px; margin: 0 auto; }\n");
        html.append("        .wallet-button { display: inline-flex; align-items: center; justify-content: center; background: #000; color: white; text-decoration: none; padding: 15px 30px; border-radius: 10px; font-size: 16px; font-weight: 600; transition: all 0.3s ease; box-shadow: 0 4px 15px rgba(0,0,0,0.2); }\n");
        html.append("        .wallet-button:hover { background: #333; transform: translateY(-2px); box-shadow: 0 6px 20px rgba(0,0,0,0.3); color: white; text-decoration: none; }\n");
        html.append("        .wallet-icon { font-size: 20px; margin-right: 10px; }\n");
        html.append("        .wallet-text { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; }\n");
        html.append("        .wallet-description { margin-top: 15px; color: #666; font-size: 14px; line-height: 1.4; }\n");
        html.append("        @media (max-width: 768px) { .wallet-button { padding: 12px 24px; font-size: 14px; } }\n");
        html.append("    </style>\n");
        html.append("    <script>\n");
        html.append("        function openImageModal(src) {\n");
        html.append("            const modal = document.getElementById('imageModal');\n");
        html.append("            const modalImg = document.getElementById('modalImage');\n");
        html.append("            modal.style.display = 'block';\n");
        html.append("            modalImg.src = src;\n");
        html.append("        }\n");
        html.append("        function closeImageModal() {\n");
        html.append("            document.getElementById('imageModal').style.display = 'none';\n");
        html.append("        }\n");
        html.append("        // Close modal when clicking outside the image\n");
        html.append("        window.onclick = function(event) {\n");
        html.append("            const modal = document.getElementById('imageModal');\n");
        html.append("            if (event.target == modal) {\n");
        html.append("                modal.style.display = 'none';\n");
        html.append("            }\n");
        html.append("        }\n");
        html.append("        // Apple Wallet functionality\n");
        html.append("        function addToWallet(event) {\n");
        html.append("            event.preventDefault();\n");
        html.append("            const url = event.target.href;\n");
        html.append("            \n");
        html.append("            // Check if device supports Apple Wallet\n");
        html.append("            if (navigator.userAgent.indexOf('iPhone') > -1 || navigator.userAgent.indexOf('iPad') > -1) {\n");
        html.append("                // For iOS devices, try to open the pass directly\n");
        html.append("                window.location.href = url;\n");
        html.append("            } else {\n");
        html.append("                // For other devices, show instructions\n");
        html.append("                alert('Apple Wallet is only available on iOS devices (iPhone/iPad).\\n\\n' +\n");
        html.append("                      'To add this pass to your Apple Wallet:\\n' +\n");
        html.append("                      '1. Open this page on your iPhone or iPad\\n' +\n");
        html.append("                      '2. Tap the \"Add to Apple Wallet\" button\\n' +\n");
        html.append("                      '3. Follow the prompts to add to your wallet');\n");
        html.append("            }\n");
        html.append("        }\n");
        html.append("    </script>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"container\">\n");
        html.append("        <div class=\"header\">\n");
        html.append("            <h1>📋 Lost & Found Record</h1>\n");
        html.append("            <p>Record ID: ").append(record.getId()).append(" | UID: ").append(record.getUid()).append("</p>\n");
        html.append("        </div>\n");
        html.append("        \n");
        html.append("        <div class=\"content\">\n");
        html.append("            <div class=\"qr-info\">\n");
        html.append("                <h4>🔍 Scanned via QR Code</h4>\n");
        html.append("                <p>This record was accessed by scanning a QR code. Contact the office for more information.</p>\n");
        html.append("            </div>\n");
        html.append("            \n");
        html.append("            <div class=\"record-info\">\n");
        html.append("                <div class=\"info-section\">\n");
        html.append("                    <h3>📝 Item Information</h3>\n");
        html.append("                    <div class=\"info-item\"><span class=\"info-label\">Description:</span><span class=\"info-value\">").append(escapeHtml(record.getItem_description())).append("</span></div>\n");
        if (record.getItem_category() != null && !record.getItem_category().isEmpty()) {
            html.append("                    <div class=\"info-item\"><span class=\"info-label\">Category:</span><span class=\"info-value\">").append(escapeHtml(record.getItem_category())).append("</span></div>\n");
        }
        if (record.getItem_brand() != null && !record.getItem_brand().isEmpty()) {
            html.append("                    <div class=\"info-item\"><span class=\"info-label\">Brand:</span><span class=\"info-value\">").append(escapeHtml(record.getItem_brand())).append("</span></div>\n");
        }
        if (record.getItem_model() != null && !record.getItem_model().isEmpty()) {
            html.append("                    <div class=\"info-item\"><span class=\"info-label\">Model:</span><span class=\"info-value\">").append(escapeHtml(record.getItem_model())).append("</span></div>\n");
        }
        if (record.getItem_color() != null && !record.getItem_color().isEmpty()) {
            html.append("                    <div class=\"info-item\"><span class=\"info-label\">Color:</span><span class=\"info-value\">").append(escapeHtml(record.getItem_color())).append("</span></div>\n");
        }
        if (record.getItem_serial_number() != null && !record.getItem_serial_number().isEmpty()) {
            html.append("                    <div class=\"info-item\"><span class=\"info-label\">Serial:</span><span class=\"info-value\">").append(escapeHtml(record.getItem_serial_number())).append("</span></div>\n");
        }
        html.append("                    <div class=\"info-item\"><span class=\"info-label\">Status:</span><span class=\"info-value\"><span class=\"status-badge status-").append(record.getStatus() != null ? record.getStatus() : "stored").append("\">").append(record.getStatus() != null ? record.getStatus() : "stored").append("</span></span></div>\n");
        html.append("                </div>\n");
        html.append("                \n");
        html.append("                <div class=\"info-section\">\n");
        html.append("                    <h3>📍 Location & Time</h3>\n");
        html.append("                    <div class=\"info-item\"><span class=\"info-label\">Found Date:</span><span class=\"info-value\">").append(record.getFound_date() != null ? record.getFound_date() : "N/A").append("</span></div>\n");
        html.append("                    <div class=\"info-item\"><span class=\"info-label\">Found Time:</span><span class=\"info-value\">").append(record.getFound_time() != null ? record.getFound_time() : "N/A").append("</span></div>\n");
        html.append("                    <div class=\"info-item\"><span class=\"info-label\">Location:</span><span class=\"info-value\">").append(escapeHtml(record.getFound_location() != null ? record.getFound_location() : "N/A")).append("</span></div>\n");
        if (record.getStorage_location() != null && !record.getStorage_location().isEmpty()) {
            html.append("                    <div class=\"info-item\"><span class=\"info-label\">Storage:</span><span class=\"info-value\">").append(escapeHtml(record.getStorage_location())).append("</span></div>\n");
        }
        html.append("                </div>\n");
        html.append("            </div>\n");
        html.append("            \n");
        if (record.getPicture_path() != null && !record.getPicture_path().isEmpty()) {
            // Convert database path to web-accessible path
            // Database stores: "images/record_XXX.jpg"
            // Web needs: "/images/record_XXX.jpg"
            String imagePath = record.getPicture_path();
            if (!imagePath.startsWith("/")) {
                imagePath = "/" + imagePath;
            }
            // Normalize path separators for web (always use forward slashes)
            imagePath = imagePath.replace("\\", "/");
            
            html.append("            <div class=\"item-image\">\n");
            html.append("                <h3 style=\"color: #2c3e50; margin-bottom: 15px; text-align: center;\">📷 Item Photo</h3>\n");
            html.append("                <img src=\"").append(escapeHtml(imagePath)).append("\" alt=\"Item Image\" style=\"max-width: 100%; max-height: 500px; border-radius: 10px; box-shadow: 0 5px 15px rgba(0,0,0,0.2); display: block; margin: 0 auto;\" onerror=\"this.parentElement.style.display='none';\" onclick=\"openImageModal(this.src)\">\n");
            html.append("            </div>\n");
        }
        html.append("            \n");
        html.append("            <div class=\"record-info\">\n");
        html.append("                <div class=\"info-section\">\n");
        html.append("                    <h3>👤 Officer Information</h3>\n");
        html.append("                    <div class=\"info-item\"><span class=\"info-label\">Officer:</span><span class=\"info-value\">").append(escapeHtml(officerName)).append("</span></div>\n");
        html.append("                </div>\n");
        html.append("                \n");
        html.append("                <div class=\"info-section\">\n");
        html.append("                    <h3>📞 Contact Information</h3>\n");
        html.append("                    <div class=\"info-item\"><span class=\"info-label\">Founder:</span><span class=\"info-value\">").append(escapeHtml(record.getFounder_first_name() != null ? record.getFounder_first_name() : "")).append(" ").append(escapeHtml(record.getFounder_last_name() != null ? record.getFounder_last_name() : "")).append("</span></div>\n");
        if (record.getFounder_telephone() != null && !record.getFounder_telephone().isEmpty()) {
            html.append("                    <div class=\"info-item\"><span class=\"info-label\">Phone:</span><span class=\"info-value\">").append(escapeHtml(record.getFounder_telephone())).append("</span></div>\n");
        }
        html.append("                </div>\n");
        html.append("            </div>\n");
        html.append("        </div>\n");
        html.append("        \n");
        html.append("        <!-- Apple Wallet Button -->\n");
        html.append("        <div class=\"wallet-section\">\n");
        html.append("            <div class=\"wallet-button-container\">\n");
        html.append("                <a href=\"/wallet/").append(record.getUid()).append("\" class=\"wallet-button\" onclick=\"addToWallet(event)\">\n");
        html.append("                    <span class=\"wallet-icon\">📱</span>\n");
        html.append("                    <span class=\"wallet-text\">Add to Apple Wallet</span>\n");
        html.append("                </a>\n");
        html.append("                <p class=\"wallet-description\">Save this record to your Apple Wallet for easy access</p>\n");
        html.append("            </div>\n");
        html.append("        </div>\n");
        html.append("        \n");
        html.append("        <div class=\"footer\">\n");
        html.append("            <p>Lost & Found Management System | Generated on ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</p>\n");
        html.append("        </div>\n");
        html.append("    </div>\n");
        html.append("    \n");
        html.append("    <!-- Image Modal -->\n");
        html.append("    <div id=\"imageModal\" class=\"image-modal\">\n");
        html.append("        <span class=\"image-modal-close\" onclick=\"closeImageModal()\">&times;</span>\n");
        html.append("        <img class=\"image-modal-content\" id=\"modalImage\">\n");
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>\n");
        
        return html.toString();
    }
    
    private String generateErrorPage(String message) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"el\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>Error - Lost & Found</title>\n");
        html.append("    <style>\n");
        html.append("        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; display: flex; align-items: center; justify-content: center; }\n");
        html.append("        .container { max-width: 500px; background: white; border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.3); padding: 40px; text-align: center; }\n");
        html.append("        h1 { color: #e74c3c; margin-bottom: 20px; }\n");
        html.append("        p { color: #666; font-size: 1.1em; line-height: 1.6; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"container\">\n");
        html.append("        <h1>❌ Error</h1>\n");
        html.append("        <p>").append(escapeHtml(message)).append("</p>\n");
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
