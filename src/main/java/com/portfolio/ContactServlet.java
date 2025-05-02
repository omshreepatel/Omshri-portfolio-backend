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
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        
//        // 👉 Add CORS headers
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
//        
//        // Set response content type
//        response.setContentType("application/json;charset=UTF-8");
//        
//        // Read form data
//        String name = request.getParameter("name");
//        String email = request.getParameter("email");
//        String message = request.getParameter("message");
//        
//        PrintWriter out = response.getWriter();
//        
//        try {
//            // JDBC Connection
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection con = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/portfolio_db", "root", "364915@Om");
//
//            // Insert Query
//            String sql = "INSERT INTO contact_messages (name, email, message) VALUES (?, ?, ?)";
//            PreparedStatement ps = con.prepareStatement(sql);
//            
//            ps.setString(1, name);
//            ps.setString(2, email);
//            ps.setString(3, message);
//            
//            int rows = ps.executeUpdate();
//            
//            if (rows > 0) {
//                // Success response
//                out.print("{\"status\":\"success\",\"message\":\"Message received successfully!\"}");
//            } else {
//                // Failure response
//                out.print("{\"status\":\"error\",\"message\":\"Failed to send message.\"}");
//            }
//            
//            con.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
//        }
//    }
//}

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 👉 Add CORS headers
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        
        // Set response content type
        response.setContentType("application/json;charset=UTF-8");
        
        // Read form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");
        
        PrintWriter out = response.getWriter();
        
        // Validate input
        if (name == null || email == null || message == null || 
            name.isEmpty() || email.isEmpty() || message.isEmpty()) {
            out.print("{\"status\":\"error\",\"message\":\"All fields are required.\"}");
            return;
        }
        
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

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle preflight CORS requests
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}