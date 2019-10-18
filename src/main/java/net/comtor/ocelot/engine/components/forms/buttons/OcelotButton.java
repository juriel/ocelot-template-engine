package net.comtor.ocelot.engine.components.forms.buttons;

import net.comtor.ocelot.html.HtmlObject;
import net.comtor.ocelot.html.IHtmlTag;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public interface OcelotButton extends HtmlObject, IHtmlTag {

    void setEndpoint(String endpoint);

    void setIconClass(String iconClass);

}
