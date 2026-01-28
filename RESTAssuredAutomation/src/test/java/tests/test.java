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
    
    
    @Test(
    	    priority = 5,
    	    dataProvider = "searchPayloadData",
    	    dataProviderClass = SearchDataProvider.class
    	)
    	public void validateUserType(Map<String, String> excelData) {

    	    search src = new search(null);

    	    // API call (no Response assignment)
    	    src.getPropertySearchAPIResponse(excelData);

    	    String userType = excelData.get("inputListings");

    	    if (userType == null || userType.trim().isEmpty()) {
    	        System.out.println("User type not provided in test data. Skipping validation.");
    	        return;
    	    }

    	    src.validateUserTypeAccordingToSearch(userType);
    	}
    
    
    @Test(
    	    priority = 6,
    	    dataProvider = "searchPayloadData",
    	    dataProviderClass = SearchDataProvider.class
    	)
    	public void validatePropertyType(Map<String, String> excelData) {

    	    search src = new search(null);

    	    // API Call
    	    src.getPropertySearchAPIResponse(excelData);

    	    String propertyType = excelData.get("propertyType");

    	    if (propertyType == null || propertyType.trim().isEmpty()) {
    	        System.out.println("Property type not provided in test data. Skipping validation.");
    	        return;
    	    }

    	    src.validatePropertyTypeAccordingToSearch(propertyType);
    	}
    
    
    
    // Validate Property type
    
    
    
    // Validate Developer Info
    // Validate Contact Details
    // Validate BHK Dispaly
    // Validate Poss.date
    
    
    
    
    
    
    
    
}
