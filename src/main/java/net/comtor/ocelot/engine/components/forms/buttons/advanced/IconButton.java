package net.comtor.ocelot.engine.components.forms.buttons.advanced;

import net.comtor.ocelot.bootstrap.forms.buttons.BButton;
import net.comtor.ocelot.html.formatting.HtmlIcon;

/**
 *
 * @author Guido Cafiel
 */
public class IconButton extends BButton {

    public static final String DELETE = "fas fa-trash-alt";
    public static final String EDIT = "fas fa-edit";
    public static final String UPLOAD = "fas fa-upload";
    public static final String SUBMIT_ICON = "fas fa-check";
    public static final String MAP = "fas fa-map-marker-alt";
    public static final String SEARCH = "fas fa-search";
    private String localLabel;

    public IconButton(String label, String style, String iconType) {
        super(style, label);
        this.localLabel = label;
        initComponents(iconType);
    }

    public void initComponents(String iconType) {
        HtmlIcon myIcon = new HtmlIcon();
        myIcon.addClass(iconType);
        setStyle("color:white");
        if (localLabel != null && !localLabel.trim().isEmpty()) {
            myIcon.setStyle("margin-right:5px");
        }
        this.add(myIcon);

    }

}
