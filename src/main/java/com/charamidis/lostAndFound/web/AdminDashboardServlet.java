package com.charamidis.lostAndFound.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminDashboardServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Basic authentication check
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            response.setHeader("WWW-Authenticate", "Basic realm=\"Admin Dashboard\"");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        // Decode and verify credentials
        String encodedCredentials = authHeader.substring(6);
        String credentials = new String(java.util.Base64.getDecoder().decode(encodedCredentials));
        String[] parts = credentials.split(":", 2);
        
        if (parts.length != 2 || !isValidCredentials(parts[0], parts[1])) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Lost & Found - Admin Dashboard</title>");
        out.println("    <style>");
        out.println(getCSS());
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <header class='header'>");
        out.println("            <h1>🔍 Lost & Found Admin Dashboard</h1>");
        out.println("            <div class='status-indicator' id='statusIndicator'>");
        out.println("                <span class='status-dot'></span>");
        out.println("                <span id='statusText'>Connected</span>");
        out.println("            </div>");
        out.println("        </header>");
        out.println("        ");
        out.println("        <div class='tabs'>");
        out.println("            <button class='tab-btn active' onclick='showTab(\"overview\")'>📊 Overview</button>");
        out.println("            <button class='tab-btn' onclick='showTab(\"records\")'>📝 Records</button>");
        out.println("            <button class='tab-btn' onclick='showTab(\"returns\")'>↩️ Returns</button>");
        out.println("            <button class='tab-btn' onclick='showTab(\"activity\")'>📋 Activity Log</button>");
        out.println("        </div>");
        out.println("        ");
        out.println("        <div id='overview' class='tab-content active'>");
        out.println("            <div class='dashboard-grid'>");
        out.println("                <div class='card'>");
        out.println("                    <h3>📊 Live Activity</h3>");
        out.println("                    <div id='activityFeed' class='activity-feed'></div>");
        out.println("                </div>");
        out.println("                <div class='card'>");
        out.println("                    <h3>📝 Recent Records</h3>");
        out.println("                    <div id='recentRecords' class='records-list'></div>");
        out.println("                </div>");
        out.println("                <div class='card'>");
        out.println("                    <h3>👥 User Activity</h3>");
        out.println("                    <div id='userActivity' class='user-activity'></div>");
        out.println("                </div>");
        out.println("                <div class='card'>");
        out.println("                    <h3>⚠️ System Alerts</h3>");
        out.println("                    <div id='systemAlerts' class='alerts-list'></div>");
        out.println("                </div>");
        out.println("            </div>");
        out.println("        </div>");
        out.println("        ");
        out.println("        <div id='records' class='tab-content'>");
        out.println("            <div class='search-container'>");
        out.println("                <input type='text' id='recordsSearch' placeholder='🔍 Search records...' class='search-input'>");
        out.println("                <button onclick='searchRecords()' class='btn btn-primary'>Search</button>");
        out.println("                <button onclick='clearRecordsSearch()' class='btn btn-secondary'>Clear</button>");
        out.println("            </div>");
        out.println("            <div class='table-container'>");
        out.println("                <table id='recordsTable' class='data-table'>");
        out.println("                    <thead>");
        out.println("                        <tr>");
        out.println("                            <th>ID</th>");
        out.println("                            <th>Date</th>");
        out.println("                            <th>Founder</th>");
        out.println("                            <th>Item Description</th>");
        out.println("                            <th>Location</th>");
        out.println("                            <th>Status</th>");
        out.println("                        </tr>");
        out.println("                    </thead>");
        out.println("                    <tbody id='recordsTableBody'>");
        out.println("                    </tbody>");
        out.println("                </table>");
        out.println("            </div>");
        out.println("        </div>");
        out.println("        ");
        out.println("        <div id='returns' class='tab-content'>");
        out.println("            <div class='search-container'>");
        out.println("                <input type='text' id='returnsSearch' placeholder='🔍 Search returns...' class='search-input'>");
        out.println("                <button onclick='searchReturns()' class='btn btn-primary'>Search</button>");
        out.println("                <button onclick='clearReturnsSearch()' class='btn btn-secondary'>Clear</button>");
        out.println("            </div>");
        out.println("            <div class='table-container'>");
        out.println("                <table id='returnsTable' class='data-table'>");
        out.println("                    <thead>");
        out.println("                        <tr>");
        out.println("                            <th>ID</th>");
        out.println("                            <th>Return Date</th>");
        out.println("                            <th>Claimant</th>");
        out.println("                            <th>Item Description</th>");
        out.println("                            <th>Officer</th>");
        out.println("                            <th>Status</th>");
        out.println("                        </tr>");
        out.println("                    </thead>");
        out.println("                    <tbody id='returnsTableBody'>");
        out.println("                    </tbody>");
        out.println("                </table>");
        out.println("            </div>");
        out.println("        </div>");
        out.println("        ");
        out.println("        <div id='activity' class='tab-content'>");
        out.println("            <div class='search-container'>");
        out.println("                <input type='text' id='activitySearch' placeholder='🔍 Search activity...' class='search-input'>");
        out.println("                <button onclick='searchActivity()' class='btn btn-primary'>Search</button>");
        out.println("                <button onclick='clearActivitySearch()' class='btn btn-secondary'>Clear</button>");
        out.println("            </div>");
        out.println("            <div class='table-container'>");
        out.println("                <table id='activityTable' class='data-table'>");
        out.println("                    <thead>");
        out.println("                        <tr>");
        out.println("                            <th>Timestamp</th>");
        out.println("                            <th>User</th>");
        out.println("                            <th>Action</th>");
        out.println("                            <th>Details</th>");
        out.println("                            <th>IP Address</th>");
        out.println("                        </tr>");
        out.println("                    </thead>");
        out.println("                    <tbody id='activityTableBody'>");
        out.println("                    </tbody>");
        out.println("                </table>");
        out.println("            </div>");
        out.println("        </div>");
        out.println("        ");
        out.println("        <div class='controls'>");
        out.println("            <button onclick='refreshData()' class='btn btn-primary'>🔄 Refresh</button>");
        out.println("            <button onclick='clearLogs()' class='btn btn-secondary'>🗑️ Clear Logs</button>");
        out.println("            <button onclick='exportLogs()' class='btn btn-success'>📥 Export Logs</button>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("    ");
        out.println("    <script>");
        out.println(getJavaScript());
        out.println("    </script>");
        out.println("</body>");
        out.println("</html>");
    }
    
    private String getCSS() {
        return "* {\n" +
            "    margin: 0;\n" +
            "    padding: 0;\n" +
            "    box-sizing: border-box;\n" +
            "}\n" +
            "body {\n" +
            "    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
            "    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
            "    min-height: 100vh;\n" +
            "    color: #333;\n" +
            "}\n" +
            ".container {\n" +
            "    max-width: 1400px;\n" +
            "    margin: 0 auto;\n" +
            "    padding: 20px;\n" +
            "}\n" +
            ".header {\n" +
            "    background: rgba(255, 255, 255, 0.95);\n" +
            "    padding: 20px;\n" +
            "    border-radius: 15px;\n" +
            "    margin-bottom: 20px;\n" +
            "    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);\n" +
            "    display: flex;\n" +
            "    justify-content: space-between;\n" +
            "    align-items: center;\n" +
            "    flex-wrap: wrap;\n" +
            "}\n" +
            ".header h1 {\n" +
            "    color: #2c3e50;\n" +
            "    font-size: 2rem;\n" +
            "    font-weight: 700;\n" +
            "}\n" +
            ".status-indicator {\n" +
            "    display: flex;\n" +
            "    align-items: center;\n" +
            "    gap: 10px;\n" +
            "    padding: 10px 20px;\n" +
            "    background: #f8f9fa;\n" +
            "    border-radius: 25px;\n" +
            "    border: 2px solid #e9ecef;\n" +
            "}\n" +
            ".status-dot {\n" +
            "    width: 12px;\n" +
            "    height: 12px;\n" +
            "    border-radius: 50%;\n" +
            "    background: #28a745;\n" +
            "    animation: pulse 2s infinite;\n" +
            "}\n" +
            "@keyframes pulse {\n" +
            "    0% { opacity: 1; }\n" +
            "    50% { opacity: 0.5; }\n" +
            "    100% { opacity: 1; }\n" +
            "}\n" +
            ".tabs {\n" +
            "    display: flex;\n" +
            "    gap: 10px;\n" +
            "    margin-bottom: 20px;\n" +
            "    background: rgba(255, 255, 255, 0.1);\n" +
            "    padding: 10px;\n" +
            "    border-radius: 10px;\n" +
            "    backdrop-filter: blur(10px);\n" +
            "}\n" +
            ".tab-btn {\n" +
            "    padding: 12px 24px;\n" +
            "    border: none;\n" +
            "    background: transparent;\n" +
            "    color: white;\n" +
            "    border-radius: 8px;\n" +
            "    cursor: pointer;\n" +
            "    font-weight: 600;\n" +
            "    transition: all 0.3s ease;\n" +
            "}\n" +
            ".tab-btn:hover {\n" +
            "    background: rgba(255, 255, 255, 0.2);\n" +
            "}\n" +
            ".tab-btn.active {\n" +
            "    background: rgba(255, 255, 255, 0.3);\n" +
            "    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);\n" +
            "}\n" +
            ".tab-content {\n" +
            "    display: none;\n" +
            "}\n" +
            ".tab-content.active {\n" +
            "    display: block;\n" +
            "}\n" +
            ".dashboard-grid {\n" +
            "    display: grid;\n" +
            "    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));\n" +
            "    gap: 20px;\n" +
            "    margin-bottom: 20px;\n" +
            "}\n" +
            ".card {\n" +
            "    background: rgba(255, 255, 255, 0.95);\n" +
            "    padding: 20px;\n" +
            "    border-radius: 15px;\n" +
            "    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);\n" +
            "    backdrop-filter: blur(10px);\n" +
            "}\n" +
            ".card h3 {\n" +
            "    color: #2c3e50;\n" +
            "    margin-bottom: 15px;\n" +
            "    font-size: 1.2rem;\n" +
            "}\n" +
            ".search-container {\n" +
            "    background: rgba(255, 255, 255, 0.95);\n" +
            "    padding: 20px;\n" +
            "    border-radius: 15px;\n" +
            "    margin-bottom: 20px;\n" +
            "    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);\n" +
            "    display: flex;\n" +
            "    gap: 10px;\n" +
            "    align-items: center;\n" +
            "    flex-wrap: wrap;\n" +
            "}\n" +
            ".search-input {\n" +
            "    flex: 1;\n" +
            "    padding: 12px 16px;\n" +
            "    border: 2px solid #e9ecef;\n" +
            "    border-radius: 8px;\n" +
            "    font-size: 16px;\n" +
            "    min-width: 300px;\n" +
            "}\n" +
            ".search-input:focus {\n" +
            "    outline: none;\n" +
            "    border-color: #007bff;\n" +
            "    box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.25);\n" +
            "}\n" +
            ".table-container {\n" +
            "    background: rgba(255, 255, 255, 0.95);\n" +
            "    border-radius: 15px;\n" +
            "    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);\n" +
            "    overflow: hidden;\n" +
            "    margin-bottom: 20px;\n" +
            "}\n" +
            ".data-table {\n" +
            "    width: 100%;\n" +
            "    border-collapse: collapse;\n" +
            "}\n" +
            ".data-table th {\n" +
            "    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
            "    color: white;\n" +
            "    padding: 15px 12px;\n" +
            "    text-align: left;\n" +
            "    font-weight: 600;\n" +
            "    font-size: 14px;\n" +
            "}\n" +
            ".data-table td {\n" +
            "    padding: 12px;\n" +
            "    border-bottom: 1px solid #e9ecef;\n" +
            "    font-size: 14px;\n" +
            "}\n" +
            ".data-table tbody tr:hover {\n" +
            "    background-color: #f8f9fa;\n" +
            "}\n" +
            ".data-table tbody tr:nth-child(even) {\n" +
            "    background-color: #f8f9fa;\n" +
            "}\n" +
            ".status-badge {\n" +
            "    padding: 4px 8px;\n" +
            "    border-radius: 12px;\n" +
            "    font-size: 12px;\n" +
            "    font-weight: 600;\n" +
            "    text-transform: uppercase;\n" +
            "}\n" +
            ".status-active {\n" +
            "    background: #d4edda;\n" +
            "    color: #155724;\n" +
            "}\n" +
            ".status-returned {\n" +
            "    background: #cce5ff;\n" +
            "    color: #004085;\n" +
            "}\n" +
            ".status-pending {\n" +
            "    background: #fff3cd;\n" +
            "    color: #856404;\n" +
            "}\n" +
            ".activity-item {\n" +
            "    padding: 10px;\n" +
            "    border-left: 3px solid #007bff;\n" +
            "    margin-bottom: 10px;\n" +
            "    background: #f8f9fa;\n" +
            "    border-radius: 0 8px 8px 0;\n" +
            "}\n" +
            ".activity-time {\n" +
            "    font-size: 12px;\n" +
            "    color: #6c757d;\n" +
            "}\n" +
            ".activity-action {\n" +
            "    font-weight: 600;\n" +
            "    color: #2c3e50;\n" +
            "}\n" +
            ".controls {\n" +
            "    display: flex;\n" +
            "    gap: 10px;\n" +
            "    justify-content: center;\n" +
            "    flex-wrap: wrap;\n" +
            "}\n" +
            ".btn {\n" +
            "    padding: 12px 24px;\n" +
            "    border: none;\n" +
            "    border-radius: 8px;\n" +
            "    cursor: pointer;\n" +
            "    font-weight: 600;\n" +
            "    transition: all 0.3s ease;\n" +
            "    text-decoration: none;\n" +
            "    display: inline-block;\n" +
            "}\n" +
            ".btn-primary {\n" +
            "    background: linear-gradient(135deg, #007bff, #0056b3);\n" +
            "    color: white;\n" +
            "}\n" +
            ".btn-primary:hover {\n" +
            "    transform: translateY(-2px);\n" +
            "    box-shadow: 0 4px 15px rgba(0, 123, 255, 0.4);\n" +
            "}\n" +
            ".btn-secondary {\n" +
            "    background: linear-gradient(135deg, #6c757d, #545b62);\n" +
            "    color: white;\n" +
            "}\n" +
            ".btn-secondary:hover {\n" +
            "    transform: translateY(-2px);\n" +
            "    box-shadow: 0 4px 15px rgba(108, 117, 125, 0.4);\n" +
            "}\n" +
            ".btn-success {\n" +
            "    background: linear-gradient(135deg, #28a745, #1e7e34);\n" +
            "    color: white;\n" +
            "}\n" +
            ".btn-success:hover {\n" +
            "    transform: translateY(-2px);\n" +
            "    box-shadow: 0 4px 15px rgba(40, 167, 69, 0.4);\n" +
            "}\n" +
            "@media (max-width: 768px) {\n" +
            "    .container {\n" +
            "        padding: 10px;\n" +
            "    }\n" +
            "    .header {\n" +
            "        flex-direction: column;\n" +
            "        gap: 15px;\n" +
            "    }\n" +
            "    .tabs {\n" +
            "        flex-wrap: wrap;\n" +
            "    }\n" +
            "    .search-container {\n" +
            "        flex-direction: column;\n" +
            "    }\n" +
            "    .search-input {\n" +
            "        min-width: 100%;\n" +
            "    }\n" +
            "    .data-table {\n" +
            "        font-size: 12px;\n" +
            "    }\n" +
            "    .data-table th,\n" +
            "    .data-table td {\n" +
            "        padding: 8px 6px;\n" +
            "    }\n" +
            "}\n";
    }
    
    private String getJavaScript() {
        return "let currentTab = 'overview';\n" +
            "let activityLog = [];\n" +
            "let recordsData = [];\n" +
            "let returnsData = [];\n" +
            "\n" +
            "function showTab(tabName) {\n" +
            "    // Hide all tab contents\n" +
            "    document.querySelectorAll('.tab-content').forEach(tab => {\n" +
            "        tab.classList.remove('active');\n" +
            "    });\n" +
            "    \n" +
            "    // Remove active class from all tab buttons\n" +
            "    document.querySelectorAll('.tab-btn').forEach(btn => {\n" +
            "        btn.classList.remove('active');\n" +
            "    });\n" +
            "    \n" +
            "    // Show selected tab content\n" +
            "    document.getElementById(tabName).classList.add('active');\n" +
            "    \n" +
            "    // Add active class to clicked button\n" +
            "    event.target.classList.add('active');\n" +
            "    \n" +
            "    currentTab = tabName;\n" +
            "    \n" +
            "    // Load data for the selected tab\n" +
            "    if (tabName === 'records') {\n" +
            "        loadRecords();\n" +
            "    } else if (tabName === 'returns') {\n" +
            "        loadReturns();\n" +
            "    } else if (tabName === 'activity') {\n" +
            "        loadActivity();\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "function loadRecords() {\n" +
            "    fetch('/api/records')\n" +
            "        .then(response => response.json())\n" +
            "        .then(data => {\n" +
            "            recordsData = data;\n" +
            "            displayRecords(data);\n" +
            "        })\n" +
            "        .catch(error => {\n" +
            "            console.error('Error loading records:', error);\n" +
            "            document.getElementById('recordsTableBody').innerHTML = '<tr><td colspan=\"6\">Error loading records</td></tr>';\n" +
            "        });\n" +
            "}\n" +
            "\n" +
            "function loadReturns() {\n" +
            "    fetch('/api/returns')\n" +
            "        .then(response => response.json())\n" +
            "        .then(data => {\n" +
            "            returnsData = data;\n" +
            "            displayReturns(data);\n" +
            "        })\n" +
            "        .catch(error => {\n" +
            "            console.error('Error loading returns:', error);\n" +
            "            document.getElementById('returnsTableBody').innerHTML = '<tr><td colspan=\"6\">Error loading returns</td></tr>';\n" +
            "        });\n" +
            "}\n" +
            "\n" +
            "function loadActivity() {\n" +
            "    fetch('/api/activity')\n" +
            "        .then(response => response.json())\n" +
            "        .then(data => {\n" +
            "            activityLog = data;\n" +
            "            displayActivity(data);\n" +
            "        })\n" +
            "        .catch(error => {\n" +
            "            console.error('Error loading activity:', error);\n" +
            "            document.getElementById('activityTableBody').innerHTML = '<tr><td colspan=\"5\">Error loading activity log</td></tr>';\n" +
            "        });\n" +
            "}\n" +
            "\n" +
            "function displayRecords(records) {\n" +
            "    const tbody = document.getElementById('recordsTableBody');\n" +
            "    tbody.innerHTML = '';\n" +
            "    \n" +
            "    records.forEach(record => {\n" +
            "        const row = document.createElement('tr');\n" +
            "        row.innerHTML = `\n" +
            "            <td>${record.id}</td>\n" +
            "            <td>${record.record_date}</td>\n" +
            "            <td>${record.founder_first_name} ${record.founder_last_name}</td>\n" +
            "            <td>${record.item_description}</td>\n" +
            "            <td>${record.found_location}</td>\n" +
            "            <td><span class=\"status-badge status-active\">Active</span></td>\n" +
            "        `;\n" +
            "        tbody.appendChild(row);\n" +
            "    });\n" +
            "}\n" +
            "\n" +
            "function displayReturns(returns) {\n" +
            "    const tbody = document.getElementById('returnsTableBody');\n" +
            "    tbody.innerHTML = '';\n" +
            "    \n" +
            "    returns.forEach(returnItem => {\n" +
            "        const row = document.createElement('tr');\n" +
            "        row.innerHTML = `\n" +
            "            <td>${returnItem.id}</td>\n" +
            "            <td>${returnItem.return_date}</td>\n" +
            "            <td>${returnItem.claimant_name}</td>\n" +
            "            <td>${returnItem.item_description}</td>\n" +
            "            <td>${returnItem.officer_name}</td>\n" +
            "            <td><span class=\"status-badge status-returned\">Returned</span></td>\n" +
            "        `;\n" +
            "        tbody.appendChild(row);\n" +
            "    });\n" +
            "}\n" +
            "\n" +
            "function displayActivity(activities) {\n" +
            "    const tbody = document.getElementById('activityTableBody');\n" +
            "    tbody.innerHTML = '';\n" +
            "    \n" +
            "    activities.forEach(activity => {\n" +
            "        const row = document.createElement('tr');\n" +
            "        row.innerHTML = `\n" +
            "            <td>${activity.timestamp}</td>\n" +
            "            <td>${activity.user}</td>\n" +
            "            <td>${activity.action}</td>\n" +
            "            <td>${activity.details}</td>\n" +
            "            <td>${activity.ip_address || 'N/A'}</td>\n" +
            "        `;\n" +
            "        tbody.appendChild(row);\n" +
            "    });\n" +
            "}\n" +
            "\n" +
            "function searchRecords() {\n" +
            "    const searchTerm = document.getElementById('recordsSearch').value.toLowerCase();\n" +
            "    const filtered = recordsData.filter(record => \n" +
            "        record.founder_first_name.toLowerCase().includes(searchTerm) ||\n" +
            "        record.founder_last_name.toLowerCase().includes(searchTerm) ||\n" +
            "        record.item_description.toLowerCase().includes(searchTerm) ||\n" +
            "        record.found_location.toLowerCase().includes(searchTerm)\n" +
            "    );\n" +
            "    displayRecords(filtered);\n" +
            "}\n" +
            "\n" +
            "function searchReturns() {\n" +
            "    const searchTerm = document.getElementById('returnsSearch').value.toLowerCase();\n" +
            "    const filtered = returnsData.filter(returnItem => \n" +
            "        returnItem.claimant_name.toLowerCase().includes(searchTerm) ||\n" +
            "        returnItem.item_description.toLowerCase().includes(searchTerm) ||\n" +
            "        returnItem.officer_name.toLowerCase().includes(searchTerm)\n" +
            "    );\n" +
            "    displayReturns(filtered);\n" +
            "}\n" +
            "\n" +
            "function searchActivity() {\n" +
            "    const searchTerm = document.getElementById('activitySearch').value.toLowerCase();\n" +
            "    const filtered = activityLog.filter(activity => \n" +
            "        activity.user.toLowerCase().includes(searchTerm) ||\n" +
            "        activity.action.toLowerCase().includes(searchTerm) ||\n" +
            "        activity.details.toLowerCase().includes(searchTerm)\n" +
            "    );\n" +
            "    displayActivity(filtered);\n" +
            "}\n" +
            "\n" +
            "function clearRecordsSearch() {\n" +
            "    document.getElementById('recordsSearch').value = '';\n" +
            "    displayRecords(recordsData);\n" +
            "}\n" +
            "\n" +
            "function clearReturnsSearch() {\n" +
            "    document.getElementById('returnsSearch').value = '';\n" +
            "    displayReturns(returnsData);\n" +
            "}\n" +
            "\n" +
            "function clearActivitySearch() {\n" +
            "    document.getElementById('activitySearch').value = '';\n" +
            "    displayActivity(activityLog);\n" +
            "}\n" +
            "\n" +
            "function refreshData() {\n" +
            "    if (currentTab === 'records') {\n" +
            "        loadRecords();\n" +
            "    } else if (currentTab === 'returns') {\n" +
            "        loadReturns();\n" +
            "    } else if (currentTab === 'activity') {\n" +
            "        loadActivity();\n" +
            "    } else {\n" +
            "        loadOverviewData();\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "function loadOverviewData() {\n" +
            "    // Load overview data\n" +
            "    fetch('/api/stats')\n" +
            "        .then(response => response.json())\n" +
            "        .then(data => {\n" +
            "            document.getElementById('recentRecords').innerHTML = `\n" +
            "                <div class=\"activity-item\">\n" +
            "                    <div class=\"activity-time\">Total Records: ${data.totalRecords}</div>\n" +
            "                    <div class=\"activity-action\">Records in System</div>\n" +
            "                </div>\n" +
            "                <div class=\"activity-item\">\n" +
            "                    <div class=\"activity-time\">This Month: ${data.recordsThisMonth}</div>\n" +
            "                    <div class=\"activity-action\">New Records</div>\n" +
            "                </div>\n" +
            "            `;\n" +
            "        })\n" +
            "        .catch(error => console.error('Error loading stats:', error));\n" +
            "    \n" +
            "    // Load recent activity\n" +
            "    fetch('/api/recent-activity')\n" +
            "        .then(response => response.json())\n" +
            "        .then(data => {\n" +
            "            const activityFeed = document.getElementById('activityFeed');\n" +
            "            activityFeed.innerHTML = data.map(activity => `\n" +
            "                <div class=\"activity-item\">\n" +
            "                    <div class=\"activity-time\">${activity.timestamp}</div>\n" +
            "                    <div class=\"activity-action\">${activity.action}</div>\n" +
            "                    <div>${activity.details}</div>\n" +
            "                </div>\n" +
            "            `).join('');\n" +
            "        })\n" +
            "        .catch(error => console.error('Error loading activity:', error));\n" +
            "}\n" +
            "\n" +
            "function clearLogs() {\n" +
            "    if (confirm('Are you sure you want to clear all logs?')) {\n" +
            "        fetch('/api/clear-logs', { method: 'POST' })\n" +
            "            .then(() => {\n" +
            "                alert('Logs cleared successfully');\n" +
            "                refreshData();\n" +
            "            })\n" +
            "            .catch(error => {\n" +
            "                console.error('Error clearing logs:', error);\n" +
            "                alert('Error clearing logs');\n" +
            "            });\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "function exportLogs() {\n" +
            "    window.open('/api/export-logs', '_blank');\n" +
            "}\n" +
            "\n" +
            "// Auto-refresh every 30 seconds\n" +
            "setInterval(() => {\n" +
            "    if (currentTab === 'overview') {\n" +
            "        loadOverviewData();\n" +
            "    }\n" +
            "}, 30000);\n" +
            "\n" +
            "// Initialize on page load\n" +
            "document.addEventListener('DOMContentLoaded', function() {\n" +
            "    loadOverviewData();\n" +
            "    \n" +
            "    // Add search functionality to input fields\n" +
            "    document.getElementById('recordsSearch').addEventListener('input', searchRecords);\n" +
            "    document.getElementById('returnsSearch').addEventListener('input', searchReturns);\n" +
            "    document.getElementById('activitySearch').addEventListener('input', searchActivity);\n" +
            "});\n";
    }
    
    private boolean isValidCredentials(String username, String password) {
        return "admin".equals(username) && WebServerManager.getAdminPassword().equals(password);
    }
}