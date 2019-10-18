package net.comtor.ocelot.engine.commons;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public class OcelotReflectionHelper {

    public static boolean haveEnabled(Object object) {

        Class entClass = object.getClass();

        try {
            Method method = entClass.getMethod("getEnabled");
            if (method != null) {
                return true;
            }

        } catch (SecurityException | NoSuchMethodException ex) {
            return false;
        }

        return false;
    }

    public static boolean getEnabled(Object object) {

        Class clazz = object.getClass();

        try {
            Method method = clazz.getMethod("getEnabled");

            if (method != null) {
                return ((int) method.invoke(object) == 1);
            }

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | NullPointerException | InvocationTargetException ex) {
            return false;
        } catch (ClassCastException ex) {

        }

        return true;
    }

}
