package data;

import data.interfaceces.IDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDB implements IDB {
    private static PostgresDB instance;  // Singleton instance
    private String host;
    private String username;
    private String password;
    private String dbName;
    private Connection connection;

    // Private constructor to prevent direct instantiation
    private PostgresDB(String host, String username, String password, String dbName) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.dbName = dbName;
    }

    // Singleton method to get the instance
    public static PostgresDB getInstance(String host, String username, String password, String dbName) {
        if (instance == null) {
            synchronized (PostgresDB.class) {
                if (instance == null) {
                    instance = new PostgresDB(host, username, password, dbName);
                }
            }
        }
        return instance;
    }

    @Override
    public Connection getConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    return connection;  // Reuse existing connection
                }
            } catch (SQLException e) {
                System.err.println("Error checking connection status: " + e.getMessage());
            }
        }

        String connectionUrl = host + "/" + dbName;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(connectionUrl, username, password);
        } catch (Exception e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
        }
        return connection;
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Failed to close connection: " + e.getMessage());
            }
        }
    }
}
