/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.connectmysql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author PULKIT SHARMA
 */
public class DatabaseUtil {
    private static final String CENTRAL_DB_URL = "jdbc:mysql://localhost:3306/central_db?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String DB_USER = "springstudent";
    private static final String DB_PASSWORD = "springstudent";

    public static Connection connectToCentralDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(CENTRAL_DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to central database!");
        }
    }

    public static Connection connectToCompanyDatabase(String companyDatabase) {
        try {
            if (companyDatabase == null || companyDatabase.isEmpty()) {
                throw new IllegalArgumentException("Company database name cannot be null or empty!");
            }
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + companyDatabase + "?zeroDateTimeBehavior=CONVERT_TO_NULL",
                    DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to company database!");
        }
    }

    public static String getCompanyDatabaseName(String companyName) {
        try (Connection con = connectToCentralDatabase()) {
            String query = "SELECT database_name FROM companies WHERE name = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, companyName);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getString("database_name");
            } else {
                throw new RuntimeException("Company not found in central database!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while fetching company database name!");
        }
    }
    
}
