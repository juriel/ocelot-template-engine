package net.comtor.ocelot.engine.view.finder;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.comtor.ocelot.bootstrap.components.alerts.BAlertInfo;
import net.comtor.ocelot.engine.commons.MapResponse;
import net.comtor.ocelot.engine.commons.paginators.PaginatorBar;
import net.comtor.ocelot.engine.commons.tables.AdvancedTable;
import net.comtor.ocelot.engine.components.forms.FinderLauncher;
import net.comtor.ocelot.engine.components.forms.buttons.GetButton;
import net.comtor.ocelot.engine.components.forms.buttons.PostButton;
import net.comtor.ocelot.engine.components.forms.forms.AjaxForm;
import net.comtor.ocelot.engine.persistence.BusinessService;
import net.comtor.ocelot.bootstrap.forms.buttons.BButton;
import net.comtor.ocelot.bootstrap.forms.buttons.BButtonStyle;
import net.comtor.ocelot.bootstrap.forms.inputs.BInputText;
import net.comtor.ocelot.engine.util.icons.FontAwesome;
import net.comtor.ocelot.html.HtmlContainer;
import net.comtor.ocelot.html.HtmlObject;
import net.comtor.ocelot.html.basic.HtmlP;
import net.comtor.ocelot.html.forms.inputs.HtmlInputHidden;
import net.comtor.ocelot.html.styles.HtmlDiv;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Guido A. Cafiel Vellojin
 * @param <E>
 */
public abstract class Finder<E> {

    private static final int FINDER_ROWS_PER_PAGE = 5;
    private static final String FINDER_FORM = "finder_form";
    private static final String FINDER_QUERY_RESULT = "finderQueryResult";
    private static final String FINDER_TABLE_RESULT = "finderTableResult";
    private static final String FINDER_TITLE = "finder-title";
    private static final String FINDER_BODY = "ocelot_modal_body";
    private String defaultFinderId;
    private String selectCallBack = "";

    private Object idParent;

    public String getFinderName() {
        RequestMapping requestMapping = getClass().getAnnotation(RequestMapping.class);

        return requestMapping.value()[0].replace("/", "");
    }

    protected abstract String getFinderTitle();

    protected String getFinderId() {
        return defaultFinderId;
    }

    protected abstract String getVisible(E entity);

    protected abstract String getHidden(E entity);

    protected abstract BusinessService getBusinessFacade();

    protected abstract void getTableTitles(LinkedList<String> titles);

    protected abstract void getRow(LinkedList<Object> data, E entity);

    protected String getEntityController() {
        return null;
    }

    protected String getSubTitle() {
        return null;
    }

    protected String getUrlParams(HttpServletRequest request) {
        String params = "?";

        Map<String, String[]> map = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue()[0];
            params += String.format("%1$s=%2$s&", key, value);
        }

        return params;
    }

    protected HtmlContainer getFormParams(HttpServletRequest request) {
        HtmlContainer container = new HtmlContainer();
        Map<String, String[]> map = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue()[0];

            container.add(new HtmlInputHidden(key, value));
        }

        return container;
    }

    protected LinkedList<HtmlObject> getFilters() {
        LinkedList<HtmlObject> filters = new LinkedList<>();

        BInputText defaultFilter = new BInputText("", "filter", getDefaultFilterMessage());
        defaultFilter.addAttribute("placeholder", "Buscar...");
        defaultFilter.onKeyPress("return pulsar(event)");

        filters.add(defaultFilter);

        return filters;
    }

    protected int getNumRowsToView() {
        return FINDER_ROWS_PER_PAGE;
    }

    protected Page<E> getQueryResult(Map<String, String[]> filterValues, Pageable pageable) {
        if (filterValues == null) {
            filterValues = getDefaultFilter();
        }

        return getBusinessFacade().getWithFiltersForFinder(filterValues, pageable);
    }

    protected Map<String, String[]> getDefaultFilter() {
        Map<String, String[]> filters = new LinkedHashMap<>();
        String[] defFilter = {""};
        filters.put("filter", defFilter);

        return filters;
    }

    LinkedList<String> getTableTitles() {
        LinkedList<String> titles = new LinkedList<>();
        getTableTitles(titles);

        return titles;
    }

    protected void getRowOptions(LinkedList<HtmlObject> optionsList, E entity) {
        BButton button = new BButton(BButtonStyle.PRIMARY, "");
        button.setIcon(FontAwesome.Solid.CHECK);
        button.addAttribute("title", "Seleccionar");
        String visible = StringUtils.replace(getVisible(entity), "'", "\\'");
        String onclick = String.format("addValuesFinder('%1$s','%2$s','%3$s');", getFinderId(), visible, getHidden(entity));
        button.onClick(onclick);
        button.addAttribute("type", "button");
        button.addAttribute("data-dismiss", "modal");

        optionsList.add(button);
    }

    private long getNumIndexOnPaginator() {
        return 5;
    }

    private HtmlContainer getTableResult(HttpServletRequest request, Page<E> page, int actualPage) {
        HtmlContainer container = new HtmlContainer();

        if (page.getContent().isEmpty()) {
            HtmlContainer divConten = new HtmlContainer();

            if (getEntityController() != null) {
                GetButton goToNew = new GetButton(BButtonStyle.PRIMARY, "Crear", getEntityController() + "/new");
                goToNew.addAttribute("data-dismiss", "modal");
                divConten.add(goToNew);
            }

            BAlertInfo alert = new BAlertInfo("No se encontraron resultados para la búsqueda.");
            alert.addAttribute("finder-alert", "true");
            container.add(alert);

            return container;
        }

        AdvancedTable table = new AdvancedTable();
        table.addHeaders(getTableTitles());

        for (E entity : page.getContent()) {
            LinkedList<Object> rowData = new LinkedList<>();
            getRow(rowData, entity);
            LinkedList<HtmlObject> optionsList = new LinkedList<>();
            getRowOptions(optionsList, entity);
            table.addRowData(rowData, optionsList);
        }

        HtmlDiv results = new HtmlDiv(FINDER_QUERY_RESULT);
        results.setStyle("overflow: auto");
        results.add(table);

        String urlEndpoint = getFinderName() + "/search";

        PaginatorBar paginator = new PaginatorBar(getNumIndexOnPaginator(), FINDER_FORM, urlEndpoint, page.getTotalPages(), page.getTotalElements(),
                actualPage, page.getContent().size(), getNumRowsToView());
        paginator.setUrlParams(getUrlParams(request));
        results.add(paginator);

        container.add(results);

        return container;
    }

    @ResponseBody
    @RequestMapping("/{default_id}")
    public List<MapResponse> mainView(@PathVariable("default_id") String defaultId, HttpServletRequest request) {
        return getMainView(defaultId, request);
    }

    @ResponseBody
    @RequestMapping("/{default_id}/{parent_id}")
    public List<MapResponse> mainViewWithParentId(@PathVariable("default_id") String defaultId, @PathVariable("parent_id") Object parentId, HttpServletRequest request) {
        this.idParent = parentId;

        return getMainView(defaultId, request);
    }

    protected void processRequest(HttpServletRequest request) {

    }

    private List<MapResponse> getMainView(String defaultId, HttpServletRequest request) {
        processRequest(request);
        this.defaultFinderId = defaultId;
        HtmlContainer container = new HtmlContainer();

        if (getSubTitle() != null) {
            container.add(new HtmlP(getSubTitle()));
        }

        LinkedList<HtmlObject> filters = getFilters();

        AjaxForm finderForm = new AjaxForm(FINDER_FORM);
        finderForm.add(getFormParams(request));
        finderForm.addAttribute("validate-intro", "true");

        filters.stream().forEach((filter) -> finderForm.add(filter));

        PostButton searchButton = new PostButton(BButtonStyle.PRIMARY, "", FINDER_FORM, getFinderName() + "/search/0" + getUrlParams(request), false);
        searchButton.setIconClass(FontAwesome.Solid.SEARCH);

        finderForm.add(searchButton);

        container.add(finderForm);

        HtmlDiv tableResult = new HtmlDiv(FINDER_TABLE_RESULT);
        tableResult.addClass("my-4");

        Page<E> queryResult = getQueryResult(request);

        tableResult.add(getTableResult(request, queryResult, 0));
        container.add(tableResult);

        List<MapResponse> options = new LinkedList<>();
        options.add(new MapResponse(FINDER_TITLE, getFinderTitle()));
        options.add(new MapResponse(FINDER_BODY, container.getHtml()));

        return options;
    }

    public Page<E> getQueryResult(HttpServletRequest request) {
        PageRequest page = new PageRequest(0, getNumRowsToView());
        Map<String, String[]> filtersMap = null;

        if (getDefaultFilter() == null) {
            filtersMap = request.getParameterMap();
            String[] filterValue = {""};
            filtersMap.put("filter", filterValue);
        } else {
            filtersMap = getDefaultFilter();
        }

        return getBusinessFacade().getWithFiltersForFinder(filtersMap, page);
    }

    @ResponseBody
    @RequestMapping(value = "/search/{pag}", method = RequestMethod.POST)
    public List<MapResponse> searchWithFilters(@PathVariable Integer pag, HttpServletRequest request) {
        PageRequest pageRequest = new PageRequest(pag, getNumRowsToView());

        Page<E> page = getQueryResult(request.getParameterMap(), pageRequest);

        List<MapResponse> options = new LinkedList<>();
        options.add(new MapResponse(FINDER_TITLE, getFinderTitle()));
        options.add(new MapResponse(FINDER_TABLE_RESULT, getTableResult(request, page, pag).getHtml()));

        return options;
    }

    protected String getDefaultFilterMessage() {
        return null;
    }

    public Object getParentId() {
        return this.idParent;
    }

    public FinderLauncher getFinderLauncher(String label, String showValue, String nameAndId, String hiddenValue) {
        FinderLauncher finderLauncher = new FinderLauncher(label, showValue, nameAndId, hiddenValue, getFinderName());
        finderLauncher.setSelectCallBack(selectCallBack);
        return finderLauncher;
    }

    public FinderLauncher getFinderLauncher(String label, String nameAndId) {
        FinderLauncher finderLauncher = new FinderLauncher(label, nameAndId, getFinderName());
        finderLauncher.setSelectCallBack(selectCallBack);
        return finderLauncher;
    }

    public String getSelectCallBack() {
        return selectCallBack;
    }

    public void setSelectCallBack(String selectCallBack) {
        this.selectCallBack = selectCallBack;
    }

}
