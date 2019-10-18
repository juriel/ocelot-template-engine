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

        HtmlThead thead = new HtmlThead();
        HtmlTr tr = new HtmlTr();

        if (numeration) {
            tr.add(new HtmlTh("#"));
        }

        for (String k : headers.keySet()) {
            HtmlTh th = new HtmlTh(headers.get(k));

            tr.addAttribute("role", "row");
            th.addAttribute("endpoint", k);
            //tr.addAttribute(header, header);
            tr.add(th);
        }

        thead.add(tr);
        this.add(thead);
    }

    public void addHeaders(LinkedList<String> headers) {
        this.addClass("table table-striped table-bordered table-hover");

        HtmlThead thead = new HtmlThead();
        HtmlTr tr = new HtmlTr();

        if (numeration) {
            tr.add(new HtmlTh("#"));
        }
        for (String header : headers) {
            HtmlTh th = new HtmlTh(header);

            tr.addAttribute("role", "row");
            tr.addAttribute(header, header);
            tr.add(th);
        }

        thead.add(tr);
        this.add(thead);
    }

    public void addHeaders(String... headers) {
        this.addClass("table table-striped table-bordered table-hover");

        HtmlThead thead = new HtmlThead();
        HtmlTr tr = new HtmlTr();

        if (numeration) {
            tr.add(new HtmlTh("#"));
        }
        for (String header : headers) {
            HtmlTh th = new HtmlTh(header);

            tr.addAttribute("role", "row");
            tr.addAttribute(header, header);
            tr.add(th);
        }

        thead.add(tr);
        this.add(thead);
    }

    public void addElement(HtmlObject obj) {
        HtmlTr tr = new HtmlTr();
        if (numeration) {
            HtmlTd td = new HtmlTd((noRows++) + "");
            td.addAttribute("scope", "row");
            tr.add(td);
        }
        tr.add(new HtmlTd(obj));
        this.add(tr);
    }

    public void addRowElements(Object... objects) {
        addRow(Arrays.asList(objects));
    }

    public void addRow(List<Object> listRow) {
        HtmlTr tr = new HtmlTr();
        if (numeration) {
            HtmlTd td = new HtmlTd((noRows++) + "");
            td.addAttribute("scope", "row");
            tr.add(td);
        }
        for (Object object : listRow) {
            HtmlTd tdd = new HtmlTd(object + "");
            if (object instanceof Number) {
                tdd.setStyle("text-align:right");
            }
            tr.add(tdd);
        }
        this.add(tr);
    }

    public void addRowTableDataElement(List<TableDataElement> elements) {
        HtmlTr tr = new HtmlTr();
        if (numeration) {
            HtmlTd td = new HtmlTd((noRows++) + "");
            td.addAttribute("scope", "row");
            tr.add(td);
        }
        for (TableDataElement dataElement : elements) {
            HtmlTd tdd = new HtmlTd(dataElement.getInsertObject() + "");
            if (dataElement.getOriginalObject() instanceof Number) {
                tdd.setStyle("text-align:right");
            }
            tr.add(tdd);
        }
        this.add(tr);
    }

    public void addRowData(LinkedList<Object> listRow, LinkedList<HtmlObject> listOptions) {
        HtmlTr tr = new HtmlTr();
        if (numeration) {
            HtmlTd td = new HtmlTd((noRows++) + "");
            td.addAttribute("scope", "row");
            tr.add(td);
        }
        for (Object object : listRow) {
            HtmlTd tdd = new HtmlTd(object + "");
            tr.add(tdd);
        }

        if (!listOptions.isEmpty()) {
            HtmlTd optionsTd = new HtmlTd();

            for (HtmlObject listOption : listOptions) {
                optionsTd.add(listOption);
            }
            optionsTd.setStyle("text-align:right");
            tr.add(optionsTd);
        }

        this.add(tr);
    }

    public void addRow(LinkedList<String> listRow, HtmlTr tr) {
        HtmlTd td = new HtmlTd();
        if (numeration) {
            td.addData((noRows++) + "");
            td.addAttribute("scope", "row");
            tr.add(td);
        }
        for (String object : listRow) {
            HtmlTd tdd = new HtmlTd(object);
            tr.add(tdd);
        }
        this.add(tr);
    }

}
