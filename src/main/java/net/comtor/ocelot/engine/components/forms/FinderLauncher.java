//package net.comtor.ocelot.engine.components.forms;
//
//import net.comtor.ocelot.bootstrap.commons.BColor;
//import net.comtor.ocelot.bootstrap.components.buttons.BModalLauncherButton;
//import net.comtor.ocelot.bootstrap.forms.BShowField;
//
///**
// *
// * @author Guido Cafiel
// */
//public class FinderLauncher extends BShowField {
//
//    private String myEndpoint;
//    private BModalLauncherButton modalLauncherButton;
//
//    public FinderLauncher(String name, String label, String endpoint) {
//        super(name, label, "", "");
//        this.myEndpoint = endpoint + "/" + name;
//        modalLauncherButton = getModalLauncher(myEndpoint);
//        getFormElementContainer().setStyle("height: 36px");
//        getFormElement().setClass(getControlClass());
//    }
//
//    public FinderLauncher(String name, String label, String hidden, String visible, String endpoint) {
//        super(name, label, hidden, visible);
//        this.myEndpoint = endpoint + "/" + name;
//        modalLauncherButton = getModalLauncher(myEndpoint);
//        getFormElementContainer().setStyle("height: 36px");
//        getFormElement().setClass(getControlClass());
//
//    }
//
//    public FinderLauncher addParamsToEndpoint(String urlParams) {
//        modalLauncherButton.addAttribute("endpoint", myEndpoint + "?" + urlParams);
//        return this;
//    }
//
//    protected BModalLauncherButton getModalLauncher(String urlEndpoint) {
//        BModalLauncherButton modalLauncherButton = new BModalLauncherButton("ocelotModal", "fas fa-search");
//        modalLauncherButton = new BModalLauncherButton("ocelotModal", "fas fa-search");
//        modalLauncherButton.addClass("btn").addClass("btn-primary").addClass("ml-3");
//        modalLauncherButton.setBColor(BColor.PRIMARY);
//        modalLauncherButton.setStyle("float:right");
//        modalLauncherButton.addClass("finderLauncher");
//        modalLauncherButton.addAttribute("endpoint", urlEndpoint);
//        return modalLauncherButton;
//    }
//
//    @Override
//    protected void preHtmlRender() {
//        getFormElementContainer().addFirst(modalLauncherButton);
//        super.preHtmlRender(); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    protected String getControlClass() {
//        return "form-control-ocelot";
//    }
//
//}
