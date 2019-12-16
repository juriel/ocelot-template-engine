package net.comtor.ocelot.engine.view.advanced.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.comtor.ocelot.bootstrap.commons.BColor;
import net.comtor.ocelot.bootstrap.components.alerts.BAlertDanger;
import net.comtor.ocelot.bootstrap.components.alerts.BAlertInfo;
import net.comtor.ocelot.bootstrap.components.alerts.BAlertSuccess;
import net.comtor.ocelot.bootstrap.forms.inputs.BInputFile;
import net.comtor.ocelot.engine.commons.MapResponse;
import net.comtor.ocelot.engine.components.forms.buttons.PostButton;
import net.comtor.ocelot.engine.components.forms.forms.AjaxForm;
import net.comtor.ocelot.engine.exceptions.OcelotException;
import net.comtor.ocelot.engine.persistence.BusinessService;
import net.comtor.ocelot.engine.util.icons.FontAwesome;
import net.comtor.ocelot.engine.util.poi.PoiHelper;
import net.comtor.ocelot.engine.util.xlsx.XlsxUtils;
import net.comtor.ocelot.engine.view.simple.SimpleView;
import net.comtor.ocelot.html.HtmlContainer;
import net.comtor.ocelot.html.basic.HtmlBr;
import net.comtor.ocelot.html.basic.HtmlHr;
import net.comtor.ocelot.html.basic.HtmlP;
import net.comtor.ocelot.html.forms.HtmlLabel;
import net.comtor.ocelot.html.links.HtmlA;
import net.comtor.ocelot.html.links.HtmlUl;
import net.comtor.ocelot.html.styles.HtmlDiv;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Dec 13, 2019
 */
public abstract class XlsxUploadView<E> extends SimpleView {

    private static final String PARAM_FILE = "file";

    protected static final String TEMPLATE_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    protected static final String DIV_RESULTS = "cont_result";

// FIXME
//    @Autowired
//    public XlsxUtils xlsxHelper;
    @Override
    protected abstract String getTitle();

    protected abstract String getFormName();

    protected abstract String getTemplateFileName();

    protected abstract String[] getTemplateHeaders();

    protected abstract List<XlsxValidatorException> validateRow(final Row row);

    protected abstract E buildEntity(final Row row);

    protected abstract void save(List<E> entities) throws OcelotException;

    protected String getHelpText() {
        return "";
    }

    @Override
    protected void getViewContent(HttpServletRequest request, HtmlContainer container) {
        BAlertInfo info = new BAlertInfo("");
        info.addRawText("<b>Las columnas cuyo encabezado tenga <span style='color: red;'>*</span> deben ser diligenciados obligatoriamente.</b>");
        container.add(info);

        if (StringUtils.isNotEmpty(getHelpText())) {
            HtmlP text = new HtmlP(getHelpText());
            container.add(text);
        }

        HtmlA download = new HtmlA(getControllerName() + "/downloadTemplate", "Descargar plantilla");
        download.addClass("btn btn-info")
                .addAttribute("download", "download");
        container.add(download)
                .add(new HtmlHr());

        AjaxForm form = new AjaxForm(getFormName());
        form.addAttribute("enctype", "multipart/form-data");

        HtmlDiv row = new HtmlDiv();
        row.addClass("row");

        BInputFile file = new BInputFile("Archivo plantilla", PARAM_FILE);
        file.required();
        file.getMainContainer()
                .addClass("col-sm-10");

        PostButton postButton = new PostButton(BColor.PRIMARY, "Cargar", getFormName(), getControllerName() + "/processUpload", true);
        postButton.setIconClass(FontAwesome.Solid.FILE_UPLOAD);
        postButton.addClass("col-sm-12");

        HtmlDiv formGroup = new HtmlDiv();
        formGroup.addClass("form-group col-sm-2");

        HtmlLabel label = new HtmlLabel();
        label.addClass("form-label");
        label.addEscapedText(".");
        label.setStyle("color: white;");

        formGroup.add(label);
        formGroup.add(postButton
                .addClass("form-control")
                .setStyle("color: white; background-color: #17A2B8;"));

        row.add(file)
                .add(formGroup);

        form.add(row);

        container.add(form)
                .add(new HtmlHr())
                .add(new HtmlBr())
                .add(new HtmlDiv(DIV_RESULTS)
                        .setStyle("max-height: 600px; overflow: auto;")
                        .addClass("px-3")
                        .addClass("row"));
    }

    @RequestMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response, HttpServletRequest request) {
        try {
            getDownloadTemplate(response, request);
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/processUpload", method = RequestMethod.POST)
    public List<MapResponse> processUpload(MultipartHttpServletRequest request) {
        return getProcessUpload(request);
    }

    protected void getDownloadTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setContentType(TEMPLATE_CONTENT_TYPE);
        response.setHeader("Content-disposition", "attachment; filename=" + getTemplateFileName());

        XSSFWorkbook workbook = getTemplateFile();

        try (ServletOutputStream os = response.getOutputStream()) {
            workbook.write(os);
            response.flushBuffer();
        }
    }

    protected XSSFWorkbook getTemplateFile() {
        XlsxUtils xlsxHelper = new XlsxUtils();

        return xlsxHelper.getUploadTemplateFile(getTemplateHeaders(), getTitle());
    }

    protected List<MapResponse> getProcessUpload(MultipartHttpServletRequest request) {
        List<MapResponse> response = new LinkedList<>();
        MultipartFile file = request.getFile(PARAM_FILE);

        try (InputStream is = file.getInputStream()) {
            validateMimeType(file);

            XSSFWorkbook workbook = new XSSFWorkbook(is);

            List<E> entities = new LinkedList<>();
            List<XlsxValidatorException> exceptions = validateFile(workbook, entities);

            HtmlContainer container = new HtmlContainer();

            if (!exceptions.isEmpty()) {
                BAlertDanger alert = new BAlertDanger("Se encontraron los siguientes errores en la plantilla:");
                alert.addClass("col-sm-12");

                HtmlUl list = new HtmlUl();
                list.addClass("col-sm-12 my-3");

                exceptions.stream().forEach((exception) -> {
                    list.addLi(String.format("Fila %d, columna %d: %s.", exception.getRow(), exception.getColumn(), exception.getMessage()));
                });

                alert.add(list);

                container.add(alert);

                response.add(new MapResponse(DIV_RESULTS, container.getHtml()));

                return response;
            }

            if (entities.isEmpty()) {
                BAlertDanger alert = new BAlertDanger("La plantilla se encuentra vacía.");
                alert.addClass("col-sm-12");

                response.add(new MapResponse(DIV_RESULTS, alert.getHtml()));

                return response;
            }

            save(entities);

            BAlertSuccess alert = new BAlertSuccess("Se cargaron " + entities.size() + " registros exitosamente.");
            alert.addClass("col-sm-12");

            response.add(new MapResponse(DIV_RESULTS, alert.getHtml()));
        } catch (IOException | OcelotException ex) {
            BAlertDanger danger = new BAlertDanger(ex.getMessage());
            danger.addClass("col-sm-12");

            response.add(new MapResponse(DIV_RESULTS, danger.getHtml()));
        }

        return response;
    }

    private void validateMimeType(MultipartFile multipartFile) throws OcelotException {
        String content = multipartFile.getContentType();

        if (!TEMPLATE_CONTENT_TYPE.equals(content)) {
            throw new OcelotException("Use para la carga la plantilla dispuesta por esta funcionalidad.");
        }
    }

    protected List<XlsxValidatorException> validateFile(XSSFWorkbook workbook, List< E> entities) throws OcelotException {
        Sheet firstSheet = workbook.getSheetAt(0);
        List<XlsxValidatorException> allExceptions = new LinkedList<>();
        List<XlsxValidatorException> rowExceptions;
        Row row;

        for (int i = 1; i <= firstSheet.getLastRowNum(); i++) {
            row = firstSheet.getRow(i);

            rowExceptions = validateRow(row);

            if (rowExceptions == null) {
                throw new OcelotException("La lista de errores es nula.");
            }

            if (rowExceptions.isEmpty()) {
                entities.add(buildEntity(row));
            } else {
                allExceptions.addAll(rowExceptions);
            }
        }

        return allExceptions;
    }

    protected String getColumnValue(final Row row, final int column) {
        return PoiHelper.getStringValue(row.getCell(column), "");
    }

    protected void validateCell(Object entity, BusinessService businessService, boolean notEmpty, boolean isUnique,
            boolean isRequired, String notFoudEntityMessage) throws OcelotException {
        if (notEmpty) {
            if (entity == null) {
                throw new OcelotException("Ingrese un valor para la columna, este valor es obligatorio.");
            }
        }

        if (isUnique) {
            if (businessService.findOne(entity) != null) {
                throw new OcelotException("Ya existe un registro con " + entity);
            }
        }

        if (isRequired) {
            businessService.validateEntity(entity);
        }

    }

}
