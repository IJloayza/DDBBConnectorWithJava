package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class GestionDDBB {
    private static Dotenv dotenv = Dotenv.load();
    
    private static final String URL = dotenv.get("db.url");
    private static final String USER = dotenv.get("db.username");
    private static final String PASSWORD = dotenv.get("db.password");
    private static Connection conn = null;

    public static void connect() throws SQLException {
        if (conn != null) return;   // is connected
        conn = DriverManager.getConnection(URL , USER , PASSWORD);
    }

    public static void connect(String url, String username, String pass) throws SQLException {
        if (conn != null) return;   // is connected
        conn = DriverManager.getConnection(url , username , pass);
    }

    public static void disconnect() throws SQLException {
        if (conn == null) return; // disconnected
        conn.close();
        conn = null;
    }

    public static void main(String[] args) {
        boolean still = true;
        while (still) {
            System.out.println("Introduce the name of the url");
            String url = Std.readLine();
            System.out.println("Introduce the username");
            String username = Std.readLine();
            System.out.println("Introduce the password for the database");
            String pass = Std.readLine();
            try {
                GestionDDBB.connect(url, username, pass);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
