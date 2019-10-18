/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.comtor.ocelot.engine.view.advanced.reports;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.LinkedList;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Guido Cafiel
 */
public class WorkbookGenerator {

    public XSSFWorkbook generateFromResultSet(ResultSet rs) throws SQLException {

        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet();
        LinkedList<String> index = getColumsIndex(rs);
        addTitles(sheet, index);
        fillReportData(sheet, index, rs);

        return workbook;
    }

    private void addTitles(XSSFSheet sheet, LinkedList<String> index) throws SQLException {

        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Arial");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.BLACK.index);

        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

        XSSFRow row = sheet.createRow(0);
        int i = 0;
        for (String string : index) {
            sheet.autoSizeColumn(i);
            XSSFCell cell = row.createCell(i++);
            cell.setCellValue(string);
            cell.setCellStyle(style);
        }

    }

    private LinkedList<String> getColumsIndex(ResultSet rs) throws SQLException {
        LinkedList<String> index = new LinkedList<>();
        ResultSetMetaData metadata = rs.getMetaData();
        for (int i = 1; i <= metadata.getColumnCount(); i++) {
            index.add(metadata.getColumnName(i));
        }
        return index;
    }

    private void fillReportData(XSSFSheet sheet, LinkedList<String> index, ResultSet rs) throws SQLException {
        int rowIndex = 1;

        ResultSetMetaData metadata = rs.getMetaData();
        int y = 0;
        while (rs.next()) {

            XSSFRow row = sheet.createRow(rowIndex++);

            int i = 0;
            for (String ind : index) {
                XSSFCell cell = row.createCell(i++);
                setCellValue(i, cell, rs, ind);
                addFormat(y, i, cell, rs, ind);
                cell.setCellType(getCellType(metadata.getColumnType(i)));

            }
            y++;
        }
    }

    private void setCellValue(int pos, XSSFCell cell, ResultSet rs, String index) throws SQLException {
        int sqlType = rs.getMetaData().getColumnType(pos);
        XSSFCreationHelper createHelper = cell.getSheet().getWorkbook().getCreationHelper();
        XSSFCellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();
        switch (sqlType) {
            case Types.TINYINT:
            case Types.BIGINT:
            case Types.INTEGER:
            case Types.SMALLINT:
                cell.setCellValue(rs.getInt(index));
                return;
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                cell.setCellValue(rs.getDouble(index));
                return;
            case Types.DATE:

                java.sql.Date myDate = rs.getDate(index);
                if (myDate != null) {
                    cell.setCellValue(new Date(myDate.getTime()));
                }
                cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
                cell.setCellStyle(cellStyle);

                return;

            case Types.TIME:
            case Types.TIMESTAMP:

                Timestamp miTimestamp = rs.getTimestamp(index);
                if (miTimestamp != null) {
                    cell.setCellValue(new Date(miTimestamp.getTime()));
                }
                cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
                cell.setCellStyle(cellStyle);

                return;

            case Types.LONGVARCHAR:
            case Types.CHAR:
            case Types.VARCHAR:
                cell.setCellValue(rs.getString(index));
                return;
        }

    }

    private int getCellType(int sqlType) {
        switch (sqlType) {
            case Types.TINYINT:
            case Types.BIGINT:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                return XSSFCell.CELL_TYPE_NUMERIC;
            case Types.LONGVARCHAR:
            case Types.CHAR:
            case Types.VARCHAR:
                return XSSFCell.CELL_TYPE_STRING;

        }
        return XSSFCell.CELL_TYPE_BLANK;
    }

    private void addFormat(int row, int column, XSSFCell cell, ResultSet rs, String columName) {

    }

}
