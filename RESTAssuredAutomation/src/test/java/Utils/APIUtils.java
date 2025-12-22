package Utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Map;

public class APIUtils {

    static {
        RestAssured.baseURI = "https://www.magicbricks.com";
    }

    public static Response get(String endpoint, Map<String, Object> payload) {

        return RestAssured
                .given()
                .contentType("application/json")
                .body(payload)
                .post(endpoint)
                .then()
                .extract()
                .response();
    }
}
