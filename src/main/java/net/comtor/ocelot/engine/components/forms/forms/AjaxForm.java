package net.comtor.ocelot.engine.components.forms.forms;

import net.comtor.html.advanced.AbstractHtmlAdministrableFormElement;
import net.comtor.html.advanced.HtmlAdministrableForm;
import net.comtor.ocelot.html.forms.HtmlFormElement;

/**
 *
 * @author Guido Cafiel
 */
public class AjaxForm extends AbstractHtmlAdministrableFormElement {

    public AjaxForm(String name) {
        setName(name);
        setId(name);
        addClass("ajaxForm");

    }

    @Override
    public HtmlAdministrableForm addField(String id, String label, HtmlFormElement input, String help, String error) {
        this.addField(id, label, input, help, error);

        return this;
    }

    public AjaxForm addSubmitWithIntro() {
        addAttribute("validate-intro", "true");

        return this;
    }

//    private LinkedHashMap<String, OcelotFormObject> formElements;
//
//    public AjaxForm(String name) {
//        super(name);
//        setId(name);
//        addClass("ajaxForm");
//        formElements = new LinkedHashMap<>();
//    }
//
//    public AjaxForm addMultipart() {
//        addAttribute("enctype", "multipart/form-data");;
//        return this;
//    }
//
//   public void addInputHidden(String name, String value){
//       add(new HtmlInputHidden(name, value));
//   }
//    public void addIdField(BootstrapFormElement formElement) {
//        this.addField(formElement, null, null, true);
//    }
//
//    public void addIdField(BootstrapFormElement formElement, String smallMessage) {
//        this.addField(formElement, smallMessage, null, true);
//    }
//
//    public void addIdField(BootstrapFormElement formElement, String smallMessage, Object defaultValue) {
//        this.addField(formElement, smallMessage, defaultValue, true);
//    }
//
//    public void addField(BootstrapFormElement formElement) {
//        this.addField(formElement, null, null, false);
//    }
//
//    public void addField(BootstrapFormElement formElement, String smallMessage) {
//        this.addField(formElement, smallMessage, null, false);
//    }
//
//    public void addField(BootstrapFormElement formElement, String smallMessage, Object defaultValue, boolean isId) {
//        if (smallMessage != null) {
//            formElement.setSmallMessage(smallMessage);
//        }
//
//        add(formElement);
//
//        if (defaultValue != null) {
//            formElements.put(formElement.getName(), new OcelotFormObject(formElement, defaultValue + "", isId));
//        } else {
//            formElements.put(formElement.getName(), new OcelotFormObject(formElement, isId));
//        }
//
//    }
//
//    public void fillForm(Object object) {
//        addId(object);
//        Class<? extends Object> clazz = object.getClass();
//        Method[] methods = clazz.getMethods();
//        for (Method method : methods) {
//            if (method.getName().contains("get")) {
//                String fieldName = method.getName().substring(3);
//                fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
//                if (formElements.containsKey(fieldName)) {
//                    String defaultValue = formElements.get(fieldName).getDefaultValue();
//                    if (defaultValue != null) {
//                        formElements.get(fieldName).getBoostrapFormElement().setValue(defaultValue);
//                    } else {
//                        try {
//                            formElements.get(fieldName).getBoostrapFormElement().setValue(method.invoke(object) != null ? method.invoke(object) + "" : "");
//
//                            if (formElements.get(fieldName).isIsId()) {
//                                formElements.get(fieldName).getBoostrapFormElement().readOnly();
//                            }
//
//                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
//                            Logger.getLogger(AjaxForm.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                }
//            }
//        }
//
//    }
//
//    public void addId(Object entity) {
//        if (entity == null) {
//            return;
//        }
//        Class<? extends Object> clazz = entity.getClass();
//        Field[] fields = clazz.getDeclaredFields();
//        System.out.println("ADD ID" + clazz);
//
//        for (Field field : fields) {
//            Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
//            for (Annotation fieldAnnotation : fieldAnnotations) {
//                System.out.println("ANNOT " + fieldAnnotation);
//                if (fieldAnnotation instanceof javax.persistence.Id) {
//                    System.out.println("ANNOT javax.persistence.Id");
//
//                    try {
//                        System.out.println("A");
//
//                        add(new HtmlInputHidden(field.getName(), ReflectionUtil.invokeGetter(field, entity)));
//                        System.out.println("B");
//
//                    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
//                        Logger.getLogger(AjaxForm.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
//        }
//    }
//
//    public AjaxForm addSubmitWithIntro() {
//        addAttribute("validate-intro", "true");
//        return this;
//    }
}
