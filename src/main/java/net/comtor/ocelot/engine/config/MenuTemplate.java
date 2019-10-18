package net.comtor.ocelot.engine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.ocelot.engine.components.menu.MenuItem;
import net.comtor.ocelot.engine.components.menu.AppWorkMenu;
import net.comtor.ocelot.engine.components.menu.OcelotMenu;
import net.comtor.ocelot.engine.security.OcelotSecurityManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author Guido Cafiel
 */
@Component
public class MenuTemplate {

    private static final Logger LOG = Logger.getLogger(MenuTemplate.class.getName());

    private static final String FREE_PRIVILEGE = "FREE";

    public static String loadMenu(OcelotMenu ocelotMenu) {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getMenuIS();
        MenuItem menuJson;

        try {
            menuJson = mapper.readValue(is, MenuItem.class);

            MenuItem filtedMenu = new MenuItem();
            List<MenuItem> filtedItems = new LinkedList<>();

            for (MenuItem itemParent : menuJson.getMenuItems()) {
                if (isAuthorizedModule(itemParent)) {
                    if (!itemParent.getMenuItems().isEmpty()) {
                        itemParent.setMenuItems(filterChildren(itemParent.getMenuItems()));
                    }

                    filtedItems.add(itemParent);
                }
            }

            filtedMenu.setMenuItems(filtedItems);
            ocelotMenu.buildMenu(filtedMenu);

            return ocelotMenu.getHtml();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return "";
    }

    public static String loadMenu() {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getMenuIS();
        MenuItem menuJson;

        try {
            menuJson = mapper.readValue(is, MenuItem.class);

            MenuItem filtedMenu = new MenuItem();
            List<MenuItem> filtedItems = new LinkedList<>();

            for (MenuItem itemParent : menuJson.getMenuItems()) {
                if (isAuthorizedModule(itemParent)) {
                    if (!itemParent.getMenuItems().isEmpty()) {
                        itemParent.setMenuItems(filterChildren(itemParent.getMenuItems()));
                    }

                    filtedItems.add(itemParent);
                }
            }

            filtedMenu.setMenuItems(filtedItems);
            AppWorkMenu menu1 = new AppWorkMenu(filtedMenu);

            return menu1.getHtml();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return "";
    }

    /**
     * Método para cargar el menú JSON en proyectos WAR.
     *
     * @param resourceName Nombre del recurso JSON.
     * @return Menú del archivo JSON
     */
    public String loadMenu(String resourceName) {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getMenuIS(resourceName);
        MenuItem menuJson;

        try {
            menuJson = mapper.readValue(is, MenuItem.class);

            MenuItem filtedMenu = new MenuItem();
            List<MenuItem> filtedItems = new LinkedList<>();

            for (MenuItem itemParent : menuJson.getMenuItems()) {
                if (isAuthorizedModule(itemParent)) {
                    if (!itemParent.getMenuItems().isEmpty()) {
                        itemParent.setMenuItems(filterChildren(itemParent.getMenuItems()));
                    }

                    filtedItems.add(itemParent);
                }
            }

            filtedMenu.setMenuItems(filtedItems);
            AppWorkMenu menu1 = new AppWorkMenu(filtedMenu);

            return menu1.getHtml();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return "";
    }

    protected static List<MenuItem> filterChildren(List<MenuItem> children) {
        List<MenuItem> filtereds = new LinkedList<>();

        for (MenuItem child : children) {
            if (FREE_PRIVILEGE.equals(child.getPrivilege())
                    || OcelotSecurityManager.isAuthorized(child.getPrivilege())) {
                filtereds.add(child);
            }
        }

        return filtereds;
    }

    protected static boolean isAuthorizedModule(MenuItem menuItem) {
        if (!menuItem.getPrivilege().contains(",")) {
            if (FREE_PRIVILEGE.equals(menuItem.getPrivilege())
                    || OcelotSecurityManager.isAuthorized(menuItem.getPrivilege())) {
                return true;
            }
        }

        String[] privileges = StringUtils.split(menuItem.getPrivilege(), ",");

        if (privileges != null) {
            for (String privilege : privileges) {
                if (OcelotSecurityManager.isAuthorized(privilege)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Lee el menú para proyectos JAR
     *
     * @return Menú del archivo JSON
     */
    protected static InputStream getMenuIS() {
        return MenuTemplate.class.getClassLoader().getResourceAsStream("menu.json");
    }

    /**
     * Lee el menú para proyectos WAR
     *
     * @param resourceName Nombre del recurso JSON.
     * @return Menú del archivo JSON
     */
    protected InputStream getMenuIS(String resourceName) {
        return MenuTemplate.class.getClassLoader().getResourceAsStream(resourceName);
    }

}
