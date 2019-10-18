/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.comtor.ocelot.engine.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Guido Cafiel
 */
public class OcelotDate extends Date {

    public static final int SECONDS = 1000;

    public static final int MINUTES = (SECONDS * 60);

    public static final int HOURS = (MINUTES * 60);

    public static final int DAYS = (HOURS * 24);

    public static final int MONTHS = (DAYS * 30);

    public static final int YEARS = (MONTHS * 12);

    private String defaultFormat = "yyyy-MM-dd";

    public OcelotDate() {
        super();
    }

    public OcelotDate(long millis) {
        super(millis);
    }

    /**
     * Crea una fecha a partir de un string con el formato por defecto
     * yyyy-MM-dd
     *
     * @param format
     * @throws ParseException
     */
    public OcelotDate(String strDate) throws ParseException {
        super(new SimpleDateFormat("yyyy-MM-dd").parse(strDate).getTime());
    }

    public OcelotDate(String format, String strDate) throws ParseException {
        super(new SimpleDateFormat(format).parse(strDate).getTime());
    }

    /**
     * Agrega días a una fecha dando como resultado la fecha con los días
     * agregados.\n
     *
     * Si se quiere restar días se debe proporcionar números negativos, para
     * restar dos días a la fecha se usa -2
     *
     * @param week
     * @return
     */
    public OcelotDate addDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return new OcelotDate(calendar.getTime().getTime());
    }

    /**
     * Agrega semanas a una fecha dando como resultado la fecha con las semanas
     * agregadas.\n
     *
     * Si se quiere restar semanas se debe proporcionar números negativos, para
     * restar dos semanas a la fecha se usa -2
     *
     * @param week
     * @return
     */
    public OcelotDate addWeek(int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this);
        calendar.add(Calendar.WEEK_OF_YEAR, week);
        return new OcelotDate(calendar.getTime().getTime());
    }

    /**
     * Agrega meses a una fecha dando como resultado la fecha con los meses
     * agregados.\n
     *
     * Si se quiere restar meses se debe proporcionar números negativos, para
     * restar dos meses a la fecha se usa -2
     *
     * @param week
     * @return
     */
    public OcelotDate addMonths(int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this);
        calendar.add(Calendar.MONTH, months);
        return new OcelotDate(calendar.getTime().getTime());
    }

    /**
     * Agrega años a una fecha dando como resultado la fecha con los años
     * agregados.\n
     *
     * Si se quiere restar años se debe proporcionar números negativos, para
     * restar dos años a la fecha se usa -2
     *
     * @param years
     * @return
     */
    public OcelotDate addYears(int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this);
        calendar.add(Calendar.YEAR, years);
        return new OcelotDate(calendar.getTime().getTime());
    }

    /**
     * Devuelve la diferencia entre dos fechas expresada en SEGUNDOS, MINUTOS,
     * HORAS, DIAS, MESES, AÑOS.
     *
     * @param date
     * @param diff
     * @return
     */
    public int getDifference(Date date, int diff) {
        double millisDif = this.getTime() - date.getTime();
        if (millisDif < 0) {
            millisDif = millisDif * (-1);
        }

        return (int) (millisDif / (double) diff);
    }

    @Override
    public String toString() {
        return toString(defaultFormat);
    }

    public String toString(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(this);
    }

}
