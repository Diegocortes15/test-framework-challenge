import io.qameta.allure.Description;
import org.themoviedb.auth.AuthConstants;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthSpec {

    @Test
    @Description("Create request token given api_key and access_token")
    public void createRequestToken() {

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
    }

    @Test
    @Description("Create access token given api_key and access_token from request token")
    public void createAccessToken(){

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + AuthConstants.USER_ACCESS_TOKEN);
        headers.put("Content-type", "application/json");

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
    }
}
