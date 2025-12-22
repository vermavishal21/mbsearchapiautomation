package Utils;

import java.util.HashMap;
import java.util.Map;

public class PayloadBuilder {

    public static Map<String, Object> fullSearchPayload() {

        Map<String, Object> payload = new HashMap<>();

        payload.put("editSearch", "Y");
        payload.put("category", "S");
        payload.put("propertyType", "10002,10003,10021,10022,10001,10017");
        payload.put("budgetMin", "500000");
        payload.put("budgetMax", "50000000");
        payload.put("bedrooms", "11701,11702");
        payload.put("city", 6403);  //6403
        payload.put("inputListings","A");  //User Type A - Agent || I - Owner / Buyer || B - Builder
        payload.put("page", 1);
        payload.put("sortBy", "premiumRecent");
        payload.put("postedSince", "-1");
        payload.put("pType", "10002,10003,10021,10022,10001,10017");
        payload.put("isNRI", "N");
        payload.put("multiLang", "en");
        payload.put("skipPrimeshuffle", "Y");
        //payload.put("cpmp", "Y");
        payload.put("excludeIds", "76975109,77256747,81448461,81702337,81341885,81952521,82015865,81895991,81996775,81853005,59755319,80107799,80998685,82199433,81945975,82129377,81808825,81381883,81722165,78604331,80171499,81797319,78931811,77079771,81764191,79967069,63310763,81162499,80693307,78108109");
        payload.put("showCnt", 1000);

        return payload;
    }
    
    
    
//    public static Map<String, Object> buildSearchPayload(Map<String, String> excelData) {
//
//        Map<String, Object> payload = new HashMap<>();
//
//        payload.put("editSearch", "Y");
//        payload.put("category", "S");
//        payload.put("propertyType", excelData.get("PropertyType"));
//        payload.put("budgetMin", excelData.get("BudgetMin"));
//        payload.put("budgetMax", excelData.get("BudgetMax"));
//        payload.put("bedrooms", excelData.get("Bedrooms"));
//        payload.put("city", Integer.parseInt(excelData.get("City")));
//        payload.put("inputListings","A");
//        payload.put("page", 1);
//        payload.put("sortBy", "premiumRecent");
//        payload.put("postedSince", "-1");
//        payload.put("pType", excelData.get("PropertyType"));
//        payload.put("isNRI", "N");
//        payload.put("multiLang", "en");
//        payload.put("skipPrimeshuffle", "Y");
//        payload.put("showCnt", 1000);
//
//        return payload;
//    }
    
    
    
    
    
    
    
}




//https://www.magicbricks.com/mbsrp/propertySearch.html?editSearch=Y&category=S&propertyType=10002,10003,10021,10022,10001,10017&bedrooms=11701,11702&city=6146&page=2&groupstart=30&offset=0&maxOffset=52&sortBy=premiumRecent&postedSince=-1&pType=10002,10003,10021,10022,10001,10017&isNRI=N&multiLang=en



//https://www.magicbricks.com/mbsrp/propertySearch.html?editSearch=Y&category=S&propertyType=10002,10003,10021,10022,10001,10017&budgetMin=5000000&budgetMax=6000000&bedrooms=11702&city=6403&inputListings=A&page=1&sortBy=premiumRecent&postedSince=-1&pType=10002,10003,10021,10022,10001,10017&isNRI=N&multiLang=en

