package net.comtor.ocelot.engine.commons.tables;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import net.comtor.ocelot.html.HtmlObject;
import net.comtor.ocelot.html.tables.HtmlTable;
import net.comtor.ocelot.html.tables.HtmlTd;
import net.comtor.ocelot.html.tables.HtmlTh;
import net.comtor.ocelot.html.tables.HtmlThead;
import net.comtor.ocelot.html.tables.HtmlTr;

/**
 *
 * @author Guido Cafiel
 */
public class AdvancedTable extends HtmlTable {

    private int noRows = 1;
    private boolean numeration;

    public AdvancedTable(boolean withNumeration) {
        this.numeration = withNumeration;
    }

    public AdvancedTable() {
        this.numeration = false;
    }

    public void addHeaders(LinkedHashMap<String, String> headers) {
        this.addClass("table table-striped table-bordered table-hover");

        HtmlThead tableHead = new HtmlThead();
        HtmlTr tableRow = new HtmlTr();

        if (numeration) {
            tableRow.add(new HtmlTh("#"));
        }

        for (String key : headers.keySet()) {
            HtmlTh tableHeader = new HtmlTh(headers.get(key));
            tableHeader.addAttribute("endpoint", key);

            tableRow.addAttribute("role", "row");
            tableRow.add(tableHeader);
        }

        tableHead.add(tableRow);

        this.add(tableHead);
    }

    public void addHeaders(LinkedList<String> headers) {
        this.addClass("table table-striped table-bordered table-hover");

        HtmlThead tableHead = new HtmlThead();
        HtmlTr tableRow = new HtmlTr();

        if (numeration) {
            tableRow.add(new HtmlTh("#"));
        }

        headers.stream().forEach((header) -> {
            HtmlTh tableHeader = new HtmlTh(header);

            tableRow.addAttribute("role", "row");
            tableRow.addAttribute(header, header);
            tableRow.add(tableHeader);
        });

        tableHead.add(tableRow);

        this.add(tableHead);
    }

    @Override
    public HtmlTable addHeaders(List<String> headers) {
        this.addClass("table table-striped table-bordered table-hover");

        HtmlThead tableHead = new HtmlThead();
        HtmlTr tableRow = new HtmlTr();

        if (numeration) {
            tableRow.add(new HtmlTh("#"));
        }

        for (String header : headers) {
            HtmlTh tableHeader = new HtmlTh(header);
            tableRow.addAttribute("role", "row");
            tableRow.addAttribute(header, header);

            tableRow.add(tableHeader);
        }

        tableHead.add(tableRow);
        
        this.add(tableHead);

        return this;
    }

    public void addElement(HtmlObject obj) {
        HtmlTr tableRow = new HtmlTr();

        if (numeration) {
            HtmlTd tableData = new HtmlTd((noRows++) + "");
            tableData.addAttribute("scope", "row");

            tableRow.add(tableData);
        }

        tableRow.add(new HtmlTd(obj));

        this.add(tableRow);
    }

    public void addRowElements(Object... objects) {
        addRow(Arrays.asList(objects));
    }

    public void addRow(List<Object> listRow) {
        HtmlTr tableRow = new HtmlTr();

        if (numeration) {
            HtmlTd tableData = new HtmlTd((noRows++) + "");
            tableData.addAttribute("scope", "row");

            tableRow.add(tableData);
        }

        for (Object object : listRow) {
            HtmlTd tableData;

            if (object instanceof HtmlObject) {
                HtmlObject o = (HtmlObject) object;

                tableData = new HtmlTd(o);
            } else {
                tableData = new HtmlTd(object + "");

                if (object instanceof Number) {
                    tableData.setStyle("text-align: right");
                }
            }

            tableRow.add(tableData);
        }

        this.add(tableRow);
    }

    public void addRowTableDataElement(List<TableDataElement> elements) {
        HtmlTr tableRow = new HtmlTr();

        if (numeration) {
            HtmlTd tableData = new HtmlTd((noRows++) + "");
            tableData.addAttribute("scope", "row");

            tableRow.add(tableData);
        }

        for (TableDataElement dataElement : elements) {
            HtmlTd tableData = new HtmlTd(dataElement.getInsertObject() + "");

            if (dataElement.getOriginalObject() instanceof Number) {
                tableData.setStyle("text-align: right");
            }

            tableRow.add(tableData);
        }

        this.add(tableRow);
    }

    public void addRowData(LinkedList<Object> listRow, LinkedList<HtmlObject> listOptions) {
        HtmlTr tableRow = new HtmlTr();

        if (numeration) {
            HtmlTd tableData = new HtmlTd((noRows++) + "");
            tableData.addAttribute("scope", "row");

            tableRow.add(tableData);
        }

        for (Object object : listRow) {
            HtmlTd tableData = new HtmlTd(object + "");
            tableRow.add(tableData);
        }

        if (!listOptions.isEmpty()) {
            HtmlTd optionsCell = new HtmlTd();

            for (HtmlObject listOption : listOptions) {
                optionsCell.add(listOption);
            }

            optionsCell.setStyle("text-align:right");
            tableRow.add(optionsCell);
        }

        this.add(tableRow);
    }

    public void addRow(LinkedList<String> objects, HtmlTr row) {
        HtmlTd tableData = new HtmlTd();

        if (numeration) {
            tableData.addEscapedText((noRows++) + "");
            tableData.addAttribute("scope", "row");

            row.add(tableData);
        }

        for (String object : objects) {
            row.add(new HtmlTd(object));
        }

        this.add(row);
    }

}
