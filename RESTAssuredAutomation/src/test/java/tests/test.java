package tests;

import java.util.Map;

import org.testng.annotations.Test;

import Pages.search;
import Setup.Base;
import DataProvider.SearchDataProvider;

public class test extends Base {

    @Test(
        priority = 1,
        dataProvider = "searchPayloadData",
        dataProviderClass = SearchDataProvider.class
    )
    public void printPropertyIds(Map<String, String> excelData) {

        search src = new search(null);
        src.getPropertySearchAPIResponse(excelData);

        src.printAllPropertyIds();
    }

    @Test(
        priority = 2,
        dataProvider = "searchPayloadData",
        dataProviderClass = SearchDataProvider.class
    )
    public void validateMandatoryFields(Map<String, String> excelData) {

        search src = new search(null);
        src.getPropertySearchAPIResponse(excelData);

        src.validateMandatoryFields();
    }

    @Test(
        priority = 3,
        dataProvider = "searchPayloadData",
        dataProviderClass = SearchDataProvider.class
    )
    public void validatePriceRange(Map<String, String> excelData) {

        search src = new search(null);
        src.getPropertySearchAPIResponse(excelData);

        src.validatePriceRange();
    }

    @Test(
        priority = 4,
        dataProvider = "searchPayloadData",
        dataProviderClass = SearchDataProvider.class
    )
    public void validateFormattedPrice(Map<String, String> excelData) {

        search src = new search(null);
        src.getPropertySearchAPIResponse(excelData);

        src.validatePriceFormatted();
    }
}
