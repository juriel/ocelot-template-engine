package net.comtor.ocelot.engine.view.advanced.upload;

import java.io.Serializable;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Dec 16, 2019
 */
public class XlsxValidatorException implements Serializable {

    private static final long serialVersionUID = 6195905501407265143L;

    private int row;
    private int column;
    private String message;

    public XlsxValidatorException(int row, int column, String message) {
        this.row = row;
        this.column = column;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "XlsxValidatorException{"
                + "row=" + row
                + ", column=" + column
                + ", message=" + message
                + '}';
    }

}
