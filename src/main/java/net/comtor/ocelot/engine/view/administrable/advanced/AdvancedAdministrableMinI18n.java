package net.comtor.ocelot.engine.view.administrable.advanced;

import java.io.Serializable;
import java.util.Locale;
import net.comtor.ocelot.engine.util.i18n.I18nEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author Guido Cafiel
 * @param <E>
 * @param <ID>
 */
public abstract class AdvancedAdministrableMinI18n<E, ID extends Serializable> extends AdvancedAdministrable<E, ID> {

    @Autowired
    private I18nEngine i18nEngine;

    @Override
    protected String getTitle() {
        return getMessageI18n("galileo.administrable.title", getEntityPhoneticName());
    }

    @Override
    protected String getNewTitle() {
        return getMessageI18n("galileo.administrable.new_entity", getEntityName());
    }

    @Override
    protected String getEditTitle() {
        return getMessageI18n("galileo.administrable.edit_entity", getEntityName());
    }

    protected String getMessageI18n(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return i18nEngine.getMessage(key, locale);
    }

    protected String getMessageI18n(String key, Object... params) {
        Locale locale = LocaleContextHolder.getLocale();
        return i18nEngine.getMessage(key, locale, params);
    }

    @Override
    protected String getSearchTitle() {
        return getMessageI18n("galileo.administrable.search");
    }

    @Override
    protected String getNewButtonTitle() {
        return getMessageI18n("galileo.administrable.new");
    }

    @Override
    protected String getOptionTitle() {
        return getMessageI18n("galileo.administrable.options");
    }

    @Override
    protected String getEditButtonTitle() {
        return getMessageI18n("galileo.administrable.edit");
    }

    @Override
    protected String getNoPrivilegesAlertTitle() {
        return getMessageI18n("galileo.administrable.no_privileges");
    }

    @Override
    protected String getBackButtonTitle() {
        return getMessageI18n("galileo.administrable.back");
    }

    @Override
    protected String getSaveButtonTitle() {
        return getMessageI18n("galileo.administrable.save");
    }

    @Override
    protected String getSavedSuccessMessage(E entity) {
        return getMessageI18n("galileo.administrable.saveSuccesful");
    }

    @Override
    protected String getEditedSuccessMessage(E entity) {
        return getMessageI18n("galileo.administrable.editSuccesful");
    }

    @Override
    protected String getSearchNoResultsTitle() {
        return getMessageI18n("galileo.administrable.no_results_search");
    }

    @Override
    protected String getDeleteSuccesfulTitle() {
        return getMessageI18n("galileo.administrable.deleteSuccesful");
    }

    @Override
    protected String getActiveSuccesfulTitle() {
        return getMessageI18n("galileo.administrable.activeSuccesful");
    }

    @Override
    protected String getFilterTitle() {
        return getMessageI18n("galileo.administrable.filter");
    }

    @Override
    protected String getResultsTitle() {
        return getMessageI18n("galileo.administrable.results");
    }

    @Override
    protected String getDeleteButtonTitle() {
        return getMessageI18n("galileo.administrable.delete");
    }

    @Override
    protected String getActiveButtonTitle() {
        return getMessageI18n("galileo.administrable.active");
    }

    @Override
    protected String getMessageResultPaginator() {
        return getMessageI18n("galileo.administrable.message_paginator");
    }

    @Override
    protected String getMessageNextPaginator() {
        return getMessageI18n("galileo.administrable.next");
    }

    @Override
    protected String getMessagePrevious() {
        return getMessageI18n("galileo.administrable.previous");
    }

    protected String getEntityName() {
        return null;
    }

    @Override
    protected String getDeleteWarningMessage() {
        return getMessageI18n("galileo.mercury.delete_msg");
    }

    @Override
    protected String getActiveWarningMessage() {
        return getMessageI18n("galileo.mercury.activate_msg");
    }

    @Override
    protected String getDeleteButtonConfirmLabel() {
        return getMessageI18n("galileo.mercury.option_confirm");
    }

    @Override
    protected String getDeleteButtonCancelLabel() {
        return getMessageI18n("galileo.mercury.option_cancel");
    }

}
