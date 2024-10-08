package API.endpoints;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class logoutEndpoint {
    public static Response userLogout() {

        Response response = given()
                .config(RestAssured.config().encoderConfig(
                        EncoderConfig.encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)
                ))
                .contentType("multipart/form-data")
                .param("route","account/logout")
                .when()
                .post(Routes.user_logout_url);
        return response;
    }



}
