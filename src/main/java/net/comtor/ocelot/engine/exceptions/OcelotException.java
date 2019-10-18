package net.comtor.ocelot.engine.exceptions;

import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public class OcelotException extends Exception {

    LinkedHashMap<String, List<String>> errors;

    public OcelotException(String message) {
        super(message);
        errors = new LinkedHashMap<>();
    }

    public OcelotException(LinkedHashMap<String, List<String>> errors) {
        this.errors = errors;
    }

    public LinkedHashMap<String, List<String>> getErrors() {
        return errors;
    }

}
