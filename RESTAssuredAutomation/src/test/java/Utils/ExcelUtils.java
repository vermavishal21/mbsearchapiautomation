package Utils;

import java.io.InputStream;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import java.io.IOException;

public class ExcelUtils {

    public static List<Map<String, String>> getExcelData(String fileName, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        try {
            InputStream is = ExcelUtils.class.getClassLoader().getResourceAsStream(fileName);
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Map<String, String> dataMap = new HashMap<>();

                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    String key = headerRow.getCell(j).getStringCellValue();
                    Cell cell = row.getCell(j);
                    String value = cell == null ? "" : cell.toString();
                    dataMap.put(key, value);
                }
                dataList.add(dataMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }
}
