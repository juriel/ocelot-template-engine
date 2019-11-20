package net.comtor.ocelot.engine.view.administrable.advanced;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.html.advanced.HtmlAdministrableForm;
import net.comtor.ocelot.engine.util.reflection.ReflectionUtil;
import net.comtor.ocelot.html.forms.HtmlFormElement;
import net.comtor.ocelot.html.forms.inputs.HtmlInputHidden;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Nov 08, 2019
 */
public final class HtmlFormHelper {

    private static final Logger LOG = Logger.getLogger(HtmlFormHelper.class.getName());

    private HtmlFormHelper() {

    }

    public static void fillForm(HtmlAdministrableForm form, Object entity) {
        addIdField(form, entity);

        Class<? extends Object> clazz = entity.getClass();

        for (Method method : clazz.getMethods()) {
            String methodName = method.getName();

            if (methodName.contains("get")) {
                String fieldName = methodName.substring(3);
                fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);

                HtmlFormElement formField = (HtmlFormElement) form.get(fieldName);

                if (formField != null) {
                    try {
                        //FIXME: NO SE TRAE EL VALOR ASIGNADO EN EL CONTROLLER
                        formField.setValue((method.invoke(entity) == null) ? "" : method.invoke(entity) + "");
                    } catch (Exception ex) {
                        LOG.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
            }
        }

    }

    private static void addIdField(HtmlAdministrableForm form, Object entity) {
        if (entity == null) {
            return;
        }

        Class<? extends Object> clazz = entity.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            for (Annotation annotaion : field.getDeclaredAnnotations()) {
                if (annotaion instanceof javax.persistence.Id) {
                    try {
                        form.add(new HtmlInputHidden(field.getName(), ReflectionUtil.invokeGetter(field, entity)));
                    } catch (Exception ex) {
                        LOG.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
            }
        }
    }
}
