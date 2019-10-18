package net.comtor.ocelot.engine.components.forms.buttons.advanced;

import net.comtor.ocelot.bootstrap.commons.BColor;
import net.comtor.ocelot.engine.components.forms.buttons.AjaxButton;

/**
 *
 * @author Guido Cafiel
 */
public class DeleteButton extends AjaxButton {

    private String warningMessage;

    public DeleteButton(String endpoint) {
        super(BColor.DANGER, null, endpoint);
        this.warningMessage = null;
        addClass("ajaxDelete");
        setIconClass("fas fa-trash-alt");

    }

    public DeleteButton(String endpoint, String warMessage) {
        super(BColor.DANGER, null, endpoint);
        this.warningMessage = warMessage;
        addClass("ajaxDelete");
        setIconClass("fas fa-trash-alt");
        if (warningMessage != null) {
            addAttribute("war-message", warningMessage);
        }
    }

    public DeleteButton(String buttonStyle, String value, String endpoint) {
        super(buttonStyle, value, endpoint);
        addClass("ajaxDelete");
        setIconClass("fas fa-trash-alt");
    }

    public void setWarningMessage(String warningMessage) {
        addAttribute("war-message", warningMessage);
    }

}
