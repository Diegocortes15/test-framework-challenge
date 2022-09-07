import io.qameta.allure.Description;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.themoviedb.auth.AuthConstants;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthSpec {
    private  static final Logger logger = LogManager.getLogger("authSpecFileLogger");

    @Test
    @Description("Create request token given api_key and access_token")
    public void createRequestToken() {

        logger.info("Create a list of movies given a name");
        logger.info("Setting headers");
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + AuthConstants.USER_ACCESS_TOKEN);

        given()
                .baseUri("https://api.themoviedb.org/4/auth")
                .headers(headers)
                .queryParam("api_key", AuthConstants.USER_API_KEY)
        .when()
                .post("/request_token")
        .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("$", hasKey("request_token"),
                        "status_message", is(equalTo("Success.")));
        logger.info("Verifying that request_token exist and status_message is Success.");
    }

    @Test
    @Description("Create access token given api_key and access_token from request token")
    public void createAccessToken(){

        logger.info("Create access token given api_key and access_token from request token");
        logger.info("Setting headers");
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + AuthConstants.USER_ACCESS_TOKEN);
        headers.put("Content-type", "application/json");

        logger.info("Setting requestBody to create a movie list");
        String json = String.format("""
                {
                    "request_token": "%s"
                }
                """, AuthConstants.USER_REQUEST_TOKEN);

        given()
                .baseUri("https://api.themoviedb.org/4/auth")
                .headers(headers)
                .queryParam("api_key", AuthConstants.USER_API_KEY)
                .body(json)
        .when()
                .post("/access_token")
        .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("$",hasKey("access_token"),
                        "status_message", is(equalTo("Success.")));
        logger.info("Verifying that access_token exist and status_message is Success.");
    }
}
