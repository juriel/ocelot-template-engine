package net.comtor.ocelot.engine.components.menu;

import java.util.List;
import net.comtor.ocelot.html.HtmlContainer;
import net.comtor.ocelot.html.HtmlTag;
import net.comtor.ocelot.html.formatting.HtmlIcon;
import net.comtor.ocelot.html.links.HtmlLi;
import net.comtor.ocelot.html.links.HtmlUl;
import net.comtor.ocelot.html.styles.HtmlDiv;
import net.comtor.ocelot.html.styles.HtmlSpan;

/**
 *
 * @author Guido Cafiel
 */
public class AppWorkMenu extends HtmlContainer implements OcelotMenu {

    public AppWorkMenu(MenuItem menuItem) {
        super();
        buildMenu(menuItem);
    }

    @Override
    public void buildMenu(MenuItem root) {
        HtmlUl rootTag = new HtmlUl();
        rootTag.addClass("sidenav-inner py-1");

        root.getMenuItems().forEach((father) -> {
            rootTag.add(getFather(father));
        });

        add(rootTag);
    }

    @Override
    public HtmlTag getFather(MenuItem father) {
        HtmlLi liFather = new HtmlLi();
        liFather.addClass("sidenav-item").addClass("item-father");

        HtmlSpan spanTag = new HtmlSpan();
        spanTag.addClass("sidenav-link").addClass("sidenav-toggle");
        spanTag.add(getIcon(father.getIcon()).setStyle("margin-right:10px"));
        spanTag.add(new HtmlDiv().addEscapedText(father.getLabel()));
        liFather.add(spanTag);

        if (!father.getMenuItems().isEmpty()) {
            liFather.add(getChildren(father.getMenuItems()));
        }

        return liFather;
    }

    @Override
    public HtmlTag getChildren(List<MenuItem> childs) {
        HtmlUl childsTag = new HtmlUl();
        childsTag.addClass("sidenav-menu");

        childs.forEach((child) -> {
            HtmlLi childLi = new HtmlLi();
            childLi.addClass("sidenav-item");

            HtmlSpan spanTag = new HtmlSpan(new HtmlDiv().addEscapedText(child.getLabel()));
            spanTag.addClass("sidenav-link").addClass("menu-item");
            spanTag.addAttribute("endpoint", child.getPath());
            childLi.add(spanTag);

            childsTag.add(childLi);
        });

        return childsTag;
    }

    private HtmlIcon getIcon(String iconClass) {
        HtmlIcon iTag = new HtmlIcon();
        iTag.addClass(iconClass).addClass("sidenav-icon");
        return iTag;
    }
}
