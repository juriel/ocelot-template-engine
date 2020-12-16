package net.comtor.ocelot.engine.view.advanced.reports;

import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.comtor.ocelot.bootstrap.components.alerts.BAlertDanger;
import net.comtor.ocelot.bootstrap.components.cards.BCard;
import net.comtor.ocelot.engine.commons.MapResponse;
import net.comtor.ocelot.engine.security.OcelotSecurityManager;
import net.comtor.ocelot.engine.util.icons.FontAwesome;
import static net.comtor.ocelot.engine.view.simple.SimpleView.OCELOT_DEFAULT_ALERT;
import static net.comtor.ocelot.engine.view.simple.SimpleView.OCELOT_HIDDEN_ALERT;
import net.comtor.ocelot.html.HtmlContainer;
import net.comtor.ocelot.html.HtmlDoubleTag;
import net.comtor.ocelot.html.HtmlObject;
import net.comtor.ocelot.html.HtmlTag;
import net.comtor.ocelot.html.IHtmlTag;
import net.comtor.ocelot.html.basic.HtmlH2;
import net.comtor.ocelot.html.basic.HtmlH5;
import net.comtor.ocelot.html.formatting.HtmlSmall;
import net.comtor.ocelot.html.links.HtmlA;
import net.comtor.ocelot.html.programing.HtmlScript;
import net.comtor.ocelot.html.styles.HtmlDiv;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Oct 28, 2020
 */
public abstract class ReportView2 extends ReportView {

    protected String getHintTitle() {
        return "Acerca de este reporte";
    }

    protected String getHintText() {
        return "";
    }

    @Override
    protected List<MapResponse> getMainView(HttpServletRequest request) {
        List<MapResponse> options = new LinkedList<>();

        if ((getViewPrivilege() != null) && !OcelotSecurityManager.isAuthorized(getViewPrivilege())) {
            BAlertDanger error = new BAlertDanger(getNoPrivilegesAlertTitle());
            options.add(new MapResponse(OCELOT_HIDDEN_ALERT, error.getHtml()));

            return options;
        }

        HtmlContainer container = new HtmlContainer();
        BCardReport card = new BCardReport(getIconClass(), getTitle());

        for (HtmlTag htmlTag : addToHeader()) {
            card.addToHeader(htmlTag);
        }

        getButtons().forEach((button) -> card.addToBody(button));

        if (getOriginController() != null) {
            card.addToHeader(getBackButton(request));
        }

        if (getSubTitle() != null) {
            HtmlSmall small = new HtmlSmall();
            small.add(getSubTitle());
            card.addToBody(small);
        }

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

        card.addToBody(new HtmlDiv(OCELOT_DEFAULT_ALERT));

        card.addToBody(viewContainer);

        container.add(new HtmlScript(""
                + " $(document).ready(function () {\n"
                + "     $('[data-toggle=\"popover\"]').popover();\n"
                + " });"
        ));

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

    class BCardReport extends BCard {

        public BCardReport(String icon, String title) {
            super(icon, title);
        }

        @Override
        protected HtmlObject getHeader(String title, List<HtmlObject> headerElements) {
            HtmlDiv cardHeader = new HtmlDiv();
            cardHeader.addClass("card-header").addClass("with-elements");

            HtmlDoubleTag cardHeaderTitle;

            if (hasSmallTitle()) {
                cardHeaderTitle = new HtmlH5();
            } else {
                cardHeaderTitle = new HtmlH2();
            }

            if (getIcon() != null) {
                cardHeaderTitle.add(getIcon());
            }

            cardHeaderTitle.addClass("card-header-title");
            cardHeaderTitle.addEscapedText(title);

            if (StringUtils.isNotEmpty(getHintText())) {
                HtmlA hint = new HtmlA("#", "");
                hint.addClass(FontAwesome.Solid.QUESTION_CIRCLE);
                hint.addClass("crud-popopver-hint-icon");
                hint.addClass("mx-2");
                hint.addAttribute("data-toggle", "popover");
                hint.addAttribute("title", getHintTitle());
                hint.addAttribute("data-content", getHintText());
                hint.addAttribute("data-placement", "right");
                hint.addAttribute("data-trigger", "hover");

                cardHeaderTitle.add(hint);
            }

            HtmlDiv cardHeaderElements = new HtmlDiv();
            cardHeaderElements.addClass("card-header-elements").addClass("ml-auto");
            cardHeaderElements.addAll(headerElements);

            cardHeader.add(cardHeaderTitle);
            cardHeader.add(cardHeaderElements);

            return cardHeader;
        }

    }
}
