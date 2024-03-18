package org.example.oraclepool;

import oracle.jdbc.OracleTypes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//@SpringBootApplication
public class OraclePoolApplication {
    private Connection connection;
    private static ConnectionFactory connectionFactory;

    // TODO: Main for test
    public static void main(String[] args) throws Exception {
        try {
            // Change info of oracle database
            connectionFactory = new ConnectionFactory();
            connectionFactory.url = "jdbc:oracle:thin:@10.11.10.12:1521:APP";
            connectionFactory.user = "EOCSGW_OWNER";
            connectionFactory.password = "ocsgw";
            connectionFactory.maxConnection = 10;
            connectionFactory.minConnection = 1;

            callFunctionOrProduceOracle();

        } finally {
            // Close connection finally
            connectionFactory.closePool();
        }
    }

    /**
     * Coppy this to oracle console to create function:

     * create FUNCTION themmoi_ls_reset(
     *     psso_tb IN VARCHAR2
     * ) RETURN VARCHAR2
     * AS
     *     v_output_string VARCHAR2(1) := psso_tb;
     * BEGIN
     *     RETURN v_output_string;
     * END;

     */
    private static void callFunctionOrProduceOracle() throws Exception {
        Connection connection = connectionFactory.getConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("{ ? = call any_function(?) }");
            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.setString(2, "Hello world !");
            callableStatement.execute();

            String returnValue = callableStatement.getString(1);
            System.out.println("Response from function Oracle: " + returnValue);

            callableStatement.close();
        } finally {
            // Close connection finally
            connectionFactory.closeConnection(connection);
        }
    }

    private Connection getConnection() throws Exception {
        if (connection == null || connection.isClosed()) {
            connection = connectionFactory.getConnection();
        }
        return connection;
    }
    public boolean isOpen() throws Exception {
        return connectionFactory.isOpen();
    }
    public void closePool() throws SQLException {
        connectionFactory.closePool();
    }

}
