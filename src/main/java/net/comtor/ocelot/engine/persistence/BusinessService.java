package net.comtor.ocelot.engine.persistence;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Size;
import net.comtor.ocelot.engine.exceptions.OcelotException;
import net.comtor.ocelot.html.links.HtmlUl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Guido A. Cafiel Vellojin
 * @param <E>
 * @param <ID>
 */
@Service
public abstract class BusinessService<E, ID extends Serializable> {

    private static final Logger LOG = Logger.getLogger(BusinessService.class.getName());

    @PersistenceContext
    private EntityManager em;

    /**
     * Return class facade to data access
     *
     * @return
     */
    protected abstract MDao<E, ID> getDao();

    /**
     * Reference to id entity
     *
     * @param entity
     * @return
     */
    protected EntityManager getEntityManager() {
        return em;
    }

    protected ID getId(E entity) {
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

    protected ID runGetter(Field field, E entity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String fieldName = field.getName();
        Method[] methods = entity.getClass().getMethods();

        for (Method method : methods) {
            if (method.getName().toLowerCase().equals("get" + fieldName)) {
                return (ID) method.invoke(entity);
            }
        }

        return null;
    }

    /**
     * Return reference to empty object
     *
     * @return
     */
    protected E getEmptyObject() {
        try {
            ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
            Class<E> eClazz = (Class<E>) type.getActualTypeArguments()[0];

            return eClazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            return null;
        }
    }

    /**
     * return collection (List) Entity
     *
     * @return
     */
    public List<E> findAll() {
        return getDao().findAll();
    }

    public Page<E> findAll(int pageNumber, int size) {
        PageRequest page = new PageRequest(pageNumber, size);

        return getDao().findAll(page);
    }

    /**
     * Return one Entity by id
     *
     * @param primaryKey
     * @return
     */
    public E findOne(ID primaryKey) {
        if (primaryKey != null && getDao().existsById(primaryKey)) {
            return getOne(primaryKey);
        }

        return null;
    }

    public E getOne(ID primaryKey) {
        return getDao().getOne(primaryKey);
    }

    /**
     * Save or edit a object
     *
     * @param newE
     * @return
     */
    public E save(E newE) throws OcelotException {
        LinkedHashMap<String, List<String>> errors = validateEntity(newE);

        if (!errors.isEmpty()) {
            throw new OcelotException(errors);
        }

        return getDao().save(newE);
    }

    public LinkedHashMap<String, List<String>> validateEntity(E e) {
        LinkedHashMap<String, List<String>> errorsMap = new LinkedHashMap<>();
        Class clazz = e.getClass();
        Field[] fields = clazz.getDeclaredFields();
        
        for (Field field : fields) {
            if (!field.getType().getName().equals("java.lang.String")) {
                continue;
            }

            List<String> errors = new LinkedList<>();
            
            Arrays.asList(field.getAnnotations()).forEach(annotation -> {
                if (annotation instanceof javax.validation.constraints.Size) {
                    try {
                        Size sizeAnnotation = (javax.validation.constraints.Size) annotation;
                        String value = (String) clazz.getMethod("get" + StringUtils.capitalize(field.getName())).invoke(e);
                       
                        if (value != null) {
                            if (value.length() < sizeAnnotation.min()) {
                                errors.add("El campo debe contener al menos " + sizeAnnotation.min() + " caracter.");
                            }

                            if (value.length() > sizeAnnotation.max()) {
                                errors.add("El campo debe ser menor a " + sizeAnnotation.max() + " caracter.");
                            }
                        }
                    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        LOG.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
            });

            if (!errors.isEmpty()) {
                errorsMap.put(field.getName(), errors);
            }
        }
        
        return errorsMap;
    }

    /**
     * Delete physically one object
     *
     * @param toDeleteE
     * @throws net.comtor.ocelot.engine.exceptions.OcelotException
     */
    public void delete(E toDeleteE) throws OcelotException {
        getDao().delete(toDeleteE);
    }

    public void active(E toActive) throws OcelotException {

    }

    public void edit(E toEditE) throws OcelotException {
        try {
            getDao().save(fillSavedObject(toEditE));
        } catch (ConstraintViolationException ex) {
            HtmlUl ul = new HtmlUl();
            Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

            for (ConstraintViolation<?> violation : violations) {
                ul.addLi(violation.getMessage());
            }

            throw new OcelotException(ul.getHtml());
        }
    }

    public Page<E> getWithFilters(Map<String, String[]> filterValues, Pageable pageable) {
        String filter = getDefaultFilter(filterValues);

        return getTokenSearch(filter, pageable, false);
    }

    public Page<E> getWithFiltersForFinder(Map<String, String[]> filterValues, Pageable pageable) {
        return getWithFilters(filterValues, pageable);
    }

    protected Page<E> getTokenSearch(String search, Pageable pageable, boolean isFinder) {
        EntityManager em = getEntityManager();
        String[] tokens = StringUtils.split(search, " ");

        if (tokens == null) {
            tokens = new String[1];
            tokens[0] = search;
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        E entity = getEmptyObject();
        Class<E> clazz = (Class<E>) entity.getClass();
        CriteriaQuery<E> cq = cb.createQuery(clazz);
        Root<E> myEntity = cq.from(clazz);

        addQueryBody(tokens, clazz, cb, myEntity, isFinder, search, cq, false);
        TypedQuery<E> query = em.createQuery(cq);

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<E> countEntity = countQuery.from(clazz);

        countQuery.select(cb.count(countEntity));
        addQueryBody(tokens, clazz, cb, myEntity, isFinder, search, countQuery, true);
        Long count = em.createQuery(countQuery).getSingleResult();

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(query.getResultList(), pageable, count);
    }

    protected void addQueryBody(String[] tokens, Class<E> clazz, CriteriaBuilder cb,
            Root<E> myEntity, boolean isFinder, String search, CriteriaQuery cq, boolean isCount)
            throws SecurityException {
        List<Predicate> andPredicates = new LinkedList<>();

        for (String token : tokens) {
            List<Predicate> orPredicates = new LinkedList<>();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(javax.persistence.Column.class)) {
                    String canonical = field.getType().getCanonicalName();

                    if (isJavaNumber(canonical) && isNumber(token)) {
                        orPredicates.add(cb.equal(myEntity.get(field.getName()), token));
                    } else if (canonical.equals("java.lang.String")) {
                        orPredicates.add(cb.like(cb.upper(myEntity.get(field.getName())), "%" + token.toUpperCase() + "%"));
                    }
                }
            }

            orPredicates.addAll(getConditions(cb, myEntity, token));

            if (isFinder) {
                orPredicates.addAll(getFinderConditions(cb, myEntity, token));
            }

            andPredicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
        }

        andPredicates.addAll(getPostConditions(cb, myEntity, search));

        if (isFinder) {
            andPredicates.addAll(getFinderPostConditions(cb, myEntity, search));
        }

        cb.and(andPredicates.toArray(new Predicate[0]));
        cq.where(andPredicates.toArray(new Predicate[0]));
        List<javax.persistence.criteria.Order> orderBy = new LinkedList<>();

        if (!isCount) {
            addOrderBy(cb, orderBy, myEntity);

            if (!orderBy.isEmpty()) {
                cq.orderBy(orderBy);
            }
        }
    }

    protected List<Predicate> getConditions(CriteriaBuilder cb, Root<E> root, String token) {
        return new LinkedList<>();
    }

    protected List<Predicate> getPostConditions(CriteriaBuilder cb, Root<E> root, String search) {
        return new LinkedList<>();
    }

    protected void addOrderBy(CriteriaBuilder cb, List<javax.persistence.criteria.Order> orderBy, Root<E> root) {

    }

    public boolean isNumber(String token) {
        try {
            Double number = Double.parseDouble(token);
        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }

    private static boolean isJavaNumber(String canonical) {
        return canonical.equals("java.lang.Integer")
                || canonical.equals("java.lang.Double")
                || canonical.equals("java.lang.Float")
                || canonical.equals("java.lang.Number")
                || canonical.equals("int")
                || canonical.equals("long")
                || canonical.equals("float")
                || canonical.equals("double");
    }

    public abstract E create(E entity) throws OcelotException;

    public E create(E entity, Object[] details) throws OcelotException {
        return null;
    }

    public E edit(E entity, Object[] details) throws OcelotException {
        return null;
    }

    public E fillSavedObject(E toEdit) throws OcelotException {
        E saved = findOne(getId(toEdit));

        if (saved == null) {
            throw new OcelotException("Can't find object to Edit ");
        }

        try {
            Class entClazz = toEdit.getClass();
            Method[] entMethods = entClazz.getMethods();

            for (Method entMethod : entMethods) {
                if (entMethod.getName().startsWith("get") && (entMethod.invoke(toEdit) != null)) {
                    StringBuilder setMet = new StringBuilder(entMethod.getName());
                    setMet.setCharAt(0, 's');

                    for (Method savMethod : entMethods) {
                        if (savMethod.getName().equals(setMet.toString())) {
                            savMethod.invoke(saved, entMethod.invoke(toEdit));
                        }
                    }
                }
            }
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return saved;
    }

    public Long count() {
        return getDao().count();
    }

    protected static boolean isEmpty(String text) {
        return (text == null || text.trim().isEmpty());
    }

    protected static void validateText(String text, String message) throws OcelotException {
        if (isEmpty(text)) {
            throw new OcelotException(message);
        }
    }

    protected static void validateInteger(Integer number, String message) throws OcelotException {
        if (number == null) {
            throw new OcelotException(message);
        }
    }

    protected static void validateDouble(Double number, String message) throws OcelotException {
        if (number == null) {
            throw new OcelotException(message);
        }
    }

    protected static void validateDate(Date date, String message) throws OcelotException {
        if (date == null) {
            throw new OcelotException(message);
        }
    }

    public void validateEntity(E entity, String messageNull, String messageEmpty)
            throws OcelotException {
        if (entity == null) {
            throw new OcelotException(messageNull);
        }

        if (findOne(entity) == null) {
            throw new OcelotException(messageEmpty);
        }

    }

    public void exist(ID primaryKey, String message) throws OcelotException {
        E pivot = findOne(primaryKey);

        if (pivot != null) {
            throw new OcelotException(message);
        }
    }

    public String getDefaultFilter(Map<String, String[]> filterValues) {
        if (filterValues == null) {
            return "";
        }

        return (filterValues.get("filter") == null) ? "" : filterValues.get("filter")[0];
    }

    public E findOne(E entity) {
        return findOne(getId(entity));
    }

    public List<Predicate> getFinderPostConditions(CriteriaBuilder cb, Root<E> root, String search) {
        return new LinkedList<>();
    }

    public List<Predicate> getFinderConditions(CriteriaBuilder cb, Root<E> root, String token) {
        return new LinkedList<>();
    }

}
