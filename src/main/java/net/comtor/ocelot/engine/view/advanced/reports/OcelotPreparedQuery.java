package net.comtor.ocelot.engine.view.advanced.reports;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Guido Cafiel
 */
public class OcelotPreparedQuery {

    private final StringBuilder sql;
    private final LinkedList<Object> parameters;
    private final ArrayList<Integer> typesArray;

    public OcelotPreparedQuery(String sql) {
        this.sql = new StringBuilder(sql);
        this.parameters = new LinkedList<>();
        this.typesArray = new ArrayList<>();
    }

    public void appendSql(String sqlPart) {
        sql.append(sqlPart);
    }

    public void addParameter(Object parameter) {
        parameters.add(parameter);
        typesArray.add(getType(parameter));
    }

    public String getSql() {
        return sql.toString();
    }

    public LinkedList<Object> getParameters() {
        return parameters;
    }

    public Object[] getParametersArray() {
        return parameters.toArray();
    }

    public Integer[] getTypesArray() {
        Integer[] arr = new Integer[typesArray.size()];
        
        return typesArray.toArray(arr);
    }

    private int getType(Object object) {
        if (object == null) {
            return Types.NULL;
        }

        Class<?> clazz = object.getClass();

        if (clazz.equals(String.class)) {
            return Types.VARCHAR;
        } else if (clazz.equals(short.class) || clazz.equals(Short.class)) {
            return Types.VARCHAR;
        } else if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
            return Types.NUMERIC;
        } else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
            return Types.NUMERIC;
        } else if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
            return Types.BOOLEAN;
        } else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
            return Types.DOUBLE;
        } else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
            return Types.FLOAT;
        } else if (clazz.equals(char.class) || clazz.equals(Character.class)) {
            return Types.CHAR;
        } else if (clazz.equals(java.sql.Date.class)) {
            return Types.DATE;
        } else if (clazz.equals(java.util.Date.class)) {
            return Types.DATE;
        } else if (clazz.equals(Timestamp.class)) {
            return Types.TIMESTAMP;
        } else if (clazz.equals(Time.class)) {
            return Types.TIME;
        } else if (clazz.equals(byte[].class)) {
            return Types.BIT;
        } else if (clazz.equals(BigDecimal.class)) {
            return Types.FLOAT;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "ReportJDBCPreparedQuery{" 
                + "sql=" + sql 
                + ", parameters=" + parameters 
                + '}';
    }

}
