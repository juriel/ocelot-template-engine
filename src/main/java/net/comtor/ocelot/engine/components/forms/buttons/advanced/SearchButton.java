package net.comtor.ocelot.engine.components.forms.buttons.advanced;

import net.comtor.ocelot.bootstrap.forms.buttons.BButtonStyle;
import net.comtor.ocelot.engine.components.forms.buttons.PostButton;
import net.comtor.ocelot.engine.util.icons.FontAwesome;

/**
 *
 * @author Guido Cafiel
 */
public class SearchButton extends PostButton {

    public SearchButton(String value, String formName, String endpoint, boolean reset) {
        super(BButtonStyle.PRIMARY, value, formName, endpoint, reset);
        setIconClass(FontAwesome.Solid.SEARCH);
    }

}
