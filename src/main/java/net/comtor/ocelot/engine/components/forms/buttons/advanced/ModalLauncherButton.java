package net.comtor.ocelot.engine.components.forms.buttons.advanced;

import net.comtor.ocelot.engine.components.forms.buttons.AjaxButton;

/**
 *
 * @author Guido Cafiel
 */
public class ModalLauncherButton extends AjaxButton {

    public ModalLauncherButton(String buttonStyle, String modalId, String iconClass, String value, String endpoint) {
        super(buttonStyle, value, endpoint, false);

        setIconClass(iconClass);
        addClass("ajaxGet");
        addAttribute("data-toggle", "modal");
        addAttribute("data-target", "#" + modalId);
        addAttribute("no-ladda", "true");
    }

}
