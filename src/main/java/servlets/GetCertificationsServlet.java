// src/java/servlets/GetCertificationsServlet.java

package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/GetCertificationsServlet")
public class GetCertificationsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/portfolio_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "364915@Om";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CORS Headers
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // Fetch certifications data
            String sql = "SELECT * FROM certifications ORDER BY issue_date DESC"; // latest certifications first
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            StringBuilder json = new StringBuilder();
            json.append("[");

            while (rs.next()) {
                json.append("{");
                json.append("\"id\":").append(rs.getInt("id")).append(",");
                json.append("\"name\":\"").append(escapeJson(rs.getString("name"))).append("\",");
                json.append("\"organization\":\"").append(escapeJson(rs.getString("organization"))).append("\",");
                json.append("\"issue_date\":\"").append(rs.getDate("issue_date").toString()).append("\"");
                json.append("},");
            }

            // Remove last comma
            if (json.length() > 1 && json.charAt(json.length() - 1) == ',') {
                json.deleteCharAt(json.length() - 1);
            }
            json.append("]");

            out.print(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            out.print("[]"); // Send empty JSON array on error
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    // Helper method to escape JSON special characters
    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
