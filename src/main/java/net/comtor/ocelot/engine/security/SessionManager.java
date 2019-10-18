package net.comtor.ocelot.engine.security;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
@Component
public class SessionManager {

    public final static String INACTIVE_VIEW = "INACTIVE_VIEW";

    public static HttpSession session() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }

    public static void setAttribute(String key, Object value) {
        session().setAttribute(key, value);
    }

    public static Object getAttribute(String key) {
        return session().getAttribute(key);
    }

    public static void activeInactivesView() {
        setAttribute(INACTIVE_VIEW, "Y");
    }

    public static void desactiveInactivesView() {
        setAttribute(INACTIVE_VIEW, "N");
    }

    public static boolean isActiveView() {
        if (getAttribute(INACTIVE_VIEW) == null || ((String) getAttribute(INACTIVE_VIEW)).equals("N")) {
            return false;
        }

        return true;
    }

}
