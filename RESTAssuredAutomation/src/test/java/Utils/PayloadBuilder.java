package Utils;

import java.util.HashMap;
import java.util.Map;

public class PayloadBuilder {

    public static Map<String, Object> searchPayloadFromExcel(Map<String, String> data) {

        Map<String, Object> payload = new HashMap<>();

        payload.put("editSearch", "Y");
        payload.put("category", "S");

        // ✅ SAFE numeric parsing from Excel
        payload.put("city", (int) Double.parseDouble(data.get("City")));
        payload.put("budgetMin", (long) Double.parseDouble(data.get("BudgetMin")));
        payload.put("budgetMax", (long) Double.parseDouble(data.get("BudgetMax")));

        payload.put("propertyType", data.get("propertyType"));
        payload.put("inputListings", data.get("inputListings"));

        payload.put("page", 1);
        payload.put("sortBy", "premiumRecent");
        payload.put("postedSince", "-1");
        payload.put("multiLang", "en");
        payload.put("showCnt", 1000);

        return payload;
    }
}
