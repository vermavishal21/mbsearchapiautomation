package tests;

import Pages.search;
import Setup.Base;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class test extends Base {

    @Test
    public void validateSearchFromExcel() {

        search src = new search();
        List<Response> responses = src.getResponsesFromExcel();

        Assert.assertTrue(responses.size() > 0,
                "❌ No Excel rows found with Run = Y");

        for (Response response : responses) {
            response.then().statusCode(200);
        }
    }
}
