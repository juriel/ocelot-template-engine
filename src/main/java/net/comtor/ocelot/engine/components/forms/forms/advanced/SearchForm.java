package net.comtor.ocelot.engine.components.forms.forms.advanced;

import net.comtor.ocelot.engine.components.forms.forms.AjaxForm;

/**
 *
 * @author Guido Cafiel
 */
public class SearchForm extends AjaxForm {

    public SearchForm() {
        super("");
        addAttribute("validate-intro", "true");
    }

    public SearchForm(String id) {
        super("");
        setId(id);
        addAttribute("validate-intro", "true");
    }
}
