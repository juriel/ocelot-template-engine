package net.comtor.ocelot.engine.components.forms.buttons;

import net.comtor.ocelot.bootstrap.forms.buttons.BButtonStyle;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public class GetButton extends AjaxButton {

    public GetButton(BButtonStyle buttonStyle, String value, String endpoint) {
        super(buttonStyle, value, endpoint);
        addClass("ajaxGet");
    }

}
