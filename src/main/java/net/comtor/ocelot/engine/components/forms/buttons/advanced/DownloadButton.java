package net.comtor.ocelot.engine.components.forms.buttons.advanced;

import net.comtor.ocelot.html.formatting.HtmlIcon;
import net.comtor.ocelot.html.links.HtmlA;
import net.comtor.ocelot.engine.util.icons.FontAwesome;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Guido Cafiel
 */
public class DownloadButton extends HtmlA {

    public DownloadButton(String downloadEndpoint, String value, String style, String icon) {
        super(downloadEndpoint, "");

        addClass("btn").addClass("btn-" + style);
        add(new HtmlIcon().setStyle("margin-right: 5px").addClass(icon));
        addAttribute("download", "download");
        setStyle("margin-right: 5px");

        if (StringUtils.isNotEmpty(value)) {
            addData(value);
        }
    }

    public DownloadButton(String downloadEndpoint, String style, String icon) {
        this(downloadEndpoint, "", style, icon);
    }

    public DownloadButton(String downloadEndpoint) {
        this(downloadEndpoint, "info", FontAwesome.Solid.FILE_DOWNLOAD);
    }

}
