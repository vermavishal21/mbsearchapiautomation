package Pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.SkipException;

import Utils.APIUtils;
import Utils.PayloadBuilder;
import Utils.Support;
import io.restassured.response.Response;

public class search {

    private Response response;

    public search(Object driver) { }

    /* ================= API HIT (DataProvider BASED) ================= */

    public void getPropertySearchAPIResponse(Map<String, String> excelData) {

        //System.out.println("Executing Search API with payload:");
        //System.out.println(excelData);

        response = APIUtils.get(
                "/mbsrp/propertySearch.html",
                PayloadBuilder.buildPayload(excelData)
        );

        response.then().statusCode(200);

//        System.out.println("===== API STATUS =====");
//        System.out.println("Status Code : " + response.getStatusCode());
//        System.out.println("======================");
    }

    /* ================= COMMON RESULT LIST ================= */

    public List<Map<String, Object>> getResultList() {

        List<Map<String, Object>> resultList =
                response.jsonPath().getList("resultList");

        if (resultList == null || resultList.isEmpty()) {

            System.out.println("⚠️ No data returned for payload");
            System.out.println("Skipping validations for this test case");
            System.out.println("--------------------------------------------");

            throw new SkipException(
                    "No data returned for this payload. Skipping validations."
            );
        }

        return resultList;
    }
    
    

    /* ================= EXTRA FUNCTIONALITY (2nd CLASS) ================= */

    // Print all Property IDs
    public void printAllPropertyIds() {

        List<String> ids =
                response.jsonPath().getList("resultList.id");

        System.out.println("===== PROPERTY IDS =====");
        

//        for (String id : ids) {
//            System.out.println("ID : " + id);
//        }
//
//        System.out.println("Total IDs : " + ids.size());
//        System.out.println("========================");
        
        
        if (ids == null || ids.isEmpty()) {
            System.out.println("No property IDs found");
            System.out.println("Total Property Count : 0");
            System.out.println("========================");
            return;
        }

        // ✅ Print IDs
//        for (String id : ids) {
//            System.out.println("ID : " + id);
//        }

        // ✅ Record count
        System.out.println("========================");
        System.out.println("Total Property Count : " + ids.size());
        System.out.println("========================");
        
        
        
    }

    /* ================= VALIDATION : MANDATORY FIELDS ================= */

    public void validateMandatoryFields() {

        List<Map<String, Object>> list = getResultList();

        int failCount = 0;
        int passCount = 0;

        String[] mandatoryFields = {
                "encId", "possStatusD", "pmtSource",
                "url", "postDateT", "endDateT",
                "priceD", "price"
        };

        System.out.println("\n========= VALIDATING MANDATORY FIELDS =========\n");

        for (int i = 0; i < list.size(); i++) {

            Map<String, Object> item = list.get(i);
            List<String> missingFields = new ArrayList<>();

            for (String field : mandatoryFields) {

                if (!item.containsKey(field)
                        || item.get(field) == null
                        || item.get(field).toString().trim().isEmpty()) {

                    missingFields.add(field);
                }
            }

            // ❌ FAIL CASE
            if (!missingFields.isEmpty()) {

                failCount++;

                System.out.println("❌ Property FAILED");
                System.out.println("ID      : " + item.get("id"));
                System.out.println("URL     : " + item.get("url"));
                System.out.println("Missing : " + missingFields);
                System.out.println("--------------------------------------------");

            }
            // ✅ PASS CASE
//            else {
//
                passCount++;
//
//                System.out.println("✅ Property PASSED");
//                System.out.println("ID : " + item.get("id"));
//                System.out.println("All mandatory fields are available");
//                System.out.println("--------------------------------------------");
//            }
        }

        // 📊 FINAL SUMMARY
        System.out.println("\n============= SUMMARY =============");
        System.out.println("Total Records Checked : " + list.size());
        System.out.println("Passed Properties     : " + passCount);
        System.out.println("Failed Properties     : " + failCount);
        System.out.println("==================================");

        Assert.assertEquals(
                failCount,
                0,
                "Some properties have missing mandatory fields!"
        );
    }

    /* ================= VALIDATION : PRICE RANGE (SAFETY RULES) ================= */

    public void validatePriceRange() {

        List<Map<String, Object>> properties = getResultList();

        int failCount = 0;
        int skippedCount = 0;

        System.out.println("========== VALIDATION PRICE RANGE ==========");

        for (Map<String, Object> item : properties) {

            String id  = String.valueOf(item.get("id"));
            String url = String.valueOf(item.get("url"));

            Number sqFtPrice = (Number) item.get("sqFtPrice");
            Number caSqFt    = (Number) item.get("caSqFt");
            Number price     = (Number) item.get("price");

            String iba  = item.get("iba")  != null ? item.get("iba").toString()  : "";
            String cpmp = item.get("cpmp") != null ? item.get("cpmp").toString() : "";
            String pl   = item.get("pl")   != null ? item.get("pl").toString()   : "";

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

            if (sqFtPrice == null || caSqFt == null || price == null) {
                failCount++;
                System.out.println("FAIL – Missing Price Fields");
                System.out.println("ID  : " + id);
                System.out.println("URL : " + url);
                System.out.println("----------------------------------");
                continue;
            }

            double calculatedPrice =
                    sqFtPrice.doubleValue() * caSqFt.doubleValue();

            double lowerLimit = calculatedPrice * 0.98;
            double upperLimit = calculatedPrice * 1.02;

            double actualPrice = price.doubleValue();

            if (actualPrice < lowerLimit || actualPrice > upperLimit) {

                failCount++;

                System.out.println("❌ PRICE OUT OF RANGE");
                System.out.println("ID            : " + id);
                System.out.println("URL           : " + url);
                System.out.println("Calculated    : " + calculatedPrice);
                System.out.println("Allowed Range : " + lowerLimit + " - " + upperLimit);
                System.out.println("Actual Price  : " + actualPrice);
                System.out.println("----------------------------------");
            }
        }

        System.out.println("============================================");
        System.out.println("Skipped Records : " + skippedCount);
        System.out.println("Failed Records  : " + failCount);
        System.out.println("============================================");

        Assert.assertEquals(
                failCount,
                0,
                "Price range validation failed!"
        );
    }

    /* ================= VALIDATION : FORMATTED PRICE ================= */

    public void validatePriceFormatted() {

        List<Number> priceList =
                response.jsonPath().getList("resultList.price", Number.class);

        List<String> priceDList =
                response.jsonPath().getList("resultList.priceD");

        int failCount = 0;
        int skippedCount = 0;

        System.out.println("========== VALIDATION FORMATTED PRICE ==========");

        for (int i = 0; i < priceList.size(); i++) {

            if (priceDList.get(i) == null || priceDList.get(i).isEmpty()) {
                skippedCount++;
                continue;
            }

            double price = priceList.get(i).doubleValue();
            double priceDValue =
                    Support.convertPriceD(priceDList.get(i));

            if (priceDValue < price * 0.99
                    || priceDValue > price * 1.01) {

                failCount++;
                System.out.println(
                        " Failed at index " + i +
                        " | Price: " + price +
                        " | PriceD: " + priceDList.get(i) +
                        " | Converted: " + priceDValue
                );
            }
        }

        System.out.println("============================================");
        System.out.println("Failed Records  : " + failCount);
        System.out.println("Skipped Records : " + skippedCount);
        System.out.println("============================================");

        Assert.assertEquals(
                failCount,
                0,
                "Formatted price validation failed!"
        );
    }
}