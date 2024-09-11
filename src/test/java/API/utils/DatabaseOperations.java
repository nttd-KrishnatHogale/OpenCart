package API.utils;

import API.pojo.register;

import java.sql.*;

public class DatabaseOperations {

    public static boolean isEmailPresent(String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean exists = false;

        try {
            connection = DatabaseUtils.getConnection();
            String query = "SELECT COUNT(*) FROM oc_customer WHERE email = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                exists = resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed");
        } finally {
            DatabaseUtils.closeResources(connection, preparedStatement, resultSet);
        }

        return exists;
    }

    public static void insertIntoDatabase(register payload) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseUtils.getConnection();
            String query = "INSERT INTO oc_customer (customer_group_id, store_id, language_id, firstname, lastname, email, telephone, fax, password, salt, cart, wishlist, newsletter, address_id, custom_field, ip, status, safe, token, code, date_added) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, payload.getCustomer_group_id());
            preparedStatement.setInt(2, 0);
            preparedStatement.setInt(3, payload.getLanguage_id());
            preparedStatement.setString(4, payload.getFirstname());
            preparedStatement.setString(5, payload.getLastname());
            preparedStatement.setString(6, payload.getEmail());
            preparedStatement.setString(7, payload.getTelephone());
            preparedStatement.setString(8, "");
            preparedStatement.setString(9, payload.getPassword()); // In a real scenario, password should be hashed
            preparedStatement.setString(10, ""); // Salt can be empty or you need to generate it
            preparedStatement.setString(11, ""); // Cart is empty or default
            preparedStatement.setString(12, ""); // Wishlist is empty or default
            preparedStatement.setInt(13, payload.getNewsletter()); // Assuming this is an integer
            preparedStatement.setInt(14, 0); // Address_id is 0 or default
            preparedStatement.setString(15, ""); // Custom_field is optional or default
            preparedStatement.setString(16, ""); // IP address can be default or empty
            preparedStatement.setInt(17, 1); // Assuming status is 1 (active)
            preparedStatement.setInt(18, 0); // Assuming safe is 0 (not safe)
            preparedStatement.setString(19, ""); // Token can be empty or generated
            preparedStatement.setString(20, ""); // Code is optional or default

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected <= 0) {
                throw new RuntimeException("No rows were inserted into the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed");
        } finally {
            DatabaseUtils.closeResources(connection, preparedStatement, null);
        }
    }

    public static void validateDatabase(register payload) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseUtils.getConnection();
            String query = "SELECT * FROM oc_customer WHERE email = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, payload.getEmail());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if (!resultSet.getString("firstname").equals(payload.getFirstname()) ||
                        !resultSet.getString("lastname").equals(payload.getLastname()) ||
                        !resultSet.getString("email").equals(payload.getEmail()) ||
                        !resultSet.getString("telephone").equals(payload.getTelephone())) {
                    System.out.println("data is same");
                    throw new RuntimeException("Database record does not match the expected values");
                }
            } else {
                throw new RuntimeException("No record found in database for email: " + payload.getEmail());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed");
        } finally {
            DatabaseUtils.closeResources(connection, preparedStatement, resultSet);
        }
    }
}
