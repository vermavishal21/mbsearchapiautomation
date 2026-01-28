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

        response = APIUtils.get(
                "/mbsrp/propertySearch.html",
                PayloadBuilder.buildPayload(excelData)
        );

        response.then().statusCode(200);
    }

    /* ================= COMMON RESULT LIST ================= */
    
    
    public List<Map<String, Object>> getResultListSafe() {

        List<Map<String, Object>> resultList =
                response.jsonPath().getList("resultList");

        if (resultList == null || resultList.isEmpty()) {
            System.out.println("⚠️ No data returned for payload");
            return new ArrayList<>();
        }
        return resultList;
    }
    

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

    
    private String getPropertyTypeName(String propertyTypeCode) {

        switch (propertyTypeCode) {
            case "10000": return "Residential Plot";
            case "10001": return "Residential House";
            case "10002": return "Multistorey Apartment";
            case "10003": return "Builder Floor Apartment";
            case "10004": return "Farm House";
            case "10005": return "Agricultural Land";
            case "10006": return "Commercial Land";
            case "10007": return "Commercial Office Space";
            case "10008": return "Commercial Shop";
            case "10009": return "Commercial Showroom";
            case "10010": return "Business Centre";
            case "10011": return "Warehouse/ Godown";
            case "10012": return "Industrial Land";
            case "10013": return "Industrial Building";
            case "10014": return "Industrial Shed";
            case "10015": return "Kiosk";
            case "10016": return "Hotel Sites";
            case "10017": return "Villa";
            case "10018": return "Office in IT Park/ SEZ";
            case "10020": return "Service Apartment";
            case "10021": return "Penthouse";
            case "10022": return "Studio Apartment";
            default: return "UNKNOWN";
        }
    }
    
    

    /* ================= COMMON ID HANDLER ================= */

    private String getRecordId(Map<String, Object> item) {
        // Builder listing → psmid
        if (item.containsKey("psmid") && item.get("psmid") != null) {
            return item.get("psmid").toString();
        }
        // Default → id
        return item.get("id") != null ? item.get("id").toString() : "NA";
    }

    /* ================= PRINT PROPERTY COUNT ================= */

    public void printAllPropertyIds() {

        List<Map<String, Object>> list = getResultListSafe();

        System.out.println("===============================");
        System.out.println("Test Case 1: Verify API Status");

        if (list.isEmpty()) {
            System.out.println("Status Code : " + response.getStatusCode());
            System.out.println("No property IDs found");
            System.out.println("Total Property Count : 0");
            System.out.println("===============================\n\n");
            return;
        }

        // 🔹 PRINT IDs (psmid for builder, id otherwise)
        for (Map<String, Object> item : list) {
            String recordId = getRecordId(item);
            //System.out.println("Record ID : " + recordId);
        }

        System.out.println("-------------------------------");
        System.out.println("Total Property Count : " + list.size());
        System.out.println("===============================\n\n");
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

        System.out.println("==================================================");
        System.out.println("Test Case 2: Verify Mandatory Field Exist");
        System.out.println("========= VALIDATING MANDATORY FIELDS =========");

        for (Map<String, Object> item : list) {

            List<String> missingFields = new ArrayList<>();

            for (String field : mandatoryFields) {
                if (!item.containsKey(field)
                        || item.get(field) == null
                        || item.get(field).toString().trim().isEmpty()) {
                    missingFields.add(field);
                }
            }

            if (!missingFields.isEmpty()) {
                failCount++;
                System.out.println("❌ Property FAILED");
                System.out.println("ID      : " + getRecordId(item));
                System.out.println("URL     : " + item.get("url"));
                System.out.println("Missing : " + missingFields);
                System.out.println("--------------------------------------------");
            } else {
                passCount++;
            }
        }

        System.out.println("\n============= SUMMARY =============");
        System.out.println("Total Records Checked : " + list.size());
        System.out.println("Passed Properties     : " + passCount);
        System.out.println("Failed Properties     : " + failCount);
        System.out.println("==================================\n\n");

        Assert.assertEquals(failCount, 0,
                "Some properties have missing mandatory fields!");
    }

    /* ================= VALIDATION : PRICE RANGE ================= */

    public void validatePriceRange() {

        List<Map<String, Object>> properties = getResultList();

        int totalCount   = properties.size();
        int failCount    = 0;
        int skippedCount = 0;
        int passCount    = 0;

        System.out.println("==================================================");
        System.out.println("Test Case 3: Verify Price Range");
        System.out.println("========== VALIDATION PRICE RANGE ==========");

        for (Map<String, Object> item : properties) {

            String id  = getRecordId(item);
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
                System.out.println("=> skipValidation Price Fields");
                System.out.println("ID  : " + id);
                System.out.println("URL : " + url);
                continue;
                
            }

            if (sqFtPrice == null || caSqFt == null || price == null) {
                failCount++;
                System.out.println("❌ Missing Price Fields");
                System.out.println("ID  : " + id);
                System.out.println("URL : " + url);
                continue;
            }

            double calculatedPrice = sqFtPrice.doubleValue() * caSqFt.doubleValue();
            double actualPrice     = price.doubleValue();

            if (actualPrice < calculatedPrice * 0.98
                    || actualPrice > calculatedPrice * 1.02) {

                failCount++;
                System.out.println("❌ PRICE OUT OF RANGE");
                System.out.println("ID : " + id);
                System.out.println("URL: " + url);
            } else {
                passCount++;
            }
        }

        System.out.println("============================================");
        System.out.println("Total Records   : " + totalCount);
        System.out.println("Passed Records  : " + passCount);
        System.out.println("Skipped Records : " + skippedCount);
        System.out.println("Failed Records  : " + failCount);
        System.out.println("============================================\n\n");

        Assert.assertEquals(failCount, 0,
                "Price range validation failed!");
    }

    /* ================= VALIDATION : FORMATTED PRICE ================= */

    public void validatePriceFormatted() {

        List<Number> priceList =
                response.jsonPath().getList("resultList.price", Number.class);
        List<String> priceDList =
                response.jsonPath().getList("resultList.priceD");

        int totalCount   = priceList.size();
        int failCount    = 0;
        int skippedCount = 0;
        int passCount    = 0;

        System.out.println("==================================================");
        System.out.println("Test Case 4: Verify Price Formatted");
        System.out.println("========== VALIDATION FORMATTED PRICE ==========");

        for (int i = 0; i < priceList.size(); i++) {

            if (priceDList.get(i) == null || priceDList.get(i).isEmpty()) {
                skippedCount++;
                continue;
            }

            double price = priceList.get(i).doubleValue();
            double priceDValue = Support.convertPriceD(priceDList.get(i));

            if (priceDValue < price * 0.99 || priceDValue > price * 1.01) {
                failCount++;
            } else {
                passCount++;
            }
        }

        System.out.println("============================================");
        System.out.println("Total Records   : " + totalCount);
        System.out.println("Passed Records  : " + passCount);
        System.out.println("Failed Records  : " + failCount);
        System.out.println("Skipped Records : " + skippedCount);
        System.out.println("============================================\n\n");

        Assert.assertEquals(failCount, 0,
                "Formatted price validation failed!");
    }

    /* ================= VALIDATION : USER TYPE ================= */

    public void validateUserTypeAccordingToSearch(String expectedUserType) {

        List<Map<String, Object>> properties = getResultList();

        int totalCount = properties.size();
        int passCount  = 0;
        int failCount  = 0;

        System.out.println("========== VALIDATING USER TYPE ==========");
        System.out.println("Search Input (inputListings) : " + expectedUserType);

        String expectedResponseUserType;

        if ("A".equalsIgnoreCase(expectedUserType)) {
            expectedResponseUserType = "AGENT";
        } else if ("I".equalsIgnoreCase(expectedUserType)) {
            expectedResponseUserType = "OWNER";
        } else if ("B".equalsIgnoreCase(expectedUserType)) {
            expectedResponseUserType = "BUILDER";
        } else {
            System.out.println("Unsupported user type");
            return;
        }

        for (Map<String, Object> item : properties) {

            String id  = getRecordId(item);
            String url = String.valueOf(item.get("url"));

            String actualUserType = item.get("userType") != null
                    ? item.get("userType").toString().trim()
                    : "";

            if (expectedResponseUserType.equalsIgnoreCase(actualUserType)) {
                passCount++;
            } else {
                failCount++;
                System.out.println("❌ USER TYPE MISMATCH");
                System.out.println("ID       : " + id);
                System.out.println("URL      : " + url);
                System.out.println("Expected : " + expectedResponseUserType);
                System.out.println("Actual   : " + actualUserType);
                System.out.println("----------------------------------");
            }
        }

        System.out.println("==========================================");
        System.out.println("Total Records  : " + totalCount);
        System.out.println("Passed Records : " + passCount);
        System.out.println("Failed Records : " + failCount);
        System.out.println("==========================================");

        Assert.assertEquals(failCount, 0,
                "User type validation failed!");
    }
    
    
    /* ================= VALIDATION : PROPERTY TYPE (CODE BASED) ================= */

    public void validatePropertyTypeAccordingToSearch(String expectedPropertyTypeCode) {

        List<Map<String, Object>> properties = getResultList();

        int totalCount = properties.size();
        int passCount  = 0;
        int failCount  = 0;

        System.out.println("========== VALIDATING PROPERTY TYPE (CODE) ==========");
        System.out.println("Search Input Property Type Code : " + expectedPropertyTypeCode);

        for (Map<String, Object> item : properties) {

            String id  = getRecordId(item);
            String url = String.valueOf(item.get("url"));

            // propertyType coming as code like 10002 / 10003
            String actualPropertyTypeCode = item.get("propertyType") != null
                    ? item.get("propertyType").toString().trim()
                    : "";

            if (expectedPropertyTypeCode.equals(actualPropertyTypeCode)) {
                passCount++;
            } else {
                failCount++;
                System.out.println("❌ PROPERTY TYPE CODE MISMATCH");
                System.out.println("ID       : " + id);
                System.out.println("URL      : " + url);
                System.out.println("Expected : " + expectedPropertyTypeCode);
                System.out.println("Actual   : " + actualPropertyTypeCode);
                System.out.println("----------------------------------");
            }
        }

        System.out.println("==========================================");
        System.out.println("Total Records  : " + totalCount);
        System.out.println("Passed Records : " + passCount);
        System.out.println("Failed Records : " + failCount);
        System.out.println("==========================================\n");

        Assert.assertEquals(failCount, 0,
                "Property type code validation failed!");
    }

    
    
    
}
