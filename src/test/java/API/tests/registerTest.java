package API.tests;

import API.endpoints.registerEndpoint;
import API.pojo.register;
import API.utils.DatabaseOperations;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class registerTest {
    register payload;

    @BeforeClass
    public void setup() {
        RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }

    @Test(priority = 1)
    public void register() {
        payload = new register();
        payload.setEmail("test23@test.com");
        payload.setLanguage_id(1);
        payload.setPassword("1234");
        payload.setFirstname("Krish");
        payload.setLastname("Hogale");
        payload.setTelephone("1234567812");
        payload.setConfirm("1234");
        payload.setAgree(1);
        payload.setNewsletter(0);
        payload.setCustomer_group_id(1);

        // Check if email already exists
        if (DatabaseOperations.isEmailPresent(payload.getEmail())) {
            System.out.println("Email already exists in the database.");
            Assert.fail("Email already exists in the database.");
        } else {
            // Insert data into database directly
            DatabaseOperations.insertIntoDatabase(payload);

            // Validate data in the database
            DatabaseOperations.validateDatabase(payload);
        }
    }
}
