package net.comtor.ocelot.engine.view.administrable.advanced;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.ocelot.engine.util.reflection.ReflectionUtil;
import net.comtor.ocelot.html.forms.HtmlForm;
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

    public static void fillForm(HtmlForm form, Object object) {
        addId(form, object);

        Class<? extends Object> clazz = object.getClass();
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            if (method.getName().contains("get")) {
                String fieldName = method.getName().substring(3);
                fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);

                LinkedHashMap<String, HtmlFormElement> formElements = form.getFormElements();

                if (formElements.containsKey(fieldName)) {
                    HtmlFormElement formElement = formElements.get(fieldName);
                    String defaultValue = formElement.getDefaultValue();

                    if (defaultValue == null) {
                        try {
                            formElement.setValue((method.invoke(object) == null) ? "" : method.invoke(object) + "");

                            if (formElement.isIsId()) {
                                formElement.getBoostrapFormElement().readOnly();
                            }

                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                            LOG.log(Level.SEVERE, ex.getMessage(), ex);
                        }
                    } else {
                        formElement.getBoostrapFormElement().setValue(defaultValue);
                    }
                }
            }
        }

    }

    private static void addId(HtmlForm form, Object entity) {
        if (entity == null) {
            return;
        }

        Class<? extends Object> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            Annotation[] fieldAnnotations = field.getDeclaredAnnotations();

            for (Annotation fieldAnnotation : fieldAnnotations) {
                if (fieldAnnotation instanceof javax.persistence.Id) {
                    try {
                        form.add(new HtmlInputHidden(field.getName(), ReflectionUtil.invokeGetter(field, entity)));
                    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
                        LOG.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
            }
        }
    }

}
