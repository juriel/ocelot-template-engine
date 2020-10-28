package net.comtor.ocelot.engine.components.forms;

import net.comtor.ocelot.bootstrap.commons.BColor;
import net.comtor.ocelot.bootstrap.components.buttons.BModalLauncherButton;
import net.comtor.ocelot.bootstrap.forms.BShowField;
import net.comtor.ocelot.bootstrap.forms.buttons.BButton;
import net.comtor.ocelot.bootstrap.forms.buttons.BButtonStyle;
import net.comtor.ocelot.engine.util.icons.FontAwesome;
import net.comtor.ocelot.html.styles.HtmlSpan;

/**
 *
 * @author Guido Cafiel
 */
public class FinderLauncher extends BShowField {

    private String id;
    private String myEndpoint;
    private BModalLauncherButton modalLauncherButton;
    private BButton clearFinderButton;

    public FinderLauncher(String label, String showValue, String name, String hiddenValue, String endpoint) {
        super(label, showValue, name, hiddenValue);

        id = name;
        myEndpoint = endpoint + "/" + name;
        modalLauncherButton = getModalLauncher(myEndpoint);
        clearFinderButton = getClearFinderButton(name);

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
        HtmlSpan row = new HtmlSpan();
        row.add(modalLauncherButton).add(clearFinderButton);

        getMainContainer().add(row);

        super.preHtmlRender();
    }

    private BModalLauncherButton getModalLauncher(String endpoint) {
        BModalLauncherButton button = new BModalLauncherButton("ocelotModal", FontAwesome.Solid.SEARCH);
        button.setId(id + "_modal_launcher_btn");
        button.addClass("btn")
                .addClass("btn-primary")
                .addClass("modal_launcher_btn")
                .addClass("ml-3");
        button.setBColor(BColor.PRIMARY)
                .addClass("finderLauncher")
                .addAttribute("endpoint", endpoint);

        return button;
    }

    private BButton getClearFinderButton(String fieldId) {
        BButton clearButton = new BButton(BButtonStyle.DANGER, "");
        clearButton.setId(id + "_clear_finder_btn");
        clearButton.addClass("btn")
                .addClass("btn-primary")
                .addClass("clear_finder_btn")
                .addClass("ml-3");
        clearButton.setIcon(FontAwesome.Solid.BROOM);
        clearButton.addAttribute("title", "Limpiar valor");
        clearButton.onClick("clearValuesFinder('" + fieldId + "');");
        clearButton.addAttribute("type", "button");

        return clearButton;
    }
}
