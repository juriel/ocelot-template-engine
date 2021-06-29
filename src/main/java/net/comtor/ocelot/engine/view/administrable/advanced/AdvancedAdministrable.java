package net.comtor.ocelot.engine.view.administrable.advanced;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.ocelot.engine.commons.MapResponse;
import net.comtor.ocelot.engine.view.administrable.Administrable;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public abstract class AdvancedAdministrable<E, ID extends Serializable> extends Administrable<E, ID> {

    private static final Logger LOG = Logger.getLogger(AdvancedAdministrable.class.getName());

    protected E getEmptyObject() {
        try {
            ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
            Class<E> eClazz = (Class<E>) type.getActualTypeArguments()[0];

            return eClazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            return null;
        }
    }

    @Override
    public String getControllerName() {
        RequestMapping requestMapping = getClass().getAnnotation(RequestMapping.class);
        String controllerName = requestMapping.value()[0];

        if (controllerName.startsWith("/")) {
            controllerName = controllerName.substring(1);
        }

        return controllerName;
    }

    @Override
    public ID getId(E entity) {
        entity = initializeAndUnproxy(entity);
        Class<? extends Object> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            Annotation[] fieldAnnotations = field.getAnnotations();

            for (Annotation fieldAnnotation : fieldAnnotations) {
                if (fieldAnnotation instanceof javax.persistence.Id) {
                    try {
                        return runGetter(field, entity);
                    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
                        LOG.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
            }
        }
        return null;
    }

    public static <T> T initializeAndUnproxy(T entity) {
        if (entity == null) {
            throw new NullPointerException("Entity passed for initialization is null");
        }

        Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer()
                    .getImplementation();
        }
        return entity;
    }

    private ID runGetter(Field field, E entity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String fieldName = field.getName();
        Method[] methods = entity.getClass().getMethods();

        for (Method method : methods) {
            if (method.getName().toLowerCase().equals("get" + fieldName)) {
                return (ID) method.invoke(entity);
            }
        }

        return null;
    }

    @Override
    protected E getEntity(Map<String, String[]> values, E entity) {
        try {
            fillObjectFromValuesMap(values, entity);
        } catch (InvocationTargetException ex) {
            LOG.log(Level.SEVERE, "Es posible que la entidad que está intentando mapear no coincida con los parametros que vienen en el request, sobre escriba el "
                    + "metodo getEntity y haga el mapeo del request de forma acorde a la necesidad que tenga", ex);
        }

        return entity;
    }

    public void fillObjectFromValuesMap(Map<String, String[]> values, E entity) throws InvocationTargetException {
        Class clazz = entity.getClass();

        for (Map.Entry<String, String[]> entry : values.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue()[0];
            Method[] methods = clazz.getMethods();

            for (Method method : methods) {
                if (method.getName().toLowerCase().equals("set" + key)) {
                    try {
                        if (method.getParameterTypes()[0].equals("java.lang.String")) {
                            method.invoke(this, val);
                        } else if (method.getParameterTypes()[0].equals("int") || method.getParameterTypes()[0].equals("java.lang.Integer")) {
                            method.invoke(this, Integer.parseInt(val));
                        } else if (method.getParameterTypes()[0].equals("double") || method.getParameterTypes()[0].equals("java.lang.Double")) {
                            method.invoke(this, Double.parseDouble(val));
                        } else if (method.getParameterTypes()[0].equals("float") || method.getParameterTypes()[0].equals("java.lang.Float")) {
                            method.invoke(this, Float.parseFloat(val));
                        } else if (method.getParameterTypes()[0].equals("long") || method.getParameterTypes()[0].equals("java.lang.Long")) {
                            method.invoke(this, Long.parseLong(val));
                        } else if (method.getParameterTypes()[0].equals("java.sql.Date")) {
                            SimpleDateFormat sdf = new SimpleDateFormat(getDeafultDateFormat());
                            method.invoke(this, new java.sql.Date(sdf.parse(val).getTime()));
                        } else if (method.getParameterTypes()[0].equals("java.util.Date")) {
                            SimpleDateFormat sdf = new SimpleDateFormat(getDeafultDateFormat());
                            method.invoke(this, sdf.parse(val).getTime());
                        }
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ParseException ex) {
                        LOG.log(Level.SEVERE, ex.getMessage(), ex);
                    }

                    break;
                }
            }
        }

    }

    protected abstract String getEntityPhoneticName();

    protected List<MapResponse> getDefaultResponse() {
        List<MapResponse> options = new LinkedList<>();
        options.add(new MapResponse("title", StringUtils.capitalize(getEntityPhoneticName()), MapResponse.TAG));

        return options;
    }

    protected String getEntityName() {
        Class entClazz = getEmptyObject().getClass();

        return entClazz.getSimpleName().toUpperCase();
    }

    @Override
    protected String getActivePrivilege() {
        if (getUniquePrivilege() != null) {
            return getUniquePrivilege();
        }

        return getEntityName() + "_" + getActive();
    }

    protected String getActive() {
        return "ACTIVE";
    }

    @Override
    protected String getCreatePrivilege() {
        if (getUniquePrivilege() != null) {
            return getUniquePrivilege();
        }

        return getEntityName() + "_" + getCreate();
    }

    protected String getCreate() {
        return "CREATE";
    }

    @Override
    protected String getEditPrivilege() {
        if (getUniquePrivilege() != null) {
            return getUniquePrivilege();
        }

        return getEntityName() + "_" + getEdit();
    }

    protected String getEdit() {
        return "EDIT";
    }

    @Override
    protected String getDeletePrivilege() {
        if (getUniquePrivilege() != null) {
            return getUniquePrivilege();
        }

        return getEntityName() + "_" + getDelete();
    }

    protected String getDelete() {
        return "DELETE";
    }

    @Override
    protected String getViewPrivilege() {
        if (getUniquePrivilege() != null) {
            return getUniquePrivilege();
        }

        return getEntityName() + "_" + getView();
    }

    protected String getView() {
        return "VIEW";
    }

    @Override
    protected String getTitle() {
        return "Administración de " + getEntityPhoneticName();
    }

    @Override
    protected String getNewTitle() {
        return "Crear " + getEntityPhoneticName();
    }

    @Override
    protected String getEditTitle() {
        return "Editar " + getEntityPhoneticName();
    }

    @Override
    protected String getDetailTitle() {
        return "Detalle de " + getEntityPhoneticName();
    }

    protected String getUniquePrivilege() {
        return null;
    }

}
