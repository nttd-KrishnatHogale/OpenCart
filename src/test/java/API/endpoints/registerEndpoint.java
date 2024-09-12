package API.endpoints;

import API.pojo.login;
import API.pojo.register;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.RedirectConfig;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class registerEndpoint {
    public static Response userRegister(register payload) {
        String userRegisterUrl = "http://127.0.0.1/opencart/upload/index.php";

        RestAssuredConfig config = RestAssuredConfig.newConfig()
                .encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs("multipart/form-data", ContentType.TEXT))
                .redirect(RedirectConfig.redirectConfig()
                        .followRedirects(false));


        Response response = given()

                .config(config)
                .contentType("multipart/form-data")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("Connection", "keep-alive")
                .multiPart("customer_group_id", "1")
                .multiPart("language_id", payload.getLanguage_id())
                .multiPart("firstname", payload.getFirstname())
                .multiPart("lastname", payload.getLastname())
                .multiPart("email", payload.getEmail())
                .multiPart("telephone", payload.getTelephone())
                .multiPart("password", payload.getPassword())
                .multiPart("confirm", payload.getConfirm())
                .multiPart("newsletter", "0")
                .multiPart("agree", "1")
                .param("route", "account/register")
                .when()
                .post(Routes.user_register_url);
        return response;

    }
}
