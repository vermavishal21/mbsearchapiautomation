package Pages;


import java.util.List;
import java.util.Map;

import org.testng.Assert;

import Utils.APIUtils;
import Utils.PayloadBuilder;
import Utils.Support;
import io.restassured.response.Response;



public class search {
	
	
	
	public search(Object driver) {
		// TODO Auto-generated constructor stub
	}
	
	Object driver = null;
	private search MBSearchAPI;

	
	public Response getPropertySearchAPIResponse() {
	    return APIUtils.get(
	            "/mbsrp/propertySearch.html",
	            PayloadBuilder.fullSearchPayload()
	    );
	}
	
	
	
	public void apiresponse () {
		
		//String targetId = "81491289";  // ID you want to filter
		
		Response response = APIUtils.get(
                "/mbsrp/propertySearch.html",
                PayloadBuilder.fullSearchPayload()
        );

        response.then().statusCode(200);
        List<String> ids = response.jsonPath().getList("resultList.id");
        System.out.println("===== PROPERTY IDS =====");

        for (String id : ids) {
            System.out.println("ID : " + id);
        }

        System.out.println("Total IDs : " + ids.size());
        
        System.out.println("===== Full API Response =====");
     //   System.out.println(response.getBody().asPrettyString());
        System.out.println("=============================");
        
        
//        List<Map<String, Object>> properties = response.jsonPath().getList("resultList");
//
//        boolean found = false;
//
//        System.out.println("========= Searching Property ID: " + targetId + " =========");
//
//        for (Map<String, Object> property : properties) {
//
//            if (property.get("id") != null && property.get("id").toString().equals(targetId)) {
//
//                found = true;
//
//                System.out.println("✔ Property Found!");
//                System.out.println("------------------------------------");
//
//                for (String key : property.keySet()) {
//                    System.out.println(key + " : " + property.get(key));
//                }
//
//                System.out.println("------------------------------------");
//                break;
//            }
//        }
//
//        if (!found) {
//            System.out.println("❌ Property ID " + targetId + " not found in API response.");
//        }
//        
        
        
        
        
	}
	
	
	
	public void verifyPropertySearchAPIStatus() {

        Response response = APIUtils.get(
                "/mbsrp/propertySearch.html",
                PayloadBuilder.fullSearchPayload()
        );

        response.then().statusCode(200);
        System.out.println("===== API Status Details =====");
        System.out.println();  // blank line
	        System.out.println("API Status : " + response.getStatusCode());
	        System.out.println();  // blank line
	        //System.out.println(response.getBody().asPrettyString());
        System.out.println("**************************");  // blank line
	
       
    }
	
	
	
	
	//Test Case: Validate Mandatory Fields Exist
	public void validateMandatoryFields(Response response) {

	    List<Map<String, Object>> list = response.jsonPath().getList("resultList");

	    System.out.println("========= Validating Mandatory Fields =========");

	    int failCount = 0;

	    String[] mandatoryFields = {
	            "encId","possStatusD","pmtSource","url","postDateT","endDateT","priceD",
	           "price"
	    };

	    for (int i = 0; i < list.size(); i++) {

	        Map<String, Object> item = list.get(i);
	        boolean missing = false;

	        for (String field : mandatoryFields) {

	            if (!item.containsKey(field) ||
	                    item.get(field) == null ||
	                    item.get(field).toString().trim().isEmpty()) {

	                missing = true;
	                System.out.println(" Missing Field: " + field + " at index " + i);
	            }
	        }

	        if (missing) {
	            failCount++;

	            System.out.println("Record Details:");
	            System.out.println("ID  : " + item.get("id"));
	            System.out.println("URL : " + item.get("url"));
	            System.out.println("--------------------------------------");
	        }
	    }

	    System.out.println("==============================================");
	    System.out.println("Total Records Checked: " + list.size());
	    System.out.println("Missing Mandatory Fields Count: " + failCount);
	    System.out.println("==============================================");

	    Assert.assertEquals(failCount, 0, "Some mandatory fields were missing!");
	}
	
	
// Test Case : Validate Price Range
	public void validatePriceRange(Response response) {

	    List<Map<String, Object>> properties =
	            response.jsonPath().getList("resultList");

	    int totalRecords = properties.size();
	    int failCount = 0;
	    int skippedCount = 0;

	    System.out.println("========== VALIDATION PRICE RANGE  ==========");

	    for (int i = 0; i < totalRecords; i++) {

	        Map<String, Object> item = properties.get(i);

	        String id  = String.valueOf(item.get("id"));
	        String url = String.valueOf(item.get("url"));

	        // Step 1–3: Get required fields
	        Number sqFtPrice = (Number) item.get("sqFtPrice");
	        Number caSqFt    = (Number) item.get("caSqFt");
	        Number price     = (Number) item.get("price");

	        // Step 4–6: Get safety flags
	        String iba  = item.get("iba")  != null ? item.get("iba").toString()  : "";
	        String cpmp = item.get("cpmp") != null ? item.get("cpmp").toString() : "";
	        String pl   = item.get("pl")   != null ? item.get("pl").toString()   : "";

	        // Step 7: SAFETY CHECK
	        boolean skipValidation =
	                ("Z".equalsIgnoreCase(iba)
	                || "Y".equalsIgnoreCase(cpmp)
	                || "Y".equalsIgnoreCase(pl))
	                && (sqFtPrice == null || caSqFt == null || price == null);

	        if (skipValidation) {
	            skippedCount++;
	            System.out.println("SKIPPED (Safety Rule Applied)");
	            System.out.println("ID  : " + id);
	            System.out.println("URL : " + url);
	            System.out.println("----------------------------------");
	            continue;
	        }

	        // If mandatory fields still missing → FAIL
	        if (sqFtPrice == null || caSqFt == null || price == null) {
	            failCount++;
	            System.out.println("FAIL – Missing Required Price Fields");
	            System.out.println("ID  : " + id);
	            System.out.println("URL : " + url);
	            System.out.println("sqFtPrice : " + sqFtPrice);
	            System.out.println("caSqFt    : " + caSqFt);
	            System.out.println("price     : " + price);
	            System.out.println("----------------------------------");
	            continue;
	        }

	        // Step 8: Calculate expected price
	        double calculatedPrice =
	                sqFtPrice.doubleValue() * caSqFt.doubleValue();

	        // Step 9: ±2% range
	        double lowerLimit = calculatedPrice * 0.98;
	        double upperLimit = calculatedPrice * 1.02;

	        // Step 10: Validate price
	        double actualPrice = price.doubleValue();

	        if (actualPrice < lowerLimit || actualPrice > upperLimit) {

	            failCount++;

	            System.out.println("❌ PRICE OUT OF RANGE");
	            System.out.println("ID            : " + id);
	            System.out.println("URL           : " + url);
	            System.out.println("SqFt Price    : " + sqFtPrice);
	            System.out.println("Area (SqFt)   : " + caSqFt);
	            System.out.println("Calculated    : " + calculatedPrice);
	            System.out.println("Allowed Range : " + lowerLimit + " - " + upperLimit);
	            System.out.println("Actual Price  : " + actualPrice);
	            System.out.println("----------------------------------");

	        } else {
//	            System.out.println(" PRICE OK → ID: " + id);
//	            System.out.println(" User Type →  : " + iba);
	        }
	    }

	    System.out.println("============================================");
	    System.out.println("Total Records Checked : " + totalRecords);
	    System.out.println("Skipped Records       : " + skippedCount);
	    System.out.println("Failed Records        : " + failCount);
	    System.out.println("============================================");

	    // ✅ Final Assertion
	    Assert.assertEquals(
	            failCount,
	            0,
	            "Price range validation failed for some properties!"
	    );
	}
	
// Test Case : Validate Formatted Price
		

	public void validatePriceFormatted(Response response) {

	    List<Map<String, Object>> properties =
	            response.jsonPath().getList("resultList");

	    List<Number> priceList =
	            response.jsonPath().getList("resultList.price", Number.class);

	    List<String> priceDList =
	            response.jsonPath().getList("resultList.priceD");

	    int totalRecords = properties.size();
	    int failCount = 0;
	    int skippedCount = 0;
	    
	    System.out.println("========== VALIDATION FORMATTED PRICE ==========");

	    for (int i = 0; i < totalRecords; i++) {

	        // STEP 1: Get values
	        double price = priceList.get(i).doubleValue();
	        String priceD = priceDList.get(i);

	        // Skip if priceD is null or empty
	        if (priceD == null || priceD.isEmpty()) {
	            skippedCount++;
	            continue;
	        }

	        // STEP 2: Convert formatted price
	        double priceDValue = Support.convertPriceD(priceD);

	        // STEP 3: Validate ±1%
	        double minPrice = price * 0.99;
	        double maxPrice = price * 1.01;

	        if (priceDValue < minPrice || priceDValue > maxPrice) {
	            failCount++;
	            System.out.println("============================================");
	            System.out.println(
	                    " Failed at index " + i +
	                    " | Price: " + price +
	                    " | PriceD: " + priceD +
	                    " | Converted: " + priceDValue
	            );
	            System.out.println("============================================");
	        }
	    }
	    
	    System.out.println("============================================");
	    System.out.println("Total Records  : " + totalRecords);
	    System.out.println("Failed Records : " + failCount);
	    System.out.println("Skipped Records: " + skippedCount);
	    System.out.println("============================================");
	    // Final assertion
	    Assert.assertEquals(failCount, 0, "Formatted price validation failed");
	}
	
	
	
	
	
	
	

}
