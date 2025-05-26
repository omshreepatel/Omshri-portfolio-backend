package com.portfolio;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ContactServlet")
public class ContactServlet extends HttpServlet {
    
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

//    private void setCorsHeaders(HttpServletResponse response) {
//        // Remove trailing slash from origin
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
//        response.setHeader("Access-Control-Max-Age", "3600");
//    }
    private void setCorsHeaders(HttpServletResponse response) {
    response.setHeader("Access-Control-Allow-Origin", "*"); // Allow frontend origin
    response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    response.setHeader("Access-Control-Allow-Credentials", "true"); // Important for authentication-based requests
}


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
          setCorsHeaders(response);
        
        // Handle preflight request first
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        
        
        // Set response content type
        response.setContentType("application/json;charset=UTF-8");
        
        // Read form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");
        
        PrintWriter out = response.getWriter();
        
        try {
            // JDBC Connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/portfolio_db", "root", "364915@Om");

            // Insert Query
            String sql = "INSERT INTO contact_messages (name, email, message) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, message);
            
            int rows = ps.executeUpdate();
            
            if (rows > 0) {
                // Success response
                out.print("{\"status\":\"success\",\"message\":\"Message received successfully!\"}");
            } else {
                // Failure response
                out.print("{\"status\":\"error\",\"message\":\"Failed to send message.\"}");
            }
            
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}

//package com.portfolio;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@WebServlet("/ContactServlet")
//public class ContactServlet extends HttpServlet {
//    
//    @Override
//    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
//        throws ServletException, IOException {
//        setCorsHeaders(response);
//        response.setStatus(HttpServletResponse.SC_OK);
//    }
//
//    private void setCorsHeaders(HttpServletResponse response) {
//        response.setHeader("Access-Control-Allow-Origin", "https://omshri-portfolio.vercel.app");
//        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        setCorsHeaders(response);
//        
//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
//            return;
//        }
//        
//        response.setContentType("application/json;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        
//        try {
//            // Validate input parameters
//            String name = validateInput(request.getParameter("name"), "Name");
//            String email = validateInput(request.getParameter("email"), "Email");
//            String message = validateInput(request.getParameter("message"), "Message");
//
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            try (Connection con = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/portfolio_db", "root", "364915@Om")) {
//                
//                String sql = "INSERT INTO contact_messages (name, email, message) VALUES (?, ?, ?)";
//                try (PreparedStatement ps = con.prepareStatement(sql)) {
//                    ps.setString(1, name);
//                    ps.setString(2, email);
//                    ps.setString(3, message);
//                    
//                    int rows = ps.executeUpdate();
//                    if (rows > 0) {
//                        out.print("{\"status\":\"success\",\"message\":\"Message received successfully!\"}");
//                    } else {
//                        out.print("{\"status\":\"error\",\"message\":\"Failed to save message\"}");
//                    }
//                }
//            }
//        } catch (IllegalArgumentException e) {
//            out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
//        } catch (Exception e) {
//            e.printStackTrace();
//            out.print("{\"status\":\"error\",\"message\":\"Server error: " + 
//                     sanitizeForJson(e.getMessage()) + "\"}");
//        }
//    }
//
//    private String validateInput(String input, String fieldName) {
//        if (input == null || input.trim().isEmpty()) {
//            throw new IllegalArgumentException(fieldName + " cannot be empty");
//        }
//        return input.trim();
//    }
//
//    private String sanitizeForJson(String input) {
//        return input.replace("\"", "'").replace("\\", "\\\\");
//    }
//}