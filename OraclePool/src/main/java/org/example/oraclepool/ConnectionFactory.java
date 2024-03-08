package org.example.oraclepool;

import lombok.Data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Data
public class ConnectionFactory implements Serializable {
    public static String url;
    public static String user;
    public static String password;
    public static int maxConnection;
    public static int minConnection;
    public static OracleConnection mOraclePoolClient;

    public ConnectionFactory() {
    }

    public static Connection getConnection() throws Exception {
        if (mOraclePoolClient == null) {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            mOraclePoolClient = new OracleConnection(url, user, password, maxConnection, minConnection);
        }
        return mOraclePoolClient.getConnection();
    }

    public static void closePool() throws SQLException {
        mOraclePoolClient.closeConnectionPool();
    }

    public static void closeConnection(Connection connection) throws SQLException {
        mOraclePoolClient.closeConnection(connection);
    }

    public static boolean isOpen() {
        return mOraclePoolClient.isOpen();
    }
}
