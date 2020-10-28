package net.comtor.ocelot.engine.view.advanced.reports;

import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.comtor.ocelot.bootstrap.components.alerts.BAlertDanger;
import net.comtor.ocelot.bootstrap.components.cards.BCard;
import net.comtor.ocelot.engine.commons.MapResponse;
import net.comtor.ocelot.engine.security.OcelotSecurityManager;
import static net.comtor.ocelot.engine.view.simple.SimpleView.OCELOT_DEFAULT_ALERT;
import static net.comtor.ocelot.engine.view.simple.SimpleView.OCELOT_HIDDEN_ALERT;
import net.comtor.ocelot.html.HtmlContainer;
import net.comtor.ocelot.html.HtmlTag;
import net.comtor.ocelot.html.IHtmlTag;
import net.comtor.ocelot.html.formatting.HtmlSmall;
import net.comtor.ocelot.html.programing.HtmlScript;
import net.comtor.ocelot.html.styles.HtmlDiv;

/**
 *
 * @author Guido Cafiel
 */
public abstract class ReportView2 extends ReportView {

    @Override
    protected List<MapResponse> getMainView(HttpServletRequest request) {
        List<MapResponse> options = new LinkedList<>();

        if ((getViewPrivilege() != null) && !OcelotSecurityManager.isAuthorized(getViewPrivilege())) {
            BAlertDanger error = new BAlertDanger(getNoPrivilegesAlertTitle());
            options.add(new MapResponse(OCELOT_HIDDEN_ALERT, error.getHtml()));

            return options;
        }

        HtmlContainer container = new HtmlContainer();
        BCard card = new BCard(getIconClass(), getTitle());

        if (getOriginController() != null) {
            card.addToHeader(getBackButton(request));
        }

        if (getSubTitle() != null) {
            HtmlSmall small = new HtmlSmall();
            small.add(getSubTitle());
            card.addToBody(small);
        }

        card.addToBody(new HtmlDiv(OCELOT_DEFAULT_ALERT));

        for (HtmlTag htmlTag : addToHeader()) {
            card.addToBody(htmlTag);
        }

        getButtons().forEach((mercuryButton) -> card.addToBody(mercuryButton));

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

}
