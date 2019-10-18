package net.comtor.ocelot.engine.components.menu;

import java.util.List;

/**
 *
 * @author Guido Cafiel
 */
public class Menu {

    private List<MenuItem> menuItems;

    public Menu() {
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
