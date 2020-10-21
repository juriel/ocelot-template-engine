package net.comtor.ocelot.engine.components.forms.buttons.advanced;

import net.comtor.ocelot.bootstrap.forms.buttons.BButtonStyle;
import net.comtor.ocelot.engine.components.forms.buttons.AjaxButton;
import static net.comtor.ocelot.engine.util.icons.FontAwesome.Solid.TRASH_ALT;

/**
 *
 * @author Guido Cafiel
 */
public class DeleteButton extends AjaxButton {

    public DeleteButton(BButtonStyle buttonStyle, String value, String endpoint) {
        super(buttonStyle, value, endpoint);

        addClass("ajaxDelete");
        setIconClass(TRASH_ALT);
    }

    public DeleteButton(String endpoint, String warMessage) {
        this(BButtonStyle.DANGER, null, endpoint);

        if (warMessage != null) {
            addAttribute("war-message", warMessage);
        }
    }

    public DeleteButton(String endpoint) {
        this(endpoint, null);
    }

    public void setWarningMessage(String warningMessage) {
        addAttribute("war-message", warningMessage);
    }

}
