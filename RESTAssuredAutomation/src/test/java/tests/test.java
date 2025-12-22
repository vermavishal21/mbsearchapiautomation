package tests;

import Setup.Base;
import io.restassured.response.Response;

import org.testng.annotations.Test;
import Pages.search;


public class test extends Base {
	
	Object driver = null;
	private search src;
	
//=============================================//	
//	@Test
//	public void apiresponse() {
//		src = new search(driver);
//		src.apiresponse();
//	}

//=============================================//
	
	
	
	
	//Test Case : to verify API is Working or not
    @Test(priority = 1)
    public void validatePropertySearchAPIStatus() {
		src = new search(driver);
		src.verifyPropertySearchAPIStatus();
    }
    
	//Test Case: Validate Mandatory Fields Exist
    @Test(priority = 2)
    public void validateMandatoryFieldsValidation() {
        src = new search(driver);
        Response response = src.getPropertySearchAPIResponse();
        response.then().statusCode(200);
        src.validateMandatoryFields(response);
    }
    
	//Test Case : Validate Price Range
    @Test(priority = 3)
   public void validatePriceCalculationTest() {
    	  src = new search(driver);
          Response response = src.getPropertySearchAPIResponse();
          response.then().statusCode(200);
          src.validatePriceRange(response);
    }
          
    //Test Case: Validate Formatted Price 
    @Test(priority = 4)
    public void validatepriceformatted() {
    	src = new search (driver);
    	Response response = src.getPropertySearchAPIResponse();
    	response.then().statusCode(200);
    	src.validatePriceFormatted(response);
    	
    }
    
    
    
}
