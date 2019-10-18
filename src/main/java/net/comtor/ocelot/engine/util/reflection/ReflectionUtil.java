package net.comtor.ocelot.engine.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author juriel
 */
public class ReflectionUtil {
     public static String invokeGetter(Field field, Object entity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
         try {
             String fieldName = field.getName();
             Method method = entity.getClass().getMethod("get"+StringUtils.capitalize(fieldName));
             return method.invoke(entity) + "";
         } catch (NoSuchMethodException | SecurityException ex) {
             Logger.getLogger(ReflectionUtil.class.getName()).log(Level.SEVERE, null, ex);
         }
         return null;
    }
}
