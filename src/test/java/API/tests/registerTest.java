package API.tests;

import API.pojo.register;
import API.utils.DatabaseOperations;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.restassured.config.LogConfig;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static API.utils.DatabaseOperations.isEmailPresent;
import static API.utils.DatabaseOperations.validateDatabase;

public class registerTest {
    register payload;
    public Logger logger;

    @BeforeClass
    public void setup() {
        logger = LogManager.getLogger(this.getClass());
        RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }

    @Test(priority = 1)
    public void register() {
        logger.info("********* Registring User ***********");

        payload = new register();
        payload.setEmail("test42@test.com");
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
            DatabaseOperations.insertIntoDatabase(payload);
            DatabaseOperations.validateDatabase(payload);
        }
        logger.info("********User Created***********");

    }

    @Test(priority = 2)
    public void updateAndValidateCustomer() {
        logger.info("********User Update***********");

        payload = new register();
        payload.setEmail("test42@test.com");
        payload.setPassword("newpassword");
        payload.setFirstname("UpdatedName");
        payload.setLastname("UpdatedLastname");
        payload.setTelephone("1231231231");

        DatabaseOperations.updateCustomer(payload);
        DatabaseOperations.validateDatabase(payload);
        logger.info("********User Updated***********");

    }

    @Test(priority = 3)
    public void deleteAndValidateCustomer() {
        logger.info("********User Deleting***********");

        String email = "test42@test.com";

        DatabaseOperations.deleteCustomer(email);

        Assert.assertFalse(DatabaseOperations.isEmailPresent(email), "Email should not be present in the database");
        logger.info("********User Deleted***********");

    }
}
