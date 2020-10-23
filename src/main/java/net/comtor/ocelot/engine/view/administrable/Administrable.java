package net.comtor.ocelot.engine.view.administrable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import net.comtor.ocelot.engine.commons.OcelotReflectionHelper;
import net.comtor.ocelot.engine.commons.MapResponse;
import net.comtor.ocelot.engine.components.forms.buttons.AjaxButton;
import net.comtor.ocelot.engine.components.forms.buttons.GetButton;
import net.comtor.ocelot.engine.components.forms.buttons.PostButton;
import net.comtor.ocelot.engine.components.forms.buttons.advanced.SearchButton;
import net.comtor.ocelot.engine.components.forms.forms.advanced.SearchForm;
import net.comtor.ocelot.engine.commons.paginators.PaginatorBar;
import net.comtor.ocelot.engine.commons.tables.AdvancedTable;
import net.comtor.ocelot.engine.exceptions.OcelotException;
import net.comtor.ocelot.engine.persistence.BusinessService;
import net.comtor.ocelot.engine.security.OcelotSecurityManager;
import net.comtor.ocelot.bootstrap.components.alerts.BAlertDanger;
import net.comtor.ocelot.bootstrap.components.alerts.BAlertInfo;
import net.comtor.ocelot.bootstrap.components.alerts.BAlertSuccess;
import net.comtor.ocelot.bootstrap.components.cards.BCard;
import net.comtor.ocelot.bootstrap.forms.BShowField;
import net.comtor.ocelot.bootstrap.forms.buttons.BButtonStyle;
import net.comtor.ocelot.bootstrap.forms.inputs.BInputText;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import net.comtor.ocelot.engine.components.forms.buttons.OcelotButton;
import net.comtor.ocelot.engine.components.forms.buttons.advanced.DeleteButton;
import net.comtor.ocelot.engine.components.forms.forms.AjaxForm;
import net.comtor.ocelot.engine.util.icons.FontAwesome;
import net.comtor.ocelot.engine.view.administrable.advanced.HtmlFormHelper;
import net.comtor.ocelot.html.HtmlContainer;
import net.comtor.ocelot.html.HtmlObject;
import net.comtor.ocelot.html.IHtmlTag;
import net.comtor.ocelot.html.basic.HtmlBr;
import net.comtor.ocelot.html.basic.HtmlH1;
import net.comtor.ocelot.html.basic.HtmlH4;
import net.comtor.ocelot.html.formatting.HtmlSmall;
import net.comtor.ocelot.html.links.HtmlUl;
import net.comtor.ocelot.html.programing.HtmlScript;
import net.comtor.ocelot.html.styles.HtmlDiv;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public abstract class Administrable<E, ID extends Serializable> {

    private static final Logger LOG = Logger.getLogger(Administrable.class.getName());

    public static final String QUERY_FORM = "query_form";

    protected static final String OCELOT_DEFAULT_ALERT = "ocelot-default-alert";
    protected static final String OCELOT_HIDDEN_ALERT = "ocelot-hidden-alert";
    protected static final String OCELOT_TABLE_RESULT = "tableResult";

    private static final String WARNING_MESSAGE_CLASS = "war-message";

    public abstract String getControllerName();

    protected abstract BusinessService getBusinessService();

    protected abstract ID getId(E entity);

    protected String getIconClass() {
        return null;
    }

    protected abstract String getTitle();

    protected String getSubTitle() {
        return null;
    }

    protected abstract String getDetailTitle();

    protected abstract String getNewTitle();

    protected abstract String getEditTitle();

    protected abstract String getCreatePrivilege();

    protected abstract String getEditPrivilege();

    protected abstract String getDeletePrivilege();

    protected abstract String getViewPrivilege();

    protected abstract void addTableTitles(LinkedHashMap<String, String> titles);

    protected abstract void getRow(LinkedList<Object> data, E entity);

    protected abstract void getNewAndEditForm(AjaxForm form, E entity);

    protected abstract E getEntity(Map<String, String[]> values, E entity);

    protected abstract List<MapResponse> getDefaultResponse();

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(getDefaultDateFormat());
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    protected String getDefaultDateFormat() {
        return "yyyy-MM-dd";
    }

    protected String getContentId() {
        return "content";
    }

    protected void cleanValues(LinkedList<Object> values) {
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) == null || values.get(i).equals("null")) {
                values.set(i, "");
            }
        }
    }

    @ResponseBody
    @RequestMapping("")
    public List<MapResponse> mainView(HttpServletRequest request) {
        processRequest(request);

        return mainView(request, getContentId());
    }

    public List<MapResponse> mainView(HttpServletRequest request, String idDivContent) {
        HtmlContainer container = new HtmlContainer();
        BCard card = new BCard(getIconClass(), getTitle());

        for (AjaxButton ajaxButton : getButtons(request)) {
            card.addToHeader(ajaxButton);
        }

        card.addToBody(new HtmlDiv().setId(OCELOT_DEFAULT_ALERT));

        if (getSubTitle() != null) {
            card.addToBody(new HtmlSmall(getSubTitle()));
        }

        SearchForm form = getFiltersForm();

        card.addToBody(form);

        HtmlDiv tableResult = new HtmlDiv(OCELOT_TABLE_RESULT);
        Page<E> queryResult = getQueryResult();
        tableResult.add(getTableResult(request, queryResult, 0));

        card.addToBody(tableResult);

        container.add(card);

        if (addJsFile() != null) {
            String filePath = addJsFile();
            HtmlScript script = new HtmlScript();
            script.setSrc(filePath);
            container.add(script);
        }

        List<MapResponse> options = getDefaultResponse();
        options.add(new MapResponse(idDivContent, container.getHtml()));

        return options;
    }

    protected SearchForm getFiltersForm() {
        LinkedList<HtmlObject> filters = new LinkedList<>();
        getFilters(filters);
        SearchForm form = new SearchForm(QUERY_FORM);
        form.addClass("ajaxForm");

        for (HtmlObject filter : filters) {
            form.add(filter);
        }

        SearchButton searchButton = new SearchButton(getSearchTitle(), QUERY_FORM, getControllerName() + "/search/0", false);
        form.add(searchButton);

        return form;
    }

    protected HtmlObject getTitleTag() {
        return new HtmlH1(getTitle());
    }

    @ResponseBody
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public List<MapResponse> toNew(HttpServletRequest request) {
        processRequest(request);
        List<MapResponse> options = getDefaultResponse();

        if (!OcelotSecurityManager.isAuthorized(getCreatePrivilege())) {
            BAlertDanger error = new BAlertDanger(getNoPrivilegesAlertTitle());
            options.add(new MapResponse("ocelot-hidden-alert", error.getHtml()));

            return options;
        }

        String formName = getControllerName() + "_" + System.currentTimeMillis();

        HtmlContainer container = new HtmlContainer();
        BCard card = new BCard(getIconClass(), getNewTitle());

        OcelotButton backButton = new GetButton(BButtonStyle.WARNING, getBackButtonTitle(), getBackPathOnNewForm());
        backButton.setIconClass(FontAwesome.Solid.ARROW_ALT_CIRCLE_LEFT);
        backButton.setStyle("float: right");

        if (!disableBackButton()) {
            card.addToHeader(backButton);
        }

        card.addToBody(new HtmlDiv(OCELOT_DEFAULT_ALERT));

        if (getNewSubTitle() != null) {
            card.addToBody(new HtmlSmall(getNewSubTitle()));
        }

        AjaxForm form = new AjaxForm(formName);
        getNewAndEditForm(form, null);

        String urlEndpoint = getToNewPath();

        if (haveFiles()) {
            urlEndpoint = getControllerName() + "/newMultipart";
            form.addAttribute("enctype", "multipart/form-data");
        }

        form.add(getSaveButton(formName, urlEndpoint));
        card.addToBody(form);

        HtmlContainer afterFormContainer = new HtmlContainer();
        addAfterForm(afterFormContainer);
        card.addToBody(afterFormContainer);

        //Ingreso de administrable a pantalla
        container.add(card);

        if (addJsFile() != null) {
            String filePath = addJsFile();
            HtmlScript script = new HtmlScript();
            script.setSrc(filePath);
            container.add(script);
        }

        options.add(new MapResponse(getContentId(), container.getHtml()));

        return options;
    }

    protected HtmlObject getSaveButton(String formName, String urlEndpoint) {
        PostButton saveButton = new PostButton(BButtonStyle.PRIMARY, getSaveButtonTitle(), formName, urlEndpoint, resetNewForm());
        saveButton.setIconClass(FontAwesome.Solid.DATABASE);

        if (addToOnclickSaveButton() != null) {
            saveButton.addAttribute("onclick", saveButton.getAttribute("onclick") + addToOnclickSaveButton());
        }

        return saveButton;
    }

    protected boolean resetNewForm() {
        return true;
    }

    @ResponseBody
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public List<MapResponse> createProcess(HttpServletRequest request, E entity) {
        processRequest(request);
        Map<String, String[]> valuesMap = request.getParameterMap();
        List<MapResponse> options = getDefaultResponse();

        if (!OcelotSecurityManager.isAuthorized(getCreatePrivilege())) {
            BAlertDanger error = new BAlertDanger(getNoPrivilegesAlertTitle());
            options.add(new MapResponse("galileo-hidden-alert", error.getHtml()));

            return options;
        }

        try {
            create(valuesMap, entity);

            BAlertSuccess success = new BAlertSuccess(getSavedSuccessMessage(entity));
            options.add(new MapResponse(OCELOT_DEFAULT_ALERT, success.getHtml()));
        } catch (OcelotException ex) {
            BAlertDanger error = new BAlertDanger(ex.getMessage() == null ? "Hay errores en algunos campos." : ex.getMessage());
            options.add(new MapResponse(OCELOT_DEFAULT_ALERT, error.getHtml()));
            options.add(new MapResponse("ocelot-error-no-reset", ""));

            if (!ex.getErrors().isEmpty()) {
                ex.getErrors().entrySet().forEach((e) -> {
                    String key = e.getKey();
                    HtmlUl errors = new HtmlUl();
                    e.getValue().forEach(x -> errors.addLi(x));

                    if (!errors.getContentObjects().isEmpty()) {
                        options.add(new MapResponse(key + "_errors", errors.getHtml()));
                    }
                });
            }
        }

        return options;
    }

    @ResponseBody
    @RequestMapping(value = "/newMultipart", method = RequestMethod.POST)
    public List<MapResponse> createProcessMultipart(MultipartHttpServletRequest multipartRequest, HttpServletRequest request) {
        processRequest(request);

        Map<String, String[]> valuesMap = multipartRequest.getParameterMap();
        List<MapResponse> options = getDefaultResponse();

        if (!OcelotSecurityManager.isAuthorized(getCreatePrivilege())) {
            BAlertDanger error = new BAlertDanger(getNoPrivilegesAlertTitle());
            options.add(new MapResponse("galileo-hidden-alert", error.getHtml()));

            return options;
        }

        try {
            createMultipart(multipartRequest, valuesMap);

            BAlertSuccess success = new BAlertSuccess(getMultipartSavedSuccessMessage(valuesMap));
            options.add(new MapResponse(OCELOT_DEFAULT_ALERT, success.getHtml()));
        } catch (OcelotException ex) {
            BAlertDanger error = new BAlertDanger(ex.getMessage());
            options.add(new MapResponse(OCELOT_DEFAULT_ALERT, error.getHtml()));
        }

        return options;
    }

    @ResponseBody
    @RequestMapping(value = "/editMultipart", method = RequestMethod.POST)
    public List<MapResponse> editProcessMultipart(MultipartHttpServletRequest multipartRequest, HttpServletRequest request) {
        processRequest(request);
        Map<String, String[]> valuesMap = multipartRequest.getParameterMap();
        List<MapResponse> options = getDefaultResponse();

        if (!OcelotSecurityManager.isAuthorized(getEditPrivilege())) {
            BAlertDanger error = new BAlertDanger(getNoPrivilegesAlertTitle());
            options.add(new MapResponse("galileo-hidden-alert", error.getHtml()));

            return options;
        }

        try {
            editMultipart(multipartRequest, valuesMap);

            BAlertSuccess success = new BAlertSuccess(getMultipartEditedSuccessMessage(valuesMap));
            options.add(new MapResponse(OCELOT_DEFAULT_ALERT, success.getHtml()));
        } catch (OcelotException ex) {
            BAlertDanger error = new BAlertDanger(ex.getMessage());
            options.add(new MapResponse(OCELOT_DEFAULT_ALERT, error.getHtml()));
        }

        return options;
    }

    protected void create(Map<String, String[]> valuesMap, E entity) throws OcelotException {
        Object[] details = getDetails(valuesMap);
        createWithDetails(valuesMap, entity, details);
    }

    protected void createMultipart(MultipartHttpServletRequest multipartRequest, Map<String, String[]> valuesMap) throws OcelotException {
    }

    protected void editMultipart(MultipartHttpServletRequest multipartRequest, Map<String, String[]> valuesMap) throws OcelotException {
    }

    protected void createWithDetails(Map<String, String[]> valuesMap, E entity, Object[] details) throws OcelotException {
        if (details == null) {
            getBusinessService().create(getEntity(valuesMap, entity));
        } else {
            getBusinessService().create(getEntity(valuesMap, entity), details);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public List<MapResponse> toEdit(@PathVariable ID id, HttpServletRequest request) {
        processRequest(request);
        List<MapResponse> options = getDefaultResponse();

        if (!OcelotSecurityManager.isAuthorized(getEditPrivilege())) {
            BAlertDanger error = new BAlertDanger(getNoPrivilegesAlertTitle());
            options.add(new MapResponse(OCELOT_HIDDEN_ALERT, error.getHtml()));

            return options;
        }

        String formName = getControllerName() + "_" + System.currentTimeMillis();
        String urlEndpoint = toEditSavePath();

        HtmlContainer container = new HtmlContainer();
        BCard card = new BCard(getIconClass(), getEditTitle());

        OcelotButton backButton = new GetButton(BButtonStyle.WARNING, getBackButtonTitle(), getBackPathOnNewForm());
        backButton.setIconClass(FontAwesome.Solid.ARROW_ALT_CIRCLE_LEFT);
        backButton.setStyle("float: right");
        card.addToHeader(backButton);
        card.addToBody(new HtmlDiv(OCELOT_DEFAULT_ALERT));

        AjaxForm form = new AjaxForm(formName);
        E entity = (E) getBusinessService().getOne(id);

        getNewAndEditForm(form, entity);
        fillFormOnEdit(form, entity);

        if (haveFiles()) {
            urlEndpoint = getControllerName() + "/editMultipart";
            form.addAttribute("enctype", "multipart/form-data");
        }

        form.add(getEditButton(formName, urlEndpoint));
        card.addToBody(form);

        //Ingreso de administrable a pantalla
        container.add(card);

        if (addJsFile() != null) {
            String filePath = addJsFile();
            HtmlScript script = new HtmlScript();
            script.setSrc(filePath);
            container.add(script);
        }

        options.add(new MapResponse(getContentId(), container.getHtml()));

        return options;
    }

    protected String toEditSavePath() {
        return getControllerName() + "/edit" + getViewParams();
    }

    protected HtmlObject getEditButton(String formName, String urlEndpoint) {
        PostButton editButton = new PostButton(BButtonStyle.PRIMARY, getSaveButtonTitle(), formName, urlEndpoint, false);
        editButton.setIconClass(FontAwesome.Solid.EDIT);

        return editButton;
    }

    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public List<MapResponse> editProcess(HttpServletRequest request, E entity) {
        processRequest(request);
        Map<String, String[]> valuesMap = request.getParameterMap();

        List<MapResponse> options = getDefaultResponse();

        if (!OcelotSecurityManager.isAuthorized(getEditPrivilege())) {
            BAlertDanger error = new BAlertDanger(getNoPrivilegesAlertTitle());
            options.add(new MapResponse("galileo-hidden-alert", error.getHtml()));

            return options;
        }

        try {
            edit(valuesMap, entity);

            BAlertSuccess success = new BAlertSuccess(getEditedSuccessMessage(entity));
            options.add(new MapResponse(OCELOT_DEFAULT_ALERT, success.getHtml()));
        } catch (OcelotException ex) {
            BAlertDanger error = new BAlertDanger(ex.getMessage());
            options.add(new MapResponse(OCELOT_DEFAULT_ALERT, error.getHtml()));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            BAlertDanger error = new BAlertDanger(ex.getMessage());
            options.add(new MapResponse(OCELOT_DEFAULT_ALERT, error.getHtml()));
        }

        return options;
    }

    protected void edit(Map<String, String[]> valuesMap, E entity) throws OcelotException {
        Object[] details = getDetails(valuesMap);
        editWithDetails(valuesMap, entity, details);
    }

    protected void editWithDetails(Map<String, String[]> valuesMap, E entity, Object[] details) throws OcelotException {
        if (details == null) {
            getBusinessService().edit(getEntity(valuesMap, entity));
        } else {
            getBusinessService().edit(getEntity(valuesMap, entity), details);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/search/{pag}", method = RequestMethod.POST)
    public List<MapResponse> searchWithFilters(@PathVariable Integer pag, HttpServletRequest request) {
        processRequest(request);
        Map<String, String[]> filters = request.getParameterMap();
        PageRequest pageRequest = new PageRequest(pag, getNumRowsToView());
        Page<E> page = getBusinessService().getWithFilters(filters, pageRequest);
        List<MapResponse> options = getDefaultResponse();
        options.add(new MapResponse(OCELOT_TABLE_RESULT, getTableResult(request, page, pag).getHtml()));

        if (page.getContent().isEmpty()) {
            BAlertInfo info = new BAlertInfo(getSearchNoResultsTitle());
            options.add(new MapResponse(OCELOT_DEFAULT_ALERT, info.getHtml()));
        } else {
            options.add(new MapResponse(OCELOT_DEFAULT_ALERT, ""));
        }

        return options;
    }

    @ResponseBody
    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public List<MapResponse> deleteEntity(@PathVariable ID id, HttpServletRequest request) {
        processRequest(request);
        List<MapResponse> options = getDefaultResponse();

        if (!OcelotSecurityManager.isAuthorized(getDeletePrivilege())) {
            BAlertDanger error = new BAlertDanger(getNoPrivilegesAlertTitle());
            options.add(new MapResponse(OCELOT_HIDDEN_ALERT, error.getHtml()));

            return options;
        }

        try {
            E toDelete = (E) getBusinessService().getOne(id);
            getBusinessService().delete(toDelete);
            BAlertSuccess success = new BAlertSuccess(getDeleteSuccesfulTitle());
            options.add(new MapResponse(OCELOT_DEFAULT_ALERT, success.getHtml()));
            options.add(new MapResponse(OCELOT_TABLE_RESULT, getTableResult(request, getQueryResult(), 0).getHtml()));
        } catch (OcelotException ex) {
            BAlertDanger error = new BAlertDanger(ex.getMessage());
            options.add(new MapResponse(OCELOT_DEFAULT_ALERT, error.getHtml()));
        }

        return options;
    }

    @ResponseBody
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public List<MapResponse> toDetail(HttpServletRequest request, @PathVariable ID id) {
        processRequest(request);
        List<MapResponse> options = getDefaultResponse();

        if (!OcelotSecurityManager.isAuthorized(getViewPrivilege())) {
            BAlertDanger error = new BAlertDanger(getNoPrivilegesAlertTitle());
            options.add(new MapResponse("ocelot-hidden-alert", error.getHtml()));

            return options;
        }

        HtmlContainer mainContainer = new HtmlContainer();
        BCard card = new BCard(getIconClass(), getDetailTitle());

        OcelotButton backButton = new GetButton(BButtonStyle.WARNING, getBackButtonTitle(), getBackPathOnDetailView());
        backButton.setIconClass(FontAwesome.Solid.ARROW_ALT_CIRCLE_LEFT);
        backButton.setStyle("float: right");

        if (!disableBackButton()) {
            card.addToHeader(backButton);
        }

        card.addToBody(new HtmlDiv(OCELOT_DEFAULT_ALERT));

        if (getDetailSubTitle() != null) {
            card.addToBody(new HtmlSmall(getDetailSubTitle()));
        }

        E entity = (E) getBusinessService().getOne(id);
        card.addToBody(getDetailView(request, entity));

        //Ingreso de administrable a pantalla
        mainContainer.add(card);

        if (addJsFile() != null) {
            String filePath = addJsFile();
            HtmlScript script = new HtmlScript();
            script.setSrc(filePath);
            mainContainer.add(script);
        }

        options.add(new MapResponse(getContentId(), mainContainer.getHtml()));

        return options;
    }

    @ResponseBody
    @RequestMapping(value = "/active/{id}", method = RequestMethod.GET)
    public List<MapResponse> activeEntity(@PathVariable ID id, HttpServletRequest request) {
        processRequest(request);
        List<MapResponse> options = getDefaultResponse();

        if (!OcelotSecurityManager.isAuthorized(getActivePrivilege())) {
            BAlertDanger error = new BAlertDanger(getNoPrivilegesAlertTitle());
            options.add(new MapResponse("galileo-hidden-alert", error.getHtml()));

            return options;
        }

        try {
            E toActive = (E) getBusinessService().getOne(id);
            getBusinessService().active(toActive);
            BAlertSuccess success = new BAlertSuccess(getActiveSuccesfulTitle());
            options.add(new MapResponse(OCELOT_DEFAULT_ALERT, success.getHtml()));
            options.add(new MapResponse(OCELOT_TABLE_RESULT, getTableResult(request, getQueryResult(), 0).getHtml()));
        } catch (OcelotException ex) {
            BAlertDanger error = new BAlertDanger(ex.getMessage());
            options.add(new MapResponse(OCELOT_DEFAULT_ALERT, error.getHtml()));
        }

        return options;
    }

    protected String getNewSubTitle() {
        return null;
    }

    protected String getDetailSubTitle() {
        return null;
    }

    protected LinkedList<AjaxButton> getButtons(HttpServletRequest request) {
        LinkedList<AjaxButton> buttons = new LinkedList<>();

        addButtonsToBanner(buttons);

        if (OcelotSecurityManager.isAuthorized(getCreatePrivilege()) && !disableNewButton()) {
            buttons.add(getNewButton());
        }

        if (getOriginController() != null) {
            buttons.add(getBackButton(request));
        }

        return buttons;
    }

    protected GetButton getNewButton() {
        GetButton toNewButton = new GetButton(BButtonStyle.INFO, getNewButtonTitle(), getToNewPath());
        toNewButton.setIconClass(FontAwesome.Solid.PLUS_CIRCLE);

        return toNewButton;
    }

    protected String getToNewPath() {
        return getControllerName() + "/new" + getViewParams();
    }

    private String getViewParams() {
        String path = "";
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        addViewParams(params);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            path += key + "=" + val + "&";
        }

        if (!path.trim().isEmpty()) {
            path = "?" + path.substring(0, path.length());
        }

        return path;
    }

    protected void addViewParams(LinkedHashMap<String, String> params) {

    }

    protected String getEndpointFormat(String url) {
        return url + getViewParams();
    }

    protected void getFilters(LinkedList<HtmlObject> filters) {
        BInputText defaultFilter = new BInputText(getFilterTitle(), "filter", getDefaultFilterMessage());
        filters.add(defaultFilter);
    }

    protected void addButtonsToBanner(LinkedList<AjaxButton> buttons) {

    }

    public HtmlObject getTableResult(HttpServletRequest request, Page<E> page, int actualPage) {
        HtmlContainer container = new HtmlContainer();

        if (hasResultTitle()) {
            container.add(new HtmlH4(getResultsTitle()));
        }

        HtmlDiv queryResult = new HtmlDiv("queryResult");
        queryResult.setStyle("overflow: auto");

        AdvancedTable table = new AdvancedTable();
        table.addHeaders(getTableTitles());

        for (E entity : page.getContent()) {
            if (canViewRow(entity)) {
                getTableRowResult(request, entity, table);
            }
        }

        queryResult.add(table);
        container.add(queryResult);
        String urlEndpoint = getControllerName() + "/search";

        if (page.getTotalPages() > 0) {
            HtmlDiv paginatorDiv = new HtmlDiv("paginatorDiv");
            paginatorDiv.add(new PaginatorBar(getNumIndexOnPaginator(), QUERY_FORM,
                    urlEndpoint, page.getTotalPages(), page.getTotalElements(), actualPage,
                    page.getContent().size(), getNumRowsToView(), getMessageResultPaginator(),
                    getMessageNextPaginator(), getMessagePrevious()));
            container.add(paginatorDiv);
        }

        return container;
    }

    protected void getTableRowResult(HttpServletRequest request, E entity, AdvancedTable table) {
        LinkedList<Object> rowData = new LinkedList<>();
        getRow(rowData, entity);
        cleanValues(rowData);
        LinkedList<HtmlObject> optionsList = new LinkedList<>();

        if (hasOptions()) {
            getRowOptions(request, optionsList, entity);
        }

        if (optionsList.isEmpty()) {
            optionsList.add(new HtmlContainer());
        }

        table.addRowData(rowData, optionsList);
    }

    protected LinkedHashMap<String, String> getTableTitles() {
        LinkedHashMap<String, String> titles = new LinkedHashMap<>();
        addTableTitles(titles);

        if (hasOptions()) {
            titles.put("_options_", getOptionTitle());
        }

        return titles;
    }

    protected void getRowOptions(HttpServletRequest request, LinkedList<HtmlObject> optionsList, E entity) {
        addAditionalOptions(optionsList, entity);

        for (HtmlObject htmlObject : optionsList) {
            if (htmlObject instanceof IHtmlTag) {
                IHtmlTag tag = (IHtmlTag) htmlObject;
                tag.setStyle("margin-right: 5px; margin-bottom: 5px;");
            }
        }

        if (OcelotReflectionHelper.haveEnabled(entity)) {
            boolean delete = false;
            boolean active = false;
            boolean edit = false;

            if (OcelotReflectionHelper.getEnabled(entity)) {
                delete = true;
                edit = true;
            } else {
                active = true;
            }

            addDefaultOptions(request, entity, optionsList, edit, delete, active);
        } else {
            addDefaultOptions(request, entity, optionsList, true, true, false);
        }

    }

    protected void addDefaultOptions(HttpServletRequest request, E entity, LinkedList<HtmlObject> optionsList, boolean showEditButton, boolean showDeleteButton,
            boolean showActiveButton) {
        if (OcelotSecurityManager.isAuthorized(getViewPrivilege()) && getDetailView(request, entity) != null) {
            GetButton toDetail = new GetButton(BButtonStyle.INFO, "", getToDetailPath(entity));
            toDetail.setStyle("margin-right: 5px; margin-bottom: 5px;");
            toDetail.setIconClass(FontAwesome.Solid.EYE);
            toDetail.addAttribute("title", getDetailTitle());
            optionsList.add(toDetail);
            addRowOption(optionsList, entity);
        }

        if (OcelotSecurityManager.isAuthorized(getEditPrivilege()) && showEditButton && editable(entity)) {
            AjaxButton edit = new GetButton(BButtonStyle.SUCCESS, null, getToEditPath(entity));
            edit.setStyle("margin-right: 5px; margin-bottom: 5px;");
            edit.setIconClass(FontAwesome.Solid.EDIT);
            edit.addAttribute("title", getEditButtonTitle());
            optionsList.add(edit);
        }

        if (OcelotSecurityManager.isAuthorized(getDeletePrivilege()) && showDeleteButton && deleteable(entity)) {
            DeleteButton delete = new DeleteButton(getToDeletePath(entity));
            delete.setStyle("margin-right: 5px; margin-bottom: 5px;");
            delete.addAttribute("title", getDeleteButtonTitle());

            if (getDeleteWarningMessage() != null) {
                delete.addAttribute(WARNING_MESSAGE_CLASS, getDeleteWarningMessage());
            }

            if (getDeleteWarningMessage(entity) != null) {
                delete.addAttribute(WARNING_MESSAGE_CLASS, getDeleteWarningMessage(entity));
            }

            if (getDeleteButtonConfirmLabel() != null) {
                delete.addAttribute("war-button-confirm", getDeleteButtonConfirmLabel());
            }

            if (getDeleteButtonCancelLabel() != null) {
                delete.addAttribute("war-button-cancel", getDeleteButtonCancelLabel());
            }

            optionsList.add(delete);
            addRowOption(optionsList, entity);
        }

        if (OcelotSecurityManager.isAuthorized(getActivePrivilege()) && showActiveButton && activable()) {
            GetButton active = new GetButton(BButtonStyle.INFO, "", getControllerName() + "/active/" + getId(entity));
            active.setStyle("margin-right: 5px; margin-bottom: 5px;");
            active.setIconClass(FontAwesome.Solid.CHECK_SQUARE);
            active.addAttribute("title", getActiveButtonTitle());

            if (getActiveWarningMessage() != null) {
                active.addAttribute(WARNING_MESSAGE_CLASS, getActiveWarningMessage());
            }

            optionsList.add(active);
            addRowOption(optionsList, entity);
        }
    }

    protected String getToDeletePath(E entity) {
        return getControllerName() + "/delete/" + getId(entity) + getViewParams();
    }

    protected String getToEditPath(E entity) {
        return getControllerName() + "/edit/" + getId(entity) + getViewParams();
    }

    protected String getToDetailPath(E entity) {
        return getControllerName() + "/detail/" + getId(entity) + getViewParams();
    }

    protected boolean editable(E entity) {
        return true;
    }

    protected boolean deleteable(E entity) {
        return true;
    }

    protected boolean activable() {
        return true;
    }

    protected void addRowOption(LinkedList<HtmlObject> optionsList, E entity) {

    }

    public Page<E> getQueryResult() {
        PageRequest request = new PageRequest(0, getNumRowsToView());
        Map<String, String[]> filtersMap = null;

        if (getDefaultFilter() == null) {
            filtersMap = new LinkedHashMap<>();
            String[] filterValue = {""};
            filtersMap.put("filter", filterValue);
        } else {
            filtersMap = getDefaultFilter();
        }

        return getBusinessService().getWithFilters(filtersMap, request);
    }

    protected Map<String, String[]> getDefaultFilter() {
        return null;
    }

    protected int getNumRowsToView() {
        return 10;
    }

    protected long getNumIndexOnPaginator() {
        return 5;
    }

    protected Object[] getDetails(Map<String, String[]> valuesMap) {
        return null;
    }

    protected String getDefaultFilterMessage() {
        return null;
    }

    protected String getDeleteWarningMessage() {
        return null;
    }

    protected String getDeleteWarningMessage(E entity) {
        return null;
    }

    protected String getActivePrivilege() {
        return null;
    }

    protected String getActiveWarningMessage() {
        return null;
    }

    protected void addAditionalOptions(LinkedList<HtmlObject> optionsList, E entity) {

    }

    protected String addJsFile() {
        return null;
    }

    protected String addToOnclickSaveButton() {
        return null;
    }

    protected boolean canViewRow(E entity) {
        return true;
    }

    protected void processRequest(HttpServletRequest request) {

    }

    protected String getOriginController() {
        return null;
    }

    protected AjaxButton getBackButton(HttpServletRequest request) {
        AjaxButton backButton = new GetButton(BButtonStyle.WARNING, getBackButtonTitle(), getOriginController());
        backButton.setIconClass(FontAwesome.Solid.ARROW_ALT_CIRCLE_LEFT);
        backButton.setStyle("float: right");

        return backButton;
    }

    protected boolean haveFiles() {
        return false;
    }

    protected String getBackPathOnNewForm() {
        return getControllerName() + getViewParams();
    }

    protected String getBackPathOnDetailView() {
        return getControllerName() + getViewParams();
    }

    protected boolean disableNewButton() {
        return false;
    }

    protected String getSearchTitle() {
        return "Buscar";
    }

    protected String getNoPrivilegesAlertTitle() {
        return "No posee privilegios para realizar esta acción";
    }

    protected String getBackButtonTitle() {
        return "Regresar";
    }

    protected String getSaveButtonTitle() {
        return "Guardar";
    }

    protected String getSavedSuccessMessage(E entity) {
        return "Se ha guardado de forma exitosa";
    }

    protected String getMultipartSavedSuccessMessage(Map<String, String[]> valuesMap) {
        return "Se ha guardado de forma exitosa";
    }

    protected String getEditedSuccessMessage(E entity) {
        return "Se ha editado de forma exitosa";
    }

    protected String getMultipartEditedSuccessMessage(Map<String, String[]> valuesMap) {
        return "Se ha editado de forma exitosa";
    }

    protected String getSearchNoResultsTitle() {
        return "No hay resultados para la búsqueda";
    }

    protected String getDeleteSuccesfulTitle() {
        return "Se ha eliminado el registro de forma exitosa";
    }

    protected String getActiveSuccesfulTitle() {
        return "Se ha activado el registro de forma exitosa";
    }

    protected String getFilterTitle() {
        return "Filtro";
    }

    protected String getResultsTitle() {
        return "Resultados";
    }

    protected String getOptionTitle() {
        return "Opciones";
    }

    protected String getDeleteButtonTitle() {
        return "Eliminar";
    }

    protected String getActiveButtonTitle() {
        return "Activar";
    }

    protected String getNewButtonTitle() {
        return "Nuevo";
    }

    protected String getEditButtonTitle() {
        return "Editar";
    }

    protected String getMessageResultPaginator() {
        return null;
    }

    protected String getMessageNextPaginator() {
        return null;
    }

    protected String getMessagePrevious() {
        return null;
    }

    protected String getDeleteButtonConfirmLabel() {
        return null;
    }

    protected String getDeleteButtonCancelLabel() {
        return null;
    }

    protected String getDeafultDateFormat() {
        return "dd/MM/yyyy";
    }

    protected boolean disableBackButton() {
        return false;
    }

    protected HtmlContainer getDetailView(HttpServletRequest request, E entity) {
        LinkedHashMap<String, String> details = getDetailViewData(request, entity);

        if (details.isEmpty()) {
            return null;
        }

        HtmlContainer container = new HtmlContainer();

        details.entrySet().forEach((entry) -> {
            container.add(new BShowField(entry.getKey(), entry.getValue()));
        });

        return container;
    }

    protected LinkedHashMap<String, String> getDetailViewData(HttpServletRequest request, E entity) {
        return new LinkedHashMap<>();
    }

    private void addAfterForm(HtmlContainer afterFormContainer) {

    }

    protected boolean hasOptions() {
        return true;
    }

    protected boolean hasResultTitle() {
        return true;
    }

    /**
     * Init form with entity values
     *
     * @param form
     * @param entity
     */
    protected void fillFormOnEdit(AjaxForm form, E entity) {
        HtmlFormHelper.fillForm(form, entity);
    }

}
