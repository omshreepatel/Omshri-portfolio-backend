// src/main/java/com/portfolio/FetchMessagesServlet.java

package com.portfolio;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/FetchMessagesServlet")
public class FetchMessagesServlet extends HttpServlet {
  
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");  // Frontend URL
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/portfolio_db", "root", "364915@Om");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM contact_messages");

            StringBuilder json = new StringBuilder();
            json.append("[");
            
            while (rs.next()) {
                json.append("{")
                    .append("\"id\":").append(rs.getInt("id")).append(",")
                    .append("\"name\":\"").append(rs.getString("name")).append("\",")
                    .append("\"email\":\"").append(rs.getString("email")).append("\",")
                    .append("\"message\":\"").append(rs.getString("message")).append("\"")
                    .append("},");
            }
            
            if(json.charAt(json.length() - 1) == ',') {
                json.deleteCharAt(json.length() - 1);
            }
            
            json.append("]");
            
            out.print(json.toString());
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.print("[]"); // empty array if error
        }
    }
}
