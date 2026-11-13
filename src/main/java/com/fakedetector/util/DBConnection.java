package com.fakedetector.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DBConnection {
    public static final Object LOCK = new Object();
    private static DBConnection instance;
    private Connection connection;

    private String url;
    private String user;
    private String password;

    private DBConnection() {
        try (InputStream in = DBConnection.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found in src/main/resources");
            }
            Properties props = new Properties();
            props.load(in);
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
        } catch (IOException e) {
            throw new IllegalStateException("Could not read config.properties", e);
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }


    public Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed() || !connection.isValid(2)) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL driver not found", e);
        }
        connection = DriverManager.getConnection(url, user, password);
    }
    return connection;
}

}
    

