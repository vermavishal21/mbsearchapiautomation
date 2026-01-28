package Utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class APIUtils {

    public static Response get(String path, Map<String, ?> params) {

        // Build full URI with query params for printing
        StringBuilder fullUri = new StringBuilder(path + "?");
        if (params != null) {
            params.forEach((k, v) -> fullUri.append(k).append("=").append(v).append("&"));
            fullUri.deleteCharAt(fullUri.length() - 1); // Remove last "&"
        }

        // ✅ Print only Request URI
        System.out.println("Request URI: " + fullUri);

        // Perform API call without logging all details
        return given()
        		//.log().all()
                .queryParams(params) // send params
            .when()
                .get(path)
            .then()
                .extract()
                .response();
    }
}

 
