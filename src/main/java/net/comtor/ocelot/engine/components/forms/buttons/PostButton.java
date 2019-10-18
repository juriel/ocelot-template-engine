package net.comtor.ocelot.engine.components.forms.buttons;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public class PostButton extends AjaxButton {

    public PostButton(String buttonStyle, String value, String formName, String endpoint, boolean reset) {
        super(buttonStyle, value, endpoint);
        initComponent(formName, reset);
    }

    private void initComponent(String formName, boolean reset) {
        if (reset) {
            addAttribute("reset", "reset");
        }

        addAttribute("form-name", formName);
        addClass("ajaxPost");
    }

}
