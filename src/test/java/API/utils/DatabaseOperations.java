package API.utils;

import API.pojo.register;

import java.sql.*;
import java.util.List;

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
            preparedStatement.setString(9, payload.getPassword());
            preparedStatement.setString(10, "");
            preparedStatement.setString(11, "");
            preparedStatement.setString(12, "");
            preparedStatement.setInt(13, payload.getNewsletter());
            preparedStatement.setInt(14, 0);
            preparedStatement.setString(15, "");
            preparedStatement.setString(16, "");
            preparedStatement.setInt(17, 1);
            preparedStatement.setInt(18, 0);
            preparedStatement.setString(19, "");
            preparedStatement.setString(20, "");

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JsonFileUtils.writePayloadToJson(payload, "src/test/resources/inserted_data.json");
                ExcelFileUtils.writePayloadToExcel(List.of(payload), "src/test/resources/inserted_data.xlsx");

            } else {
                throw new RuntimeException("No rows were inserted into the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed");
        } finally {
            DatabaseUtils.closeResources(connection, preparedStatement, null);
        }
    }

    public static void updateCustomer(register payload) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseUtils.getConnection();
            String query = "UPDATE oc_customer SET " +
                    "customer_group_id = ?, " +
                    "store_id = ?, " +
                    "language_id = ?, " +
                    "firstname = ?, " +
                    "lastname = ?, " +
                    "telephone = ?, " +
                    "fax = ?, " +
                    "password = ?, " +
                    "salt = ?, " +
                    "cart = ?, " +
                    "wishlist = ?, " +
                    "newsletter = ?, " +
                    "address_id = ?, " +
                    "custom_field = ?, " +
                    "ip = ?, " +
                    "status = ?, " +
                    "safe = ?, " +
                    "token = ?, " +
                    "code = ? " +
                    "WHERE email = ?";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, payload.getCustomer_group_id());
            preparedStatement.setInt(2, 0);
            preparedStatement.setInt(3, 1);
            preparedStatement.setString(4, payload.getFirstname());
            preparedStatement.setString(5, payload.getLastname());
            preparedStatement.setString(6, payload.getTelephone());
            preparedStatement.setString(7, "");
            preparedStatement.setString(8, payload.getPassword());
            preparedStatement.setString(9, "");
            preparedStatement.setString(10, "");
            preparedStatement.setString(11, "");
            preparedStatement.setInt(12, payload.getNewsletter());
            preparedStatement.setInt(13, 0);
            preparedStatement.setString(14, "");
            preparedStatement.setString(15, "");
            preparedStatement.setInt(16, 1);
            preparedStatement.setInt(17, 0);
            preparedStatement.setString(18, "");
            preparedStatement.setString(19, "");
            preparedStatement.setString(20, payload.getEmail());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JsonFileUtils.writePayloadToJson(payload, "src/test/resources/updated_data.json");
                ExcelFileUtils.writePayloadToExcel(List.of(payload), "src/test/resources/updated_data.xlsx");

            } else {
                throw new RuntimeException("No rows were updated in the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed");
        } finally {
            DatabaseUtils.closeResources(connection, preparedStatement, null);
        }
    }

    public static void deleteCustomer(String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseUtils.getConnection();
            String query = "DELETE FROM oc_customer WHERE email = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {

//                JsonFileUtils.removeRecordFromJson(email, "src/test/resources/inserted_data.json");
//                JsonFileUtils.removeRecordFromJson(email,"src/test/resources/updated_data.json");
                ExcelFileUtils.removeRecordFromExcel(email, "src/test/resources/inserted_data.xlsx");
                ExcelFileUtils.removeRecordFromExcel(email,"src/test/resources/updated_data.xlsx");


                JsonFileUtils.writeDeletionRecordToJson(email, "src/test/resources/deleted_data.json");
                ExcelFileUtils.writeDeletionRecordToExcel(email, "src/test/resources/deleted_data.xlsx");


            } else {
                throw new RuntimeException("No rows were deleted from the database");
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
