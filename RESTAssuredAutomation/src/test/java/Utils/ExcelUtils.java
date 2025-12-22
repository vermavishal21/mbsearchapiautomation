package Utils;

import org.apache.poi.ss.usermodel.*;
import java.io.InputStream;
import java.util.*;

public class ExcelUtils {

    public static List<Map<String, String>> getSearchData() {

        List<Map<String, String>> dataList = new ArrayList<>();

        try {
            InputStream is = ExcelUtils.class
                    .getClassLoader()
                    .getResourceAsStream("SearchData.xlsx");

            if (is == null) {
                throw new RuntimeException("❌ SearchData.xlsx not found in src/test/resources");
            }

            Workbook workbook = WorkbookFactory.create(is);

            // 🔥 FIX: take FIRST sheet automatically
            Sheet sheet = workbook.getSheetAt(0);

            Row header = sheet.getRow(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new HashMap<>();

                for (int j = 0; j < header.getLastCellNum(); j++) {
                    String key = header.getCell(j).getStringCellValue().trim();
                    String value = row.getCell(j).toString().trim();
                    rowData.put(key, value);
                }

                // Only rows with Run = Y
                if ("Y".equalsIgnoreCase(rowData.get("Run"))) {
                    dataList.add(rowData);
                }
            }

            workbook.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return dataList;
    }
}
