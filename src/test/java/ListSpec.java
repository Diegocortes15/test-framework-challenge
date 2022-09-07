import org.testng.annotations.Test;
import org.themoviedb.auth.AuthConstants;
import org.themoviedb.list.ListConstants;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ListSpec {

    @Test
    public void createList() {
        String json = String.format("""
                {
                    "name": "%s",
                    "iso_639_1": "en"
                }
                """, ListConstants.LIST_NAME);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + AuthConstants.USER_APP_ACCESS_TOKEN);
        headers.put("Content-type", "application/json");

        given()
                .baseUri("https://api.themoviedb.org/4")
                .headers(headers)
                .queryParam("api_key", AuthConstants.USER_API_KEY)
                .body(json)
        .when()
                .post("/list")
        .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .body("status_message", is(equalTo("The item/record was created successfully.")),
                        "success", is(true));
    }

    @Test
    public void getList() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + AuthConstants.USER_APP_ACCESS_TOKEN);

        given()
                .baseUri("https://api.themoviedb.org/4")
                .headers(headers)
                .queryParam("api_key", AuthConstants.USER_API_KEY)
        .when()
                .get("/list/" + ListConstants.LIST_ID)
        .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("id", is(equalTo(ListConstants.LIST_ID)),
                        "name", is(equalTo(ListConstants.LIST_NAME)));
    }

    @Test
    public void addItems() {

        String json = """
                {
                    "items": [
                        {
                            "media_type": "movie",
                            "media_id": 550
                        },
                        {
                            "media_type": "movie",
                            "media_id": 244786
                        },
                        {
                            "media_type": "tv",
                            "media_id": 1396
                        }
                    ]
                }
                """;

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + AuthConstants.USER_APP_ACCESS_TOKEN);
        headers.put("Content-type", "application/json");

        given()
                .baseUri("https://api.themoviedb.org/4")
                .headers(headers)
                .queryParam("api_key", AuthConstants.USER_API_KEY)
                .body(json)
        .when()
                .post("/list/" + ListConstants.LIST_ID + "/items")
        .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("status_message", is(equalTo("Success.")),
                        "success", is(true),
                        "results[0].media_type", is(equalTo("movie")),
                        "results[0].media_id", is(equalTo(550)),
                        "results[1].media_type", is(equalTo("movie")),
                        "results[1].media_id", is(equalTo(244786)),
                        "results[2].media_type", is(equalTo("tv")),
                        "results[2].media_id", is(equalTo(1396))
                );
    }
}
