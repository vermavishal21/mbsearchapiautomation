package DataProvider;

import java.util.List;
import java.util.Map;
import org.testng.annotations.DataProvider;
import Utils.ExcelUtils;

public class SearchDataProvider {

    @DataProvider(name = "searchPayloadData")
    public static Object[][] getData() {
        List<Map<String, String>> dataList = ExcelUtils.getExcelData("SearchPayload.xlsx", "Sheet1");
        Object[][] data = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i);
        }
        return data;
    }
}
