package Utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class APIUtils {

    public static Response get(String url, Map<String, Object> params) {

        return given()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .header("Accept", "application/json, text/plain, */*")
                .cookie("lang", "en")
                .cookie("mbVisitorId", "1234567890")
                .cookie("authId", "xyz123")
                .queryParams(params)
                .contentType(ContentType.HTML)// important
                .when()
                .get(url)
                .then()
                .extract()
                .response();
    }
}



//package Utils;
//
//import io.restassured.http.ContentType;
//import io.restassured.response.Response;
//
//import java.util.Map;
//
//import static io.restassured.RestAssured.given;
//
//public class APIUtils {
//
//    public static Response get(Map<String, Object> params) {
//        return given()
//                .header("User-Agent", "Mozilla/5.0")
//                .queryParams(params)
//                .contentType(ContentType.HTML)   // important
//                .when()
//                .get()
//                .then()
//                .extract()
//                .response();
//    }
//}
