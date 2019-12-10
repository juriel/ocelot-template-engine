package net.comtor.ocelot.engine.view;

import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.comtor.ocelot.bootstrap.commons.BColor;
import net.comtor.ocelot.engine.commons.MapResponse;
import net.comtor.ocelot.engine.components.forms.buttons.AjaxButton;
import net.comtor.ocelot.engine.components.forms.buttons.GetButton;
import net.comtor.ocelot.engine.util.icons.FontAwesome;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public abstract class WebView {

    protected abstract String getTitle();

    @ResponseBody
    @RequestMapping("")
    protected abstract List<MapResponse> mainView(HttpServletRequest request);

    public String getControllerName() {
        Class<? extends WebView> clazz = this.getClass();
        Annotation[] anotations = clazz.getAnnotations();

        for (Annotation annotation : anotations) {
            if (annotation instanceof org.springframework.web.bind.annotation.RequestMapping) {
                RequestMapping requestMappingAn = (RequestMapping) annotation;
                String controllerName = requestMappingAn.value()[0];

                if (controllerName.startsWith("/")) {
                    controllerName = controllerName.substring(1);
                }

                return controllerName;
            }
        }

        return null;
    }

    protected String getOriginController() {
        return null;
    }

    protected AjaxButton getBackButton(HttpServletRequest request) {
        AjaxButton backButton = new GetButton(BColor.WARNING, getBackButtonTitle(),
                getEndpointFormat(getOriginController()));
        backButton.setIconClass(FontAwesome.Solid.ARROW_ALT_CIRCLE_LEFT);
        backButton.setStyle("float: right");

        return backButton;
    }

    protected String getBackButtonTitle() {
        return "Regresar";
    }

    protected abstract void addJsResources(List<String> paths);

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(true);
    }

    protected String getNoPrivilegesAlertTitle() {
        return "No posee privilegios para realizar esta acción";
    }

    private String getViewParams() {
        String path = "";
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        addViewParams(params);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            path += key + "=" + val + "&";
        }

        if (!path.trim().isEmpty()) {
            path = "?" + path.substring(0, path.length());
        }

        return path;
    }

    protected void addViewParams(LinkedHashMap<String, String> params) {

    }

    protected String getEndpointFormat(String url) {
        return url + getViewParams();
    }

}
