package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.net.ConnectException;



public class ClientDDBB {
    

    public static void showTables(Connection conn) throws SQLException {
        String query = "SHOW TABLES";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        System.out.println(query);
        while (resultSet.next()) {
            String tableName = resultSet.getString(1);
            System.out.println(tableName); 
            showColumns(conn, tableName); 
        }
    
        resultSet.close();
        statement.close();
    }

    // Usado debido a que el getColumnName solo me retornaba TABLE_NAME
    public static void showColumns(Connection conn, String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName + " LIMIT 1";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ResultSetMetaData metaData = resultSet.getMetaData();

        System.out.println("Columns in table " + tableName + ":");
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i + 1);
            String columnType = metaData.getColumnTypeName(i + 1);
            System.out.println("    " + columnName + " (" + columnType + ")");
        }

        resultSet.close();
        statement.close();
    }

     public boolean CreateDatabase(Connection connection, InputStream input) 
    throws IOException, ConnectException, SQLException {

        boolean dupRecord = false;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            StringBuilder sqlStatement = new StringBuilder();
            String line;

            try (Statement statement = connection.createStatement()) {
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                        
                    if (line.isEmpty() || line.startsWith("--") || line.startsWith("//") || line.startsWith("/*")) {
                            continue;
                    }

                    sqlStatement.append(line);
                    if (line.endsWith(";")) {
                        String sql = sqlStatement.toString().replace(";", "").trim();
                        statement.execute(sql);

                        sqlStatement.setLength(0);
                    }
                }
            } catch (SQLException e) {
                if (!e.getMessage().contains("Duplicate entry")) {
                    System.err.println(e.getMessage());
                } else {
                    dupRecord = true;
                    br.readLine();
                }
            }
        }
        return dupRecord;
    }
}