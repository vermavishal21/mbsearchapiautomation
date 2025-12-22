//package Utils;
//
//import org.apache.poi.ss.usermodel.*;
//import java.io.FileInputStream;
//import java.util.*;
//
//public class ExcelUtils {
//
//    public static List<Map<String, String>> getData(String filePath) {
//
//        List<Map<String, String>> list = new ArrayList<>();
//
//        try (FileInputStream fis = new FileInputStream(filePath)) {
//
//            Workbook wb = WorkbookFactory.create(fis);
//            Sheet sheet = wb.getSheetAt(0);
//
//            Row header = sheet.getRow(0);
//
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//
//                Row row = sheet.getRow(i);
//                if (row == null) continue;
//
//                if (!row.getCell(0).getStringCellValue().equalsIgnoreCase("Y"))
//                    continue;
//
//                Map<String, String> data = new HashMap<>();
//
//                for (int j = 0; j < header.getLastCellNum(); j++) {
//                    data.put(
//                        header.getCell(j).getStringCellValue(),
//                        row.getCell(j).toString()
//                    );
//                }
//
//                list.add(data);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//}