/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.comtor.ocelot.engine.commons.tables;

/**
 *
 * @author Guido Cafiel
 */
public class TableDataElement {

    private Object originalObject;
    private Object insertObject;

    public TableDataElement() {
    }

    public TableDataElement(Object originalObject, Object insertObject) {
        this.originalObject = originalObject;
        this.insertObject = insertObject;
    }

    public TableDataElement(Object originalObject) {
        this.originalObject = originalObject;
        this.insertObject = originalObject;
    }

    public Object getInsertObject() {
        return insertObject;
    }

    public void setInsertObject(Object insertObject) {
        this.insertObject = insertObject;
    }

    public Object getOriginalObject() {
        return originalObject;
    }

    public void setOriginalObject(Object originalObject) {
        this.originalObject = originalObject;
    }

}
