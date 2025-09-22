// here we can perform connection !
package com.bankSiml.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bank_sim?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "student";

    static {
        System.out.println("Loading Driver !");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded !!");
        } catch (Exception e) {
            throw new RuntimeException("Driver loading failed", e);
        }
    }

    // method for connection
    public static Connection getConnection() throws SQLException {
        System.out.println("Connecting to MYSQL !");
        Connection conn = DriverManager.getConnection(URL, USER, PASS);
        System.out.println("Connected to MYSQL !!");
        return conn;
    }
}
