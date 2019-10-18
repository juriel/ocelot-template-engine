package net.comtor.ocelot.engine.view.administrable.advanced;

import java.io.Serializable;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public abstract class AdvancedAdministrableEs<E, ID extends Serializable> extends AdvancedAdministrable<E, ID> {

    @Override
    protected String getActive() {
        return "ACTIVAR";
    }

    @Override
    protected String getView() {
        return "VER";
    }

    @Override
    protected String getCreate() {
        return "CREAR";
    }

    @Override
    protected String getEdit() {
        return "EDITAR";
    }

    @Override
    protected String getDelete() {
        return "ELIMINAR";
    }

    @Override
    protected String getTitle() {
        return "Administración de " + getEntityPhoneticName();
    }

    @Override
    protected String getNewTitle() {
        return "Nuevo " + getEntityPhoneticName();
    }

    @Override
    protected String getEditTitle() {
        return "Editar " + getEntityPhoneticName();
    }
}
