
package net.comtor.ocelot.engine.components.menu;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Guido Cafiel
 */
public class MenuItem {

    private String label;
    private String path;
    private String privilege;
    private String icon;
    private List<MenuItem> menuItems;

    public MenuItem() {
        menuItems = new LinkedList<>();
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }    
}
