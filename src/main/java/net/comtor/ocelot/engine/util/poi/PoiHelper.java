package net.comtor.ocelot.engine.util.poi;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Guido A. Cafiel V.
 */
public class PoiHelper {

    private static void addCell(Row row, int i, String obj) {
        addCell(row, i, obj, null);
    }

    private static void addCell(Row row, int i, String obj, CellStyle style) {
        if (obj == null) {
            return;
        }

        Cell cell = row.createCell(i);

        if (isNumber(obj)) {
            if (obj.startsWith("0") && obj.length() > 1) {
                cell.setCellValue(obj.toString());
                cell.setCellType(Cell.CELL_TYPE_STRING);
            } else {
                cell.setCellValue(Double.parseDouble(obj));
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            }
        } else {
            cell.setCellValue(obj);
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }

        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    private static boolean validateTypeNumeric(String type) {
        if (type == null) {
            return false;
        }

        if (type.equalsIgnoreCase("INT") || type.equalsIgnoreCase("DOUBLE")) {
            return true;
        }

        return false;
    }

    private static void addCell(Row row, int i, Object obj, Object type, CellStyle style) {
        if (obj == null) {
            return;
        }

        Cell cell = row.createCell(i);

        if (validateTypeNumeric(type.toString())) {
            try {
                cell.setCellValue(Double.parseDouble(obj.toString()));
            } catch (Exception ex) {
                cell.setCellValue(obj.toString());
            }

            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        } else {
            cell.setCellValue(obj.toString());
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }

        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    public static void writeRow(Sheet sheet, int irow, int startColumn, CellStyle format, LinkedList<String> strs) {
        int counter = startColumn;
        Row row = sheet.createRow(irow);

        for (String string : strs) {
            addCell(row, counter, string, format);
            counter++;
        }
    }

    public static void writeRow(Sheet sheet, int irow, int startColumn, CellStyle format, LinkedList<Object> objects, LinkedList<Object> columnTypes) {
        int counter = startColumn;
        Row row = sheet.createRow(irow);
        int i = 0;

        for (Object object : objects) {
            addCell(row, counter, object, columnTypes.get(i), format);
            counter++;
            i++;
        }
    }

    /**
     * Le va la madre al que use esta funcion para sacar valores de una hoja de excel en un cargue.
     *
     * Para eso use las otras funciones que sacan el valor correcto.
     *
     * @param cell
     * @return
     * @deprecated
     */
    public static String getValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        int type = cell.getCellType();

        if (type == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        }

        if ((type == Cell.CELL_TYPE_STRING)) {
            return String.valueOf(cell.getStringCellValue());
        }

        if ((type == Cell.CELL_TYPE_NUMERIC)) {
            return String.valueOf(cell.getNumericCellValue());
        }

        return cell.toString();
    }

    /**
     *
     * @param cell
     * @param defaultStringValue
     * @return
     */
    public static String getStringValue(Cell cell, String defaultStringValue) {
        return (cell == null) ? null : getStringValueByType(cell, cell.getCellType(), defaultStringValue);
    }

    private static String getStringValueByType(Cell cell, int resultType, String defaultStringValue) {
        switch (resultType) {
            case Cell.CELL_TYPE_BLANK: {
                return defaultStringValue;
            }

            case Cell.CELL_TYPE_BOOLEAN: {
                return String.valueOf(cell.getBooleanCellValue());
            }

            case Cell.CELL_TYPE_ERROR: {
                return FormulaError.forInt(cell.getErrorCellValue()).getString();
            }

            case Cell.CELL_TYPE_FORMULA: {
                return getStringValueByType(cell, cell.getCachedFormulaResultType(), defaultStringValue);
            }

            case Cell.CELL_TYPE_NUMERIC: {
                double value = cell.getNumericCellValue();

                if (Math.floor(value) == value) {
                    return "" + (long) value;
                }

                NumberFormat nf2 = NumberFormat.getInstance();
                nf2.setMaximumFractionDigits(4);
                nf2.setMinimumFractionDigits(0);
                nf2.setGroupingUsed(false);

                return nf2.format(value);
            }

            case Cell.CELL_TYPE_STRING: {
                return String.valueOf(cell.getStringCellValue());
            }

            default: {
                return cell.toString();
            }
        }
    }

    public static String getStringValue(Cell cell) {
        return getStringValue(cell, "");
    }

    public static int getIntValue(Cell cell) {
        if (cell == null) {
            return 0;
        }

        int type = cell.getCellType();

        if (type == Cell.CELL_TYPE_BOOLEAN) {
            boolean booleanValue = cell.getBooleanCellValue();

            return booleanValue ? 1 : 0;
        }

        if (type == Cell.CELL_TYPE_STRING) {
            String str = cell.getStringCellValue();

            if (isNumber(str)) {
                return Integer.parseInt(str);
            }

            return 0;
        }

        if (type == Cell.CELL_TYPE_NUMERIC) {
            double value = cell.getNumericCellValue();

            return (int) value;
        }

        if ((type == Cell.CELL_TYPE_BLANK)) {
            return 0;
        }

        return 0;
    }

    public static long getLongValue(Cell cell) {
        if (cell == null) {
            return 0;
        }

        int type = cell.getCellType();

        if (type == Cell.CELL_TYPE_BOOLEAN) {
            boolean booleanValue = cell.getBooleanCellValue();

            return booleanValue ? 1 : 0;
        }

        if (type == Cell.CELL_TYPE_STRING) {
            String str = cell.getStringCellValue();

            if (isNumber(str)) {
                return Long.parseLong(str);
            }

            return 0;
        }

        if (type == Cell.CELL_TYPE_NUMERIC) {
            double value = cell.getNumericCellValue();

            return (long) value;
        }

        if (type == Cell.CELL_TYPE_BLANK) {
            return 0;
        }

        return 0;
    }

    public static double getDoubleValue(Cell cell) {
        if (cell == null) {
            return 0;
        }

        int type = cell.getCellType();

        if (type == Cell.CELL_TYPE_BOOLEAN) {
            boolean booleanValue = cell.getBooleanCellValue();

            return booleanValue ? 1 : 0;
        }

        if (type == Cell.CELL_TYPE_STRING) {
            String str = cell.getStringCellValue();

            if (isNumber(str)) {
                return Double.parseDouble(str);
            }

            return 0;
        }

        if (type == Cell.CELL_TYPE_NUMERIC) {
            return cell.getNumericCellValue();
        }

        if (type == Cell.CELL_TYPE_BLANK) {
            return 0;
        }

        return 0;
    }

    public static java.sql.Date getDateValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        try {
            return new java.sql.Date(cell.getDateCellValue().getTime());
        } catch (Exception ex) {
            return getDateFromValue(cell.toString());
        }
    }

    public static java.sql.Date getDateFromValue(String value_text) {
        java.util.Date value = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (StringUtils.isNotEmpty(value_text)) {
            try {
                value = dateFormat.parse(value_text);
            } catch (ParseException ex) {
            }
        }

        if (value == null) {
            return null;
        }

        return new java.sql.Date(value.getTime());
    }

    /**
     * @author juandiego@comtor.net
     * @since Sep 2, 2016
     * @param cell
     * @return
     */
    public static Timestamp getTimestampValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        try {
            return new Timestamp(cell.getDateCellValue().getTime());
        } catch (Exception ex) {
            return getTimestampFromValue(cell.toString());
        }

    }

    /**
     * @author juandiego@comtor.net
     * @since Sep 2, 2016
     * @param value
     * @return
     */
    public static Timestamp getTimestampFromValue(String value) {
        long time = 0;

        if (value != null && !value.trim().isEmpty()) {
            try {
                time = Long.parseLong(value);
            } catch (Exception ex) {
            }
        }

        return new Timestamp(time);
    }

    public static void main(String[] args) {
    }

    private static boolean isNumber(String obj) {
        try {
            Double.parseDouble(obj);

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
