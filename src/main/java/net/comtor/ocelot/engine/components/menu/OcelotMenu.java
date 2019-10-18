package net.comtor.ocelot.engine.components.menu;

import java.util.List;
import net.comtor.ocelot.html.HtmlObject;
import net.comtor.ocelot.html.HtmlTag;

/**
 *
 * @author Guido Cafiel
 */
public interface OcelotMenu extends HtmlObject{

    public void buildMenu(MenuItem root);

    public HtmlTag getFather(MenuItem father);

    public HtmlTag getChildren(List<MenuItem> childs);
}
