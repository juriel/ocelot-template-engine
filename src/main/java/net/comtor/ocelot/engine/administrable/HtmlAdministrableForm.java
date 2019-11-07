package net.comtor.ocelot.engine.administrable;

import net.comtor.ocelot.html.IHtmlContainer;
import net.comtor.ocelot.html.forms.HtmlFormElement;

/**
 *
 * @author juriel
 */
public interface HtmlAdministrableForm extends IHtmlContainer{

    public void addInputHidden(String name, String value);

    public void addField(String id, String label, HtmlFormElement input);

    public void addField(String label, HtmlFormElement input);  // Esta saca el id del name del HtmlFormElement

    public void addField(String label, HtmlFormElement input, String help);

    public void addField(String label, HtmlFormElement input, String help, String error);
    HtmlFormElement getInput(String name);  // Que es el mismo id
    
}
