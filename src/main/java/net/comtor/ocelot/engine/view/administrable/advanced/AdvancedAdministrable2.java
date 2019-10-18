/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.comtor.ocelot.engine.view.administrable.advanced;

import java.io.Serializable;
import net.comtor.ocelot.engine.components.forms.buttons.advanced.SearchButton;
import net.comtor.ocelot.engine.components.forms.forms.advanced.SearchForm;
import static net.comtor.ocelot.engine.view.administrable.Administrable.QUERY_FORM;
import net.comtor.ocelot.html.forms.inputs.HtmlInputText;
import net.comtor.ocelot.html.styles.HtmlDiv;

/**
 *
 * @author Guido Cafiel
 */
public abstract class AdvancedAdministrable2<E, ID extends Serializable> extends AdvancedAdministrable<E, ID> {

    @Override
    protected SearchForm getFiltersForm() {
        SearchForm form = new SearchForm(QUERY_FORM);
        form.addClass("ajaxForm");

        HtmlDiv formGrup = new HtmlDiv();
        formGrup.addClass("form-group").addClass("row");

        HtmlInputText inputText = new HtmlInputText();
        inputText.setName("filter").setId("filter");
        inputText.addClass("form-control").onKeyPress("return pulsar(event)");
        inputText.addAttribute("placeholder", "Ingrese los criterios para filtro.");
        formGrup.add(inputText);

        SearchButton searchButton = new SearchButton("", QUERY_FORM, getControllerName() + "/search/0", false);
        formGrup.add(searchButton);

        form.add(formGrup);
        return form;
    }

    @Override
    protected boolean hasResultTitle() {
        return false;
    }

}
