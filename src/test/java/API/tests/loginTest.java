package API.tests;
import API.pojo.login;
import org.testng.annotations.Test;
import io.restassured.response.Response;
import API.endpoints.loginEndpoint;
import org.testng.Assert;



public class loginTest {
    login payload;

    @Test(priority = 1)
    public void login() {
        payload = new login();
        payload.setEmail("test4@test.com");
        payload.setPassword("1234");
        Response response = loginEndpoint.userLogin(payload);
        System.out.println(response.statusCode());
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
