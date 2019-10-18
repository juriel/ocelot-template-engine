package net.comtor.ocelot.engine.security;

import java.util.Collection;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Guido Cafiel
 */
@Component
public class OcelotSecurityManager {

    public static boolean isAuthorized(String privilege) {
        if (privilege == null) {
            return false;
        }

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder
                .getContext().getAuthentication().getAuthorities();
        for (SimpleGrantedAuthority simpleGrantedAuthority : authorities) {
            if (simpleGrantedAuthority.toString().equals("ALL") || simpleGrantedAuthority.toString().equals(privilege)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isActiveView(String privilege) {
        return isAuthorized(privilege) && SessionManager.isActiveView();
    }
}
