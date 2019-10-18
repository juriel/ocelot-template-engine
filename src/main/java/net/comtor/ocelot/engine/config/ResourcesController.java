package net.comtor.ocelot.engine.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import net.comtor.ocelot.engine.commons.mime.MimeType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public class ResourcesController {

    private final String engine_package_location = "/ocelotengine/files";

    private final String project_package_location = "/project/files";

    @RequestMapping(value = "/ocelot-framework/**", method = RequestMethod.GET)
    public void getFiles(HttpServletResponse response, HttpServletRequest request) {
        String basePath = "/resources/ocelot-framework";
        String path = request.getServletPath();

        if (path.contains("..")) {
            try {
                response.sendError(404, "File Not Found 1 ");
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        path = path.substring(basePath.length());

        OutputStream os = null;
        InputStream is = null;

        try {
            os = response.getOutputStream();
            String resourcePath = project_package_location + path;
            is = getClass().getResourceAsStream(resourcePath);

            if (is == null) {
                resourcePath = engine_package_location + path;
                is = getClass().getResourceAsStream(resourcePath);
            }

            if (is == null) {
                response.sendError(404, "File Not Found 2");
                return;
            }

            response.setContentType(MimeType.getInstance().getValueToFileName(resourcePath));

            byte buff[] = new byte[1024000];
            int readed;

            while ((readed = is.read(buff)) > 0) {
                os.write(buff, 0, readed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                }
            }
        }

    }

    @RequestMapping(value = "/mapana", method = RequestMethod.GET, produces = "text/javascript")
    @ResponseBody
    public byte[] getMapana() throws IOException {
        InputStream fis = new FileInputStream("/PROJECTS/JAVA/SPRING/provet-lab-2/src/main/resources/templates/mapana.js");

        return IOUtils.toByteArray(fis);
    }

    @RequestMapping("error")
    public String toErrorPage() {
        return "/error";
    }

}
