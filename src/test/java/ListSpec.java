import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import org.themoviedb.auth.AuthConstants;
import org.themoviedb.list.ListConstants;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@Feature("List Tests")
public class ListSpec {

    private  static final Logger logger = LogManager.getLogger("listSpecFileLogger");

    @Test
    @Description("Create a list of movies given a name")
    public void createList() {

        logger.info("Create a list of movies given a name");
        logger.info("Setting requestBody to create a movie list");
        String json = String.format("""
                {
                    "name": "%s",
                    "iso_639_1": "en"
                }
                """, ListConstants.LIST_NAME);

        logger.info("Setting headers");
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + AuthConstants.USER_APP_ACCESS_TOKEN);
        headers.put("Content-type", "application/json");

        logger.info("Making a POST request with /list resource to create a new list");
        given()
                .baseUri("https://api.themoviedb.org/4")
                .headers(headers)
                .queryParam("api_key", AuthConstants.USER_API_KEY)
                .body(json)
        .when()
                .post("/list")
        .then()
                .log().body()
                .assertThat()
                .statusCode(201)
                .body("status_message", is(equalTo("The item/record was created successfully.")),
                        "success", is(true));

        logger.info("Making assertions from response body");
    }

    @Test
    @Description("Get a list of movies given an id")
    public void getList() {

        logger.info("Create a list of movies given a name");
        logger.info("Setting headers");
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + AuthConstants.USER_APP_ACCESS_TOKEN);

        logger.info("Making a POST request with /list resource to create a new list");
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

        logger.info("Making assertions from response body");
    }

    @Test
    @Description("Add movies in a list given an id")
    public void addItems() {

        logger.info("Add movies in a list given an id");
        logger.info("Setting requestBody to create a movie list");
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

        logger.info("Making a POST request with /list resource to create a new list");
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

        logger.info("Making assertions from response body");
    }
}
