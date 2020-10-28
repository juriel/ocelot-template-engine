package net.comtor.ocelot.engine.view.simple;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.comtor.ocelot.engine.commons.MapResponse;
import net.comtor.ocelot.engine.components.forms.buttons.AjaxButton;
import net.comtor.ocelot.engine.security.OcelotSecurityManager;
import net.comtor.ocelot.engine.view.WebView;
import net.comtor.ocelot.bootstrap.components.alerts.BAlertDanger;
import net.comtor.ocelot.bootstrap.components.cards.BCard;
import net.comtor.ocelot.html.HtmlContainer;
import net.comtor.ocelot.html.formatting.HtmlSmall;
import net.comtor.ocelot.html.HtmlObject;
import net.comtor.ocelot.html.HtmlTag;
import net.comtor.ocelot.html.IHtmlTag;
import net.comtor.ocelot.html.programing.HtmlScript;
import net.comtor.ocelot.html.styles.HtmlDiv;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 *
 * @author Guido Cafiel
 */
public abstract class SimpleView extends WebView {

    public static final String OCELOT_DEFAULT_ALERT = "ocelot-default-alert";
    public static final String OCELOT_HIDDEN_ALERT = "ocelot-hidden-alert";

    protected abstract void getViewContent(HttpServletRequest request, HtmlContainer container);

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(getDefaultDateFormat());
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    protected String getDefaultDateFormat() {
        return "yyyy-MM-dd";
    }

    protected LinkedList<AjaxButton> getButtons() {
        LinkedList<AjaxButton> buttons = new LinkedList<>();
        addButtons(buttons);

        return buttons;
    }

    protected void addButtons(LinkedList<AjaxButton> buttons) {

    }

    protected HtmlObject getSubTitle() {
        return null;
    }

    protected String getViewPrivilege() {
        return null;
    }

    protected String getProcessPrivilege() {
        return null;
    }

    protected String addJsFile() {
        return null;
    }

    protected String getOriginController() {
        return null;
    }

    protected String getIconClass() {
        return null;
    }

    @Override
    public List<MapResponse> mainView(HttpServletRequest request) {
        processRequest(request);

        return getMainView(request);
    }

    protected List<MapResponse> getMainView(HttpServletRequest request) {
        List<MapResponse> options = new LinkedList<>();

        if ((getViewPrivilege() != null) && !OcelotSecurityManager.isAuthorized(getViewPrivilege())) {
            BAlertDanger error = new BAlertDanger(getNoPrivilegesAlertTitle());
            options.add(new MapResponse(OCELOT_HIDDEN_ALERT, error.getHtml()));

            return options;
        }

        HtmlContainer container = new HtmlContainer();
        BCard card = new BCard(getIconClass(), getTitle());

        for (HtmlTag htmlTag : addToHeader()) {
            card.addToHeader(htmlTag);
        }

        getButtons().forEach((mercuryButton) -> {
            card.addToHeader(mercuryButton);
        });

        if (getOriginController() != null) {
            card.addToHeader(getBackButton(request));
        }

        if (getSubTitle() != null) {
            HtmlSmall small = new HtmlSmall();
            small.add(getSubTitle());
            card.addToBody(small);
        }
        
        card.addToBody(new HtmlDiv(OCELOT_DEFAULT_ALERT));

        HtmlContainer viewContainer = new HtmlContainer();

        getViewContent(request, viewContainer);

        if (isTwoColumns()) {
            viewContainer.getContentObjects().forEach((htmlObject) -> {
                if (htmlObject instanceof IHtmlTag) {
                    IHtmlTag obj = (IHtmlTag) htmlObject;
                    obj.addClass("col-sm-6");
                }
            });
        }

        card.addToBody(viewContainer);

        container.add(card);

        List<String> jsPathResources = new LinkedList<>();

        addJsResources(jsPathResources);

        for (String jsPathResource : jsPathResources) {
            HtmlScript script = new HtmlScript();
            script.setSrc(jsPathResource);
            container.add(script);
        }

        if (addJsFile() != null) {
            HtmlScript script = new HtmlScript();
            script.setSrc(addJsFile());
            container.add(script);
        }

        options.add(new MapResponse(getContentDivId(), container.getHtml()));

        return options;
    }

    protected List<HtmlTag> addToHeader() {
        return new LinkedList<>();
    }

    protected String getContentDivId() {
        return "content";
    }

    protected boolean isTwoColumns() {
        return false;
    }

    protected void processRequest(HttpServletRequest request) {

    }

    @Override
    protected void addJsResources(List<String> paths) {

    }

    public String getHtml(HttpServletRequest request) {
        return mainView(request).get(0).getValue();
    }

}
