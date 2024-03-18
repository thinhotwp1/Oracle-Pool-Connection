package org.example.oraclepool;

import lombok.Data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Data
public class ConnectionFactory implements Serializable {
    public String url;
    public String user;
    public String password;
    public int maxConnection;
    public int minConnection;
    public OracleConnection mOraclePoolClient;

    public ConnectionFactory() {
    }

    public Connection getConnection() throws Exception {
        if (mOraclePoolClient == null) {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            mOraclePoolClient = new OracleConnection(url, user, password, maxConnection, minConnection);
        }
        return mOraclePoolClient.getConnection();
    }

    public void closePool() throws SQLException {
        mOraclePoolClient.closeConnectionPool();
    }

    public void closeConnection(Connection connection) throws SQLException {
        mOraclePoolClient.closeConnection(connection);
    }

    public boolean isOpen() {
        return mOraclePoolClient.isOpen();
    }
}
