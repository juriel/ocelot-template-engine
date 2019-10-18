package net.comtor.ocelot.engine.util.i18n;

import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/**
 *
 * @author Guido Cafiel
 */
@Component
public class I18nEngine {

    ReloadableResourceBundleMessageSource messageSource;

    public I18nEngine(ReloadableResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

    public String getMessage(String key, Object... objects) {
        Locale locale = LocaleContextHolder.getLocale();
        return String.format(messageSource.getMessage(key, null, locale), objects);
    }

    public String getMessage(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }

    public String getMessage(String key, Locale locale, Object... objects) {
        return String.format(messageSource.getMessage(key, null, locale), objects);
    }

}
