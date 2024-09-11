package API.endpoints;

import API.pojo.login;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

public class loginEndpoint {
    public static Response userLogin(login payload) {

        Response response = given()
                .config(RestAssured.config().encoderConfig(
                        EncoderConfig.encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)
                ))
                .contentType("multipart/form-data")
                .formParam("email", payload.getEmail())
                .formParam("password", payload.getPassword())
                .param("route","account/login")
                .when()
                .post(Routes.user_login_url);
        return response;
    }



}
