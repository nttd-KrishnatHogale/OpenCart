package API.utils;

import java.sql.*;

public class DatabaseUtils {

    private static final String URL = "jdbc:mysql://localhost:3307/openshop";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Load MySQL JDBC Driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeResources(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void printAllTables() {
        Connection connection = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            DatabaseMetaData metaData = connection.getMetaData();

            // Retrieve tables
            resultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            System.out.println("Tables in the database:");
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                System.out.println(tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, null, resultSet);
        }
    }
}
