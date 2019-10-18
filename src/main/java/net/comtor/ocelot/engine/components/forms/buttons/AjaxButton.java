package net.comtor.ocelot.engine.components.forms.buttons;

import net.comtor.ocelot.bootstrap.forms.buttons.BButton;
import net.comtor.ocelot.html.formatting.HtmlIcon;
import net.comtor.ocelot.html.styles.HtmlSpan;

/**
 *
 * @author Guido Cafiel
 */
public class AjaxButton extends BButton implements OcelotButton {

    private String localValue;

    public AjaxButton(String buttonStyle, String value, String endpoint, boolean isLadda) {
        super();

        setButtonStyle(buttonStyle);
        setEndpoint(endpoint);

        this.localValue = value;

        if (isLadda) {
            becomeLaddaButtom();
        }
    }

    public AjaxButton(String buttonStyle, String value, String endpoint) {
        super();

        setButtonStyle(buttonStyle);
        setEndpoint(endpoint);

        this.localValue = value;

        becomeLaddaButtom();
    }

    @Override
    public void setEndpoint(String endpoint) {
        addAttribute("endpoint", endpoint);
        addAttribute("data-style", "zoom-in");
        addClass("ladda-button ladda-button-demo");
        setStyle("margin-right: 5px");
    }

    private void becomeLaddaButtom() {
        addClass("ladda-button").addAttribute("data-style", "zoom-out");

        HtmlSpan span = new HtmlSpan(this.localValue);
        span.addClass("ladda-label");

        add(span).add(new HtmlSpan().addClass("ladda-spinner"));
    }

    @Override
    public void setIconClass(String iconClass) {
        HtmlIcon iTag = new HtmlIcon();
        iTag.addClass(iconClass);

        if ((this.localValue != null) && !this.localValue.trim().isEmpty()) {
            iTag.setStyle("margin-right: 5px");
        }

        addFirst(iTag);
    }

}
