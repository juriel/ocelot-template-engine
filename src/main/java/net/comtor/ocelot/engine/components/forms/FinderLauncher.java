package net.comtor.ocelot.engine.components.forms;

import net.comtor.ocelot.bootstrap.commons.BColor;
import net.comtor.ocelot.bootstrap.components.buttons.BModalLauncherButton;
import net.comtor.ocelot.bootstrap.forms.BShowField;

/**
 *
 * @author Guido Cafiel
 */
//TODO: QUE QUEDE BONITO EL BOTON JUNTO AL INPUT
public class FinderLauncher extends BShowField {

    private String myEndpoint;
    private BModalLauncherButton modalLauncherButton;

    public FinderLauncher(String label, String showValue, String name, String hiddenValue, String endpoint) {
        super(label, showValue, name, hiddenValue);

        myEndpoint = endpoint + "/" + name;
        modalLauncherButton = getModalLauncher(myEndpoint);

        setStyle("height: 36px;");

        getInput().setClass("form-control ocelot-finder-visible");
    }

    public FinderLauncher(String label, String name, String endpoint) {
        this(label, "", name, null, endpoint);
    }

    public FinderLauncher addParamsToEndpoint(String urlParams) {
        modalLauncherButton.addAttribute("endpoint", myEndpoint + "?" + urlParams);

        return this;
    }

    @Override
    protected void preHtmlRender() {
        getMainContainer().add(modalLauncherButton);

        super.preHtmlRender();
    }

    private BModalLauncherButton getModalLauncher(String endpoint) {
        BModalLauncherButton button = new BModalLauncherButton("ocelotModal", "fas fa-search");
        button.addClass("btn")
                .addClass("btn-primary")
                .addClass("ml-3");
        button.setBColor(BColor.PRIMARY)
                .setStyle("float: right")
                .addClass("finderLauncher")
                .addAttribute("endpoint", endpoint);

        return button;
    }
}
