/*
Esta clase y todo su contenido es propiedad intelectual de Guido Antonio Cafiel Vellojin, 
Queda prohibido el uso, distribución y comercialización sin previa autorización del titular
(Guido Antonio Cafiel Vellojin) cualquier duda al respecto contáctese al correo guido.cafiel@ingenieros.com
 */
package net.comtor.ocelot.engine.view.administrable;

import java.util.LinkedList;
import java.util.List;
import net.comtor.ocelot.engine.components.forms.buttons.GetButton;
import net.comtor.ocelot.bootstrap.components.cards.BCard;
import net.comtor.ocelot.bootstrap.forms.buttons.BButtonStyle;
import net.comtor.ocelot.html.HtmlObject;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public interface AdministrableMainView {

    default HtmlObject getAdministrableMainView(String title, String controller) {
        BCard mainCard = new BCard(title);
        mainCard.setHeaderElements(getHeaderElements(controller));
        
        return mainCard;
    }

    default List<HtmlObject> getHeaderElements(String controller) {
        List<HtmlObject> elements = new LinkedList<>();
        elements.add(getNewButton(controller));
        addElementsHeaderToMainView(elements);
        
        return elements;
    }

    default void addElementsHeaderToMainView(List<HtmlObject> elements) {

    }

    default HtmlObject getNewButton(String controller) {
        GetButton newButton = new GetButton(BButtonStyle.INFO, "Nuevo", null);
        newButton.setEndpoint(controller + "/new");

        return newButton;
    }

}
