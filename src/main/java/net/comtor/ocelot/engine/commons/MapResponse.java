package net.comtor.ocelot.engine.commons;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public class MapResponse {

    public static final String REPLACE = "REPLACE";
    public static final String APPEND = "APPEND";
    public static final String DELETE = "DELETE";
    public static final String ERROR = "ERROR";
    public static final String TAG = "TAG";

    private String label;
    private String value;
    private String eventType;

    public MapResponse(String label, String value) {
        this.label = label;
        this.value = value;
        this.eventType = REPLACE;
    }

    public MapResponse(String label, String value, String eventType) {
        this.label = label;
        this.value = value;
        this.eventType = eventType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

}
