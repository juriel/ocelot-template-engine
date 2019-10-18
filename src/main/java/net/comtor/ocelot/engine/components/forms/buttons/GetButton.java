package net.comtor.ocelot.engine.components.forms.buttons;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public class GetButton extends AjaxButton {

    public GetButton(String buttonStyle, String value, String endpoint) {
        super(buttonStyle, value, endpoint);
        addClass("ajaxGet");
    }

}
