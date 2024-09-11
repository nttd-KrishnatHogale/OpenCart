package API.tests;

import API.endpoints.loginEndpoint;
import API.pojo.login;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import API.endpoints.logoutEndpoint;
import org.testng.Assert;

public class logoutTest {

    @Test(priority = 1)
    public void logout() {
        Response response = logoutEndpoint.userLogout();
        System.out.println(response.statusCode());
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
