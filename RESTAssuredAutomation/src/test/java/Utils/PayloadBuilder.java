package Utils;

import java.util.HashMap;
import java.util.Map;

public class PayloadBuilder {

    /* ================= UTILITY METHODS ================= */

    // 🔧 Clean numeric values like 1000000.0 → 1000000
    private static String cleanNumber(String value) {
        if (value == null) return null;

        if (value.endsWith(".0")) {
            return value.substring(0, value.length() - 2);
        }
        return value;
    }

    // 🔧 Clean comma-separated values: "10002, 10003" → "10002,10003"
    private static String cleanCommaSeparated(String value) {
        if (value == null) return null;
        return value.replaceAll("\\s+", "");
    }

    /* ================= PAYLOAD BUILDER ================= */

    public static Map<String, Object> buildPayload(Map<String, String> excelData) {

        Map<String, Object> payload = new HashMap<>();

        /* 🔥 FIXED MANDATORY PARAMS */
        payload.put("editSearch", "Y");
        payload.put("page", 1);
        payload.put("sortBy", "premiumRecent");
        payload.put("postedSince", "-1");
        payload.put("isNRI", "N");
        payload.put("multiLang", "en");

        /* 🔥 DYNAMIC PARAMS (CLEANED) */

        payload.put("category", excelData.get("category"));
        payload.put("city", cleanNumber(excelData.get("city")));
        payload.put("inputListings", excelData.get("inputListings"));
        payload.put("showCnt", cleanNumber(excelData.get("showCnt")));

        payload.put("budgetMin", cleanNumber(excelData.get("budgetMin")));
        payload.put("budgetMax", cleanNumber(excelData.get("budgetMax")));

        payload.put(
            "bedrooms",
            cleanCommaSeparated(cleanNumber(excelData.get("bedrooms")))
        );

        //payload.put("propertyType", cleanCommaSeparated(cleanNumber(excelData.get("propertyType"))));

       

        // DO NOT SEND pType
         payload.put("pType", cleanCommaSeparated(cleanNumber(excelData.get("pType"))));

        /* DEBUG (keep for now, remove later) */
        //System.out.println("FINAL PAYLOAD => " + payload);

        return payload;
    }
}





//https://www.magicbricks.com/mbsrp/propertySearch.html?editSearch=Y&category=S&propertyType=10002,10003,10021,10022,10001,10017&bedrooms=11701,11702&city=6146&page=2&groupstart=30&offset=0&maxOffset=52&sortBy=premiumRecent&postedSince=-1&pType=10002,10003,10021,10022,10001,10017&isNRI=N&multiLang=en



//https://www.magicbricks.com/mbsrp/propertySearch.html?editSearch=Y&category=S&propertyType=10002,10003,10021,10022,10001,10017&budgetMin=5000000&budgetMax=6000000&bedrooms=11702&city=6403&inputListings=A&page=1&sortBy=premiumRecent&postedSince=-1&pType=10002,10003,10021,10022,10001,10017&isNRI=N&multiLang=en

