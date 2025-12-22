package Pages;

import Utils.APIUtils;
import Utils.ExcelUtils;
import Utils.PayloadBuilder;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class search {

    public List<Response> getResponsesFromExcel() {

        List<Response> responses = new ArrayList<>();

        List<Map<String, String>> excelRows =
                ExcelUtils.getSearchData();

        for (Map<String, String> row : excelRows) {

            Response response = APIUtils.get(
                    "/mbsrp/propertySearch.html",
                    PayloadBuilder.searchPayloadFromExcel(row)
            );

            responses.add(response);
        }

        return responses;
    }
}
