package net.comtor.ocelot.engine.components.forms.forms;

import java.util.Map;
import net.comtor.ocelot.engine.administrable.HtmlAdministrableForm;
import net.comtor.ocelot.html.formatting.HtmlSmall;
import net.comtor.ocelot.html.forms.HtmlForm;
import net.comtor.ocelot.html.forms.HtmlFormElement;
import net.comtor.ocelot.html.forms.HtmlLabel;
import net.comtor.ocelot.html.forms.inputs.HtmlInputHidden;
import net.comtor.ocelot.html.styles.HtmlDiv;
import net.comtor.ocelot.html.styles.HtmlSpan;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Nov 07, 2019
 */
//public class ComtorForm extends HtmlForm implements HtmlAdministrableForm {
// TODO: Agregar wrapper

//    @Override
//    public void addInputHidden(String name, String value) {
//        add(new HtmlInputHidden(name, value));
//    }
//
//    @Override
//    public void addField(String id, String label, HtmlFormElement input) {
//        input.setId(id);
//
//        addField(label, input);
//    }
//
//    @Override
//    public void addField(String label, HtmlFormElement input) {
//        addField(label, input, "");
//    }
//
//    @Override
//    public void addField(String label, HtmlFormElement input, String help) {
//        addField(label, input, help, "");
//    }
//
//    @Override
//    public void addField(String label, HtmlFormElement input, String help, String error) {
//        HtmlLabel l = new HtmlLabel(input.getId(), label);
//        HtmlSmall hint = new HtmlSmall(help);
//        HtmlSpan e = new HtmlSpan(error);
//
//        HtmlDiv container = new HtmlDiv();
//        container.addAll(l, input, hint, e);
//
//        add(container);
//    }
//
//    @Override
//    public HtmlFormElement getInput(String name) {
//        for (Map.Entry<String, HtmlFormElement> entrySet : getFormElements().entrySet()) {
//            HtmlFormElement value = entrySet.getValue();
//
//            if (value.getName().equals(name)) {
//                return value;
//            }
//        }
//
//        return null;
//    }
//
//    @Override
//    public void setAction(String action) {
//        addAttribute("action", action);
//    }
//
//    @Override
//    public String getAction() {
//        return getAttribute("action");
//    }
//
//    @Override
//    public void setName(String name) {
//        addAttribute("name", name);
//    }
//
//    @Override
//    public String getName() {
//        return getAttribute("name");
//    }
//
//    @Override
//    public void setEnctType(String encType) {
//        addAttribute("encType", encType);
//    }

//}
