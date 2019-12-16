package net.comtor.ocelot.engine.util.xlsx;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Dec 13, 2019
 */
//@Service
public final class XlsxUtils {
//
////    private XlsxUtils() {
////
////    }

    public XSSFWorkbook getUploadTemplateFile(String[] headers, String sheetName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        addTitles(sheet, headers);

        return workbook;
    }

    public void addTitles(XSSFSheet sheet, String... titles) {
        Row row = sheet.createRow(0);

        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Arial");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.BLACK.index);

        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

        int colNum = 0;

        for (String title : titles) {
            Cell cell = row.createCell(colNum++);
            cell.setCellValue(title);
            cell.setCellStyle(style);
        }
    }

    public void addRow(XSSFSheet sheet, int index, Object... content) {
        Row row = sheet.createRow(index);
        int colNum = 0;

        for (Object cont : content) {
            Cell cell = row.createCell(colNum++);
            cell.setCellValue(cont + "");
        }
    }

    public Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
        }

        return null;
    }

}
