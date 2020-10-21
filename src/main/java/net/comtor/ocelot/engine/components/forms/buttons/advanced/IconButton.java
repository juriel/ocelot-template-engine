package net.comtor.ocelot.engine.components.forms.buttons.advanced;

import net.comtor.ocelot.bootstrap.forms.buttons.BButton;
import net.comtor.ocelot.bootstrap.forms.buttons.BButtonStyle;
import net.comtor.ocelot.html.formatting.HtmlIcon;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Guido Cafiel
 */
public class IconButton extends BButton {

    private final String localLabel;

    public IconButton(String label, BButtonStyle style, String iconType) {
        super(style, label);

        this.localLabel = label;

        initComponents(iconType);
    }

    public void initComponents(String iconType) {
        HtmlIcon myIcon = new HtmlIcon();
        myIcon.addClass(iconType);
        setStyle("color: white");

        if (StringUtils.isNotEmpty(localLabel)) {
            myIcon.setStyle("margin-right: 5px");
        }

        this.add(myIcon);

    }

}
