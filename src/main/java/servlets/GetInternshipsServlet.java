// src/java/servlets/GetInternshipsServlet.java

package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/GetInternshipsServlet")
public class GetInternshipsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
         response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");  // Frontend URL
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/portfolio_db", "root", "364915@Om");

            String sql = "SELECT * FROM internships"; // Assuming table name is 'internships'
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            StringBuilder json = new StringBuilder();
            json.append("[");

            while (rs.next()) {
                json.append("{");
                json.append("\"id\":").append(rs.getInt("id")).append(",");
                json.append("\"company\":\"").append(rs.getString("company")).append("\",");
                json.append("\"role\":\"").append(rs.getString("role")).append("\",");
                json.append("\"duration\":\"").append(rs.getString("duration")).append("\"");
                json.append("},");
            }
            if (json.charAt(json.length() - 1) == ',') {
                json.deleteCharAt(json.length() - 1);
            }
            json.append("]");

            out.print(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            out.print("[]");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
}
