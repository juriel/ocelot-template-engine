package net.comtor.ocelot.engine.view.advanced.reports;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.comtor.ocelot.bootstrap.commons.BColor;
import net.comtor.ocelot.bootstrap.components.alerts.BAlertInfo;
import net.comtor.ocelot.bootstrap.components.alerts.BAlertSuccess;
import net.comtor.ocelot.bootstrap.forms.inputs.BInputText;
import net.comtor.ocelot.engine.commons.MapResponse;
import net.comtor.ocelot.engine.commons.tables.AdvancedTable;
import net.comtor.ocelot.engine.commons.tables.TableDataElement;
import net.comtor.ocelot.engine.components.forms.buttons.PostButton;
import net.comtor.ocelot.engine.components.forms.buttons.advanced.DownloadButton;
import net.comtor.ocelot.engine.components.forms.forms.AjaxForm;
import net.comtor.ocelot.engine.util.icons.FontAwesome;
import net.comtor.ocelot.engine.view.simple.SimpleView;
import net.comtor.ocelot.html.HtmlContainer;
import net.comtor.ocelot.html.HtmlObject;
import net.comtor.ocelot.html.HtmlTag;
import net.comtor.ocelot.html.basic.HtmlH4;
import net.comtor.ocelot.html.basic.HtmlHr;
import net.comtor.ocelot.html.styles.HtmlDiv;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Guido Cafiel
 */
public abstract class ReportView extends SimpleView {

    private static final Logger LOG = Logger.getLogger(ReportView.class.getName());

    public static final int PREVIEW_AND_DOWNLOAD = 1;
    public static final int PREVIEW = 2;
    public static final int DOWNLOAD = 3;

    private OcelotPreparedQuery preparedQuery;

    protected abstract DataSource getDataSource();

    protected abstract OcelotPreparedQuery getPreparedQuery(HttpServletRequest request);

    private String filterFormName = "form-" + System.currentTimeMillis();

    protected AjaxForm getFiltersForm() {
        AjaxForm form = new AjaxForm(filterFormName);
        form.addSubmitWithIntro();

        addFilters(form);

        form.add(getSendFiltersButton(filterFormName));

        return form;
    }

    protected PostButton getSendFiltersButton(String formName) {
        PostButton sendFilters = new PostButton(BColor.PRIMARY, "Consultar",
                formName, getControllerName() + "/filter", false);

        return sendFilters;
    }

    protected void addFilters(AjaxForm form) {
        BInputText filter = new BInputText("filter", "Filtro");
        filter.required();
        form.add(filter);
    }

    @Override
    protected void getViewContent(HttpServletRequest request, HtmlContainer container) {
        container.add(getFiltersForm());
        container.add(new HtmlHr());
        container.add(new HtmlH4(getTableTitle()));
        HtmlDiv div = getDivResults();
        div.add(new BAlertInfo("Use los filtros para generar el reporte"));
        container.add(div);
    }

    protected HtmlDiv getDivResults() {
        HtmlDiv div = new HtmlDiv("table_result");
        div.addClass("report-result");
        return div;
    }

    protected String getTableTitle() {
        return "Resultados del reporte";
    }

    protected boolean downloadInXlsx() {
        return false;
    }

    @Override
    protected List<HtmlTag> addToHeader() {
        List<HtmlTag> list = new LinkedList<>();

        if (downloadInXlsx()) {
            HtmlDiv contentDiv = new HtmlDiv("donwload_button_div");
            list.add(contentDiv);
        }

        return list;
    }

    public HtmlTag getDownloadButton(HttpServletRequest request)
            throws UnsupportedEncodingException {
        Map<String, String[]> map = request.getParameterMap();

        String params = "?";

        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue()[0];
            params += key + "=" + value + "&";
        }

        String urlEndpoint = getControllerName() + "/xlsx" + params;
        DownloadButton downloadXlsx = new DownloadButton(
                urlEndpoint,
                "Descargar XLSX",
                BColor.SUCCESS,
                FontAwesome.Solid.FILE_EXCEL);

        return downloadXlsx;
    }

    @ResponseBody
    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    protected List<MapResponse> getTableResult(HttpServletRequest request) throws SQLException {
        List<MapResponse> response = new LinkedList<>();
        preparedQuery = getPreparedQuery(request);
        ResultSet rs = getResultSetFromQuery();

        try {
            response.add(new MapResponse(OCELOT_DEFAULT_ALERT,
                    new BAlertSuccess("Se ha realizado la consulta de forma exitosa.").getHtml()));
            response.add(new MapResponse("table_result", getResults(rs).getHtml()));
            response.add(new MapResponse("donwload_button_div", getDownloadButton(request).getHtml()));
        } catch (UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }

        return response;
    }

    @ResponseBody
    @RequestMapping("/xlsx")
    protected void downloadXlsx(HttpServletRequest request, HttpServletResponse response)
            throws SQLException {
        preparedQuery = getPreparedQuery(request);

        ResultSet rs = getResultSetFromQuery();
        String fileName = "report" + System.currentTimeMillis() + ".xlsx";

        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);

            XSSFWorkbook workbook = new WorkbookGenerator().generateFromResultSet(rs);
            workbook.write(response.getOutputStream());

            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    protected HtmlObject getResults(ResultSet resultSet) throws SQLException {
        HtmlContainer container = new HtmlContainer();
        container.add(getTableResult(resultSet));
        return container;
    }

    protected AdvancedTable getTableResult(ResultSet resultSet) throws SQLException {
        AdvancedTable table = new AdvancedTable(false);
        table.addHeaders(getColumnNames(resultSet));

        while (resultSet.next()) {
            List<TableDataElement> rowData = new LinkedList<>();

            for (String index : getIndex(resultSet)) {
                TableDataElement tableDataElement = getValueFromResultSet(resultSet, index);
                rowData.add(tableDataElement);
            }

            table.addRowTableDataElement(rowData);
        }

        return table;
    }

    protected TableDataElement getValueFromResultSet(ResultSet resultSet, String index) throws SQLException {
        return new TableDataElement(resultSet.getObject(index), resultSet.getObject(index));
    }

    private List<String> getIndex(ResultSet resultSet) throws SQLException {
        List<String> index = new LinkedList<>();
        ResultSetMetaData metadata = resultSet.getMetaData();

        for (int i = 1; i <= metadata.getColumnCount(); i++) {
            index.add(metadata.getColumnName(i));
        }

        return index;
    }

    public String print(Object[] objects) {
        String pri = "";

        for (Object obj : objects) {
            pri += obj + ", ";
        }

        return pri;
    }

    public String getValueFromMap(String[] filter) {
        if (filter != null) {
            return filter[0] == null ? "" : filter[0];
        }

        return "";
    }

    protected ResultSet getResultSetFromQuery() throws SQLException {
        Connection conn = getDataSource().getConnection();
        PreparedStatement ps = conn.prepareStatement(preparedQuery.getSql(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        fillPreparedStatement(ps);

        return ps.executeQuery();
    }

    private void fillPreparedStatement(PreparedStatement ps) throws SQLException {
        int index = 1;

        for (Object object : preparedQuery.getParameters()) {
            if (object == null) {
                ps.setNull(index, Types.NULL);

                return;
            }

            Class<?> clazz = object.getClass();

            if (clazz.equals(String.class)) {
                ps.setString(index++, "" + object);
            } else if (clazz.equals(short.class) || clazz.equals(Short.class)) {
                ps.setShort(index++, ((Short) object));
            } else if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
                ps.setInt(index++, ((Integer) object));
            } else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
                ps.setLong(index++, ((Long) object));
            } else if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
                ps.setBoolean(index++, ((Boolean) object));
            } else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
                ps.setDouble(index++, ((Double) object));
            } else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
                ps.setDouble(index++, ((Float) object));
            } else if (clazz.equals(char.class) || clazz.equals(Character.class)) {
                ps.setDouble(index++, ((Character) object));
            } else if (clazz.equals(java.sql.Date.class)) {
                java.sql.Date myDate = (java.sql.Date) object;
                ps.setDate(index, myDate);
            } else if (clazz.equals(Date.class)) {
                Date myDate = (Date) object;
                ps.setDate(index, new java.sql.Date(myDate.getTime()));
            } else if (clazz.equals(Timestamp.class)) {
                ps.setTimestamp(index, (Timestamp) object);
            } else if (clazz.equals(Time.class)) {
                ps.setTime(index, (Time) object);
            } else if (clazz.equals(byte[].class)) {
                ps.setBytes(index, (byte[]) object);
            } else if (clazz.equals(BigDecimal.class)) {
                ps.setBigDecimal(index, (BigDecimal) object);
            } else {
                ps.setObject(index, object);
            }
        }
    }

    private LinkedList<String> getColumnNames(ResultSet resultSet) throws SQLException {
        LinkedList<String> columnNames = new LinkedList<>();
        ResultSetMetaData metadata = resultSet.getMetaData();

        for (int i = 1; i <= metadata.getColumnCount(); i++) {
            columnNames.add(metadata.getColumnName(i));
        }

        return columnNames;
    }

}
