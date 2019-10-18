package net.comtor.ocelot.engine.components.forms.forms.advanced;

import net.comtor.ocelot.bootstrap.decorators.BootstrapFormElement;

/**
 *
 * @author Guido Cafiel
 */
public class OcelotFormObject {

    private BootstrapFormElement boostrapFormElement;
    private String defaultValue;
    private String defaultVisible;
    private boolean isId;

    public OcelotFormObject(BootstrapFormElement boostrapFormElement, String defaultValue, String defaultVisible, boolean isId) {
        this.boostrapFormElement = boostrapFormElement;
        this.defaultValue = defaultValue;
        this.defaultVisible = defaultVisible;
        this.isId = isId;
    }

    public OcelotFormObject(BootstrapFormElement boostrapFormElement, String defaultValue, boolean isId) {
        this.boostrapFormElement = boostrapFormElement;
        this.defaultValue = defaultValue;
        this.defaultVisible = null;
        this.isId = isId;
    }

    public OcelotFormObject(BootstrapFormElement boostrapFormElement, boolean isId) {
        this.boostrapFormElement = boostrapFormElement;
        this.defaultValue = null;
        this.defaultVisible = null;
        this.isId = isId;
    }

    public BootstrapFormElement getBoostrapFormElement() {
        return boostrapFormElement;
    }

    public void setBoostrapFormElement(BootstrapFormElement boostrapFormElement) {
        this.boostrapFormElement = boostrapFormElement;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultVisible() {
        return defaultVisible;
    }

    public void setDefaultVisible(String defaultVisible) {
        this.defaultVisible = defaultVisible;
    }

    public boolean isIsId() {
        return isId;
    }

    public void setIsId(boolean isId) {
        this.isId = isId;
    }

}
