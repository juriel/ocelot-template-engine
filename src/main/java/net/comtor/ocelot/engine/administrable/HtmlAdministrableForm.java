package net.comtor.ocelot.engine.administrable;

import net.comtor.ocelot.html.IHtmlContainer;
import net.comtor.ocelot.html.forms.HtmlFormElement;

/**
 *
 * @author juriel
 */
public interface HtmlAdministrableForm extends IHtmlContainer {

    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    public static final String ENCTYPE_MULTIPART_FORM_DATA = "multipart/form-data";

    public void addInputHidden(String name, String value);

    public void addField(String id, String label, HtmlFormElement input);

    public void addField(String label, HtmlFormElement input);  // Esta saca el id del name del HtmlFormElement

    public void addField(String label, HtmlFormElement input, String help);

    public void addField(String label, HtmlFormElement input, String help, String error);

    HtmlFormElement getInput(String name);  // Que es el mismo id

    void setAction(String action);

    String getAction();

    void setName(String name);

    String getName();

    void setEnctType(String encType);
}
