package net.comtor.ocelot.engine.components.forms.buttons.advanced;

import net.comtor.ocelot.bootstrap.commons.BColor;
import net.comtor.ocelot.engine.components.forms.buttons.PostButton;

/**
 *
 * @author Guido Cafiel
 */
public class SearchButton extends PostButton {

    public SearchButton(String value, String formName, String endpoint, boolean reset) {
        super(BColor.PRIMARY, value, formName, endpoint, reset);
        setIconClass("fa fa-search");
    }

}
