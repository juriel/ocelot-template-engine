package net.comtor.ocelot.engine.components.forms.forms;

import net.comtor.html.advanced.AbstractHtmlAdministrableFormElement;
import net.comtor.html.advanced.HtmlAdministrableForm;
import net.comtor.ocelot.html.forms.HtmlFormElement;

/**
 *
 * @author Guido Cafiel
 */
public class AjaxForm extends AbstractHtmlAdministrableFormElement {

    public AjaxForm(String name) {
        setName(name);
        setId(name);
        addClass("ajaxForm");
    }

    @Override
    public HtmlAdministrableForm addField(String id, String label, HtmlFormElement input, String help, String error) {
        this.addField(id, label, input, help, error);

        return this;
    }

    public AjaxForm addMultipart() {
        addAttribute("enctype", "multipart/form-data");;

        return this;
    }

    public AjaxForm addSubmitWithIntro() {
        addAttribute("validate-intro", "true");

        return this;
    }
}
