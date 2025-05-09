//// src/java/servlets/AdminLoginServlet.java
//
//package servlets;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.*;
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.*;
//
//@WebServlet("/AdminLoginServlet")
//public class AdminLoginServlet extends HttpServlet {
//
//    // Common method for setting CORS headers
//    
//     private void setCorsHeaders(HttpServletResponse response) {
//        // Remove trailing slash and duplicate header
//        response.setHeader("Access-Control-Allow-Origin", "https://omshri-portfolio.vercel.app");
//        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//    }
//
//    // Handle OPTIONS requests (pre-flight)
//    @Override
//    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        setCorsHeaders(response);
//        response.setStatus(HttpServletResponse.SC_OK); // Respond with status 200
//    }
//
//    // Handle POST requests for login
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        setCorsHeaders(response);
//        
//        // Handle preflight
//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
//            return;
//        }
//
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/portfolio_db", "root", "364915@Om");
//
//            String sql = "SELECT * FROM admin_users WHERE username=? AND password=?";
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, username);
//            stmt.setString(2, password);
//
//            rs = stmt.executeQuery();
//
//            PrintWriter out = response.getWriter();
//            if (rs.next()) {
//                HttpSession session = request.getSession();
//                session.setAttribute("adminUsername", username);
//                out.write("success"); // Successful login
//            } else {
//                out.write("failure"); // Invalid login
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.getWriter().write("error");  // In case of an error
//        } finally {
//            try { if (rs != null) rs.close(); } catch (Exception e) {}
//            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
//            try { if (conn != null) conn.close(); } catch (Exception e) {}
//        }
//    }
//}


package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/AdminLoginServlet")
public class AdminLoginServlet extends HttpServlet {

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "https://omshri-portfolio.vercel.app");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        
        setCorsHeaders(response);
        response.setContentType("application/json;charset=UTF-8");
        
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        PrintWriter out = response.getWriter();

        if (username == null || password == null || 
            username.isBlank() || password.isBlank()) {
            out.print("{\"status\":\"error\",\"message\":\"Missing credentials\"}");
            return;
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/portfolio_db", "root", "364915@Om")) {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            String sql = "SELECT * FROM admin_users WHERE username=? AND password=?";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username.trim());
                stmt.setString(2, password.trim());
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        HttpSession session = request.getSession();
                        session.setAttribute("adminUsername", username);
                        out.print("{\"status\":\"success\",\"message\":\"Login successful\"}");
                    } else {
                        out.print("{\"status\":\"error\",\"message\":\"Invalid credentials\"}");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"status\":\"error\",\"message\":\"Server error: " + 
                     sanitizeForJson(e.getMessage()) + "\"}");
        }
    }

    private String sanitizeForJson(String input) {
        return (input != null) ? 
            input.replace("\"", "'").replace("\\", "\\\\") : "Unknown error";
    }
}