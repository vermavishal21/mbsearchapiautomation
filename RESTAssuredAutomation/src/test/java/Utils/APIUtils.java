package Utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class APIUtils {

	public static Response get(String path, Map<String, ?> params) {
        return given()
                .log().all()
                .queryParams(params)
             .when()
                .get("/mbsrp/propertySearch.html")
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
