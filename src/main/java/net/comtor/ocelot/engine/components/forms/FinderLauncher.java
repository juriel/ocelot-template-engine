package net.comtor.ocelot.engine.components.forms;

import net.comtor.ocelot.bootstrap.commons.BColor;
import net.comtor.ocelot.bootstrap.components.buttons.BModalLauncherButton;
import net.comtor.ocelot.bootstrap.forms.BShowField;

/**
 *
 * @author Guido Cafiel
 */
public class FinderLauncher extends BShowField {

    private static final String CONTROL_CLASS = "form-control-ocelot";

    private String myEndpoint;
    private BModalLauncherButton modalLauncherButton;

    public FinderLauncher(String label, String showValue, String name, String hiddenValue, String endpoint) {
        super(label, showValue, name, hiddenValue);

        myEndpoint = endpoint + "/" + name;
        modalLauncherButton = getModalLauncher(myEndpoint);

        setStyle("height: 36px;");

        getInput().setClass(CONTROL_CLASS);
    }

    public FinderLauncher(String label, String name, String endpoint) {
        this(label, "", name, null, endpoint);
    }

    public FinderLauncher addParamsToEndpoint(String urlParams) {
        modalLauncherButton.addAttribute("endpoint", myEndpoint + "?" + urlParams);

        return this;
    }

    protected BModalLauncherButton getModalLauncher(String urlEndpoint) {
        BModalLauncherButton button = new BModalLauncherButton("ocelotModal", "fas fa-search");
        button.addClass("btn")
                .addClass("btn-primary")
                .addClass("ml-3");
        button.setBColor(BColor.PRIMARY)
                .setStyle("float: right")
                .addClass("finderLauncher")
                .addAttribute("endpoint", urlEndpoint);

        return button;
    }

    @Override
    protected void preHtmlRender() {
        add(modalLauncherButton);

        super.preHtmlRender();
    }
}
