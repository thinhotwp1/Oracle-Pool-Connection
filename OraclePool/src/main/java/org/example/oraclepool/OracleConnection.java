package org.example.oraclepool;

import oracle.jdbc.pool.OracleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class OracleConnection implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(OracleConnection.class);

    private OracleDataSource oracleDataSource;

    public OracleConnection(String strUrl, String strUserName, String strPassword, int iMaxConnection, int iMinConnection) throws SQLException {
        oracleDataSource = createConnectionPool(strUrl, strUserName, strPassword, iMaxConnection, iMinConnection);
    }

    private OracleDataSource createConnectionPool(String strUrl, String strUserName, String strPassword, int iMaxConnection, int iMinConnection) throws SQLException {
        OracleDataSource ds = new OracleDataSource();

        Properties prop = new Properties();
        prop.setProperty("MinLimit", String.valueOf(iMinConnection));
        prop.setProperty("MaxLimit", String.valueOf(iMaxConnection));

        ds.setURL(strUrl);
        ds.setUser(strUserName);
        ds.setPassword(strPassword);
        ds.setConnectionCacheProperties(prop);
        ds.setConnectionCachingEnabled(true);
        ds.setConnectionCacheName("OraclePoolCache");

        return ds;
    }

    public Connection getConnection() throws SQLException {
        if (oracleDataSource == null) {
            throw new IllegalStateException("DataSource has not been initialized. Please call createConnectionPool() method first.");
        }
        return oracleDataSource.getConnection();
    }

    public void closeConnectionPool() throws SQLException {
        if (oracleDataSource != null) oracleDataSource.close();
    }

    @Override
    protected void finalize() throws Throwable {
        if (oracleDataSource != null) {
            oracleDataSource.close();
            oracleDataSource = null;
        }
        super.finalize();
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                log.error("Error close connection Oracle database: " + ex);
            }
        }
    }

    public boolean isOpen() {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from dual");
            resultSet.close();
            statement.close();
            closeConnection(connection);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
