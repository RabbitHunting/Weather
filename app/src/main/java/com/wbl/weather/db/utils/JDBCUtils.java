package com.wbl.weather.db.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtils {
    static{

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://120.24.77.165:3306/weather","weather","123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
