package net.comtor.ocelot.engine.components.forms;

import net.comtor.ocelot.bootstrap.commons.BColor;
import net.comtor.ocelot.bootstrap.components.buttons.BModalLauncherButton;
import net.comtor.ocelot.bootstrap.forms.BShowField;
import net.comtor.ocelot.bootstrap.forms.buttons.BButton;
import net.comtor.ocelot.bootstrap.forms.buttons.BButtonStyle;
import net.comtor.ocelot.engine.util.icons.FontAwesome;
import net.comtor.ocelot.html.formatting.HtmlSmall;
import net.comtor.ocelot.html.forms.HtmlLabel;
import net.comtor.ocelot.html.forms.inputs.HtmlInputText;
import net.comtor.ocelot.html.styles.HtmlDiv;
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
        // Obtengo los elementos originales del input
        HtmlLabel label = getLabelElement();
        HtmlInputText input = getInput();
        HtmlSmall help = getHelpElement();
        HtmlSmall error = getErrorElement();

        // Borro los elementos para generar mi propia diagramacion
        getMainContainer().removeAll();

        HtmlDiv row = new HtmlDiv();
        row.addClass("row m-0");
        row.add(input.addClass("col-9"));

        HtmlSpan buttons = new HtmlSpan();
        buttons.addClass("col-3 py-0");
        buttons.add(modalLauncherButton).add(clearFinderButton);

        row.add(buttons);

        getMainContainer().add(label).add(row).add(help).add(error);
    }

    private BModalLauncherButton getModalLauncher(String endpoint) {
        BModalLauncherButton button = new BModalLauncherButton("ocelotModal", FontAwesome.Solid.SEARCH);
        button.setId(id + "_modal_launcher_btn");
        button.addClass("btn")
                .addClass("btn-primary")
                .addClass("modal_launcher_btn")
                .addClass("ml-1 mx-0");
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
                .addClass("ml-1 mx-0");
        clearButton.setIcon(FontAwesome.Solid.TIMES_CIRCLE);
        clearButton.addAttribute("title", "Limpiar valor");
        clearButton.onClick("clearValuesFinder('" + fieldId + "');");
        clearButton.addAttribute("type", "button");

        return clearButton;
    }
}