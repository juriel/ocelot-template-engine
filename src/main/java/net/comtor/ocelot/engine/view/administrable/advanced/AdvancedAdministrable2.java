package net.comtor.ocelot.engine.view.administrable.advanced;

import java.io.Serializable;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.comtor.ocelot.bootstrap.components.cards.BCard;
import net.comtor.ocelot.engine.commons.MapResponse;
import net.comtor.ocelot.engine.components.forms.buttons.AjaxButton;
import net.comtor.ocelot.engine.components.forms.buttons.advanced.SearchButton;
import net.comtor.ocelot.engine.components.forms.forms.advanced.SearchForm;
import static net.comtor.ocelot.engine.view.administrable.Administrable.QUERY_FORM;
import net.comtor.ocelot.html.HtmlContainer;
import net.comtor.ocelot.html.formatting.HtmlSmall;
import net.comtor.ocelot.html.forms.inputs.HtmlInputText;
import net.comtor.ocelot.html.programing.HtmlScript;
import net.comtor.ocelot.html.styles.HtmlDiv;
import org.springframework.data.domain.Page;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Oct 28, 2020
 */
public abstract class AdvancedAdministrable2<E, ID extends Serializable> extends AdvancedAdministrable<E, ID> {

    @Override
    protected SearchForm getFiltersForm() {
        SearchForm form = new SearchForm(QUERY_FORM);

        HtmlDiv formGroup = new HtmlDiv();
        formGroup.addClass("form-group").addClass("row");

        HtmlInputText inputText = new HtmlInputText();
        inputText.setName("filter").setId("filter");
        inputText.addClass("form-control").onKeyPress("return pulsar(event)");
        inputText.addAttribute("placeholder", "Buscar...");
        formGroup.add(inputText);

        SearchButton searchButton = new SearchButton("", QUERY_FORM, getControllerName() + "/search/0", false);
        formGroup.add(searchButton);

        form.add(formGroup);

        return form;
    }

    @Override
    protected boolean hasResultTitle() {
        return false;
    }

    @Override
    public List<MapResponse> mainView(HttpServletRequest request, String idDivContent) {
        HtmlContainer container = new HtmlContainer();
        BCard card = new BCard(getIconClass(), getTitle());

        if (getSubTitle() != null) {
            card.addToBody(new HtmlSmall(getSubTitle()));
        }

        card.addToBody(new HtmlDiv().setId(OCELOT_DEFAULT_ALERT));

        SearchForm searchForm = getFiltersForm();
        card.addToBody(searchForm);

        HtmlDiv buttonsRow = new HtmlDiv("crud-action-buttons");
        buttonsRow.addClass("m-3");

        for (AjaxButton ajaxButton : getButtons(request)) {
            buttonsRow.add(ajaxButton);
        }

        card.addToBody(buttonsRow);

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

    @Override
    protected String getOptionTitle() {
        return "";
    }

}
