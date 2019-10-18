package net.comtor.ocelot.engine.commons.mime;

import java.util.HashMap;

/**
 *
 * @author Diego
 */
public class MimeType {

    private static MimeType instance;
    private HashMap<String, String> map;

    static {
        instance = new MimeType();
    }

    public static MimeType getInstance() {
        return instance;
    }

    private MimeType() {
        map = new HashMap<String, String>();
        map.put("man", "application/x-troff-man");
        map.put("ai", "application/postscript");
        map.put("aif", "audio/x-aiff");
        map.put("aiff", "audio/x-aiff");
        map.put("art", "message/news");
        map.put("asc", "text/plain");
        map.put("asf", "video/x-ms-asf");
        map.put("asx", "video/x-ms-asf");
        map.put("au", "audio/basic");
        map.put("avi", "video/x-msvideo");
        map.put("bcpio", "application/x-bcpio");
        map.put("bin", "application/octet-stream");
        map.put("bmp", "image/bmp");
        map.put("bz2", "application/x-bzip2");
        map.put("cdf", "application/x-netcdf");
        map.put("chrt", "application/x-kchart");

        map.put("cpio", "application/x-cpio");
        map.put("cpt", "application/mac-compactpro");
        map.put("csh", "application/x-csh");
        map.put("css", "text/css");
        map.put("dcr", "application/x-director");
        map.put("dir", "application/x-director");
        map.put("djv", "image/vnd.djvu");
        map.put("djvu", "image/vnd.djvu");
        map.put("dms", "application/octet-stream");
        map.put("doc", "application/msword");
        map.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        map.put("docm", "application/vnd.ms-word.document.macroEnabled.12");
        map.put("docm", "application/vnd.ms-word.document.macroEnabled.12");
        map.put("dvi", "application/x-dvi");
        map.put("eml", "message/rfc822");
        map.put("ent", "text/xml-external-parsed-entity");
        map.put("eps", "application/postscript");
        map.put("etx", "text/x-setext");

        map.put("exe", "application/octet-stream");
        map.put("ez", "application/andrew-inset");

        map.put("fla", "application/octet-stream");
        map.put("flv", "video/x-flv");
        map.put("fm", "application/vnd.framemaker");
        map.put("fts", "image/x-fits");
        map.put("gif", "image/gif");
        map.put("gtar", "application/x-gtar");
        map.put("gz", "application/x-gzip");
        map.put("hdf", "application/x-hdf");
        map.put("hqx", "application/mac-binhex40");
        map.put("html", "text/html");
        map.put("htm", "text/html");
        map.put("ice", "x-conference/x-cooltalk");
        map.put("ico", "image/vnd.microsoft.icon");
        map.put("ief", "image/ief");
        map.put("iges", "model/iges");
        map.put("igs", "model/iges");
        map.put("jad", "text/vnd.sun.j2me.app-descriptor");
        map.put("jar", "application/x-java-archive");
        map.put("jnlp", "application/x-java-jnlp-file");
        map.put("jpeg", "image/jpeg");
        map.put("jpg", "image/jpeg");
        map.put("js", "application/x-javascript");
        map.put("kil", "application/x-killustrator");
        map.put("kpr", "application/x-kpresenter");
        map.put("kpt", "application/x-kpresenter");
        map.put("ksp", "application/x-kspread");
        map.put("kwd", "application/x-kword");
        map.put("kwt", "application/x-kword");
        map.put("latex", "application/x-latex");
        map.put("m3u", "audio/x-mpegurl");
        map.put("mail", "message/rfc822");
        map.put("man", "application/x-troff-man");
        map.put("me", "application/x-troff-me");
        map.put("mesh", "model/mesh");
        map.put("mid", "audio/midi");
        map.put("midi", "audio/midi");
        map.put("mif", "application/vnd.mif");
        map.put("movie", "video/x-sgi-movie");
        map.put("mov", "video/quicktime");
        map.put("mp2", "audio/mpeg");
        map.put("mp3", "audio/mpeg");
        map.put("mp4", "video/mp4");
        map.put("mpeg", "video/mpeg");
        map.put("mpga", "audio/mpeg");
        map.put("mpg", "video/mpeg");
        map.put("ms", "application/x-troff-ms");
        map.put("msh", "model/mesh");
        map.put("mxu", "video/vnd.mpegurl");
        map.put("nc", "application/x-netcdf");
        map.put("oda", "application/oda");
        map.put("odb", "application/vnd.oasis.opendocument.database");
        map.put("odc", "application/vnd.oasis.opendocument.chart");
        map.put("odf", "application/vnd.oasis.opendocument.formula");
        map.put("odg", "application/vnd.oasis.opendocument.graphics");
        map.put("odi", "application/vnd.oasis.opendocument.image");
        map.put("odm", "application/vnd.oasis.opendocument.text-master");
        map.put("odp", "application/vnd.oasis.opendocument.presentation");
        map.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
        map.put("odt", "application/vnd.oasis.opendocument.text");
        map.put("ogg", "application/ogg");
        map.put("otg", "application/vnd.oasis.opendocument.graphics-template");
        map.put("oth", "application/vnd.oasis.opendocument.text-web");
        map.put("otp", "application/vnd.oasis.opendocument.presentation-template");
        map.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template");
        map.put("ott", "application/vnd.oasis.opendocument.text-template");
        map.put("pbm", "image/x-portable-bitmap");
        map.put("pdb", "chemical/x-pdb");
        map.put("pdf", "application/pdf");
        map.put("pgm", "image/x-portable-graymap");
        map.put("pgn", "application/x-chess-pgn");
        map.put("pgp", "application/pgp-encrypted");
        map.put("pl", "application/x-perl");
        map.put("png", "image/png");
        map.put("pnm", "image/x-portable-anymap");
        map.put("pod", "text/x-pod");
        map.put("ppm", "image/x-portable-pixmap");
        map.put("ppt", "application/vnd.ms-powerpoint");
        map.put("qt", "video/quicktime");
        map.put("ra", "audio/x-realaudio");
        map.put("ram", "audio/x-pn-realaudio");
        map.put("ras", "image/x-cmu-raster");

        map.put("rgb", "image/x-rgb");
        map.put("rm", "audio/x-pn-realaudio");
        map.put("rpm", "application/x-rpm");
        map.put("rtf", "application/rtf");
        map.put("rtf", "text/rtf");
        map.put("rtx", "text/richtext");
        map.put("sgml", "text/sgml");
        map.put("sgm", "text/sgml");
        map.put("sh", "application/x-sh");
        map.put("shar", "application/x-shar");
        map.put("sis", "application/vnd.symbian.install");
        map.put("sit", "application/x-stuffit");
        map.put("skd", "application/x-koan");
        map.put("skp", "application/x-koan");
        map.put("smi", "application/smil");
        map.put("smil", "application/smil");
        map.put("snd", "audio/basic");
        map.put("spl", "application/x-futuresplash");
        map.put("src", "application/x-wais-source");
        map.put("stc", "application/vnd.sun.xml.calc.template");
        map.put("std", "application/vnd.sun.xml.draw.template");
        map.put("sti", "application/vnd.sun.xml.impress.template");
        map.put("stw", "application/vnd.sun.xml.writer.template");
        map.put("sv4cpio", "application/x-sv4cpio");
        map.put("sv4crc", "application/x-sv4crc");
        map.put("swf", "application/x-shockwave-flash");
        map.put("sxc", "application/vnd.sun.xml.calc");
        map.put("sxd", "application/vnd.sun.xml.draw");
        map.put("sxg", "application/vnd.sun.xml.writer.global");
        map.put("sxi", "application/vnd.sun.xml.impress");
        map.put("sxm", "application/vnd.sun.xml.math");
        map.put("sxw", "application/vnd.sun.xml.writer");
        map.put("tar", "application/x-tar");
        map.put("tcl", "application/x-tcl");
        map.put("tex", "application/x-tex");
        map.put("texi", "application/x-texinfo");
        map.put("texinfo", "application/x-texinfo");
        map.put("tga", "image/x-targa");
        map.put("tgz", "application/x-gzip");
        map.put("tiff", "image/tiff");
        map.put("tif", "image/tiff");
        map.put("torrent", "application/x-bittorrent");
        map.put("tr", "application/x-troff");
        map.put("tsv", "text/tab-separated-values");
        map.put("txt", "text/plain");

        map.put("ustar", "application/x-ustar");
        map.put("vcd", "application/x-cdlink");
        map.put("vcf", "text/x-vcard");
        map.put("vrml", "model/vrml");
        map.put("wav", "audio/x-wav");
        map.put("wax", "audio/x-ms-wax");
        map.put("wbmp", "image/vnd.wap.wbmp");
        map.put("wbxml", "application/vnd.wap.wbxml");
        map.put("wma", "audio/x-ms-wma");
        map.put("wmlc", "application/vnd.wap.wmlc");
        map.put("wmlsc", "application/vnd.wap.wmlscriptc");
        map.put("wmls", "text/vnd.wap.wmlscript");
        map.put("wml", "text/vnd.wap.wml");
        map.put("wm", "video/x-ms-wm");
        map.put("wmv", "video/x-ms-wmv");
        map.put("wmx", "video/x-ms-wmx");
        map.put("wrl", "model/vrml");
        map.put("wvx", "video/x-ms-wvx");
        map.put("xbm", "image/x-xbitmap");
        map.put("xht", "application/xhtml+xml");
        map.put("xhtml", "application/xhtml+xml");
        map.put("xls", "application/vnd.ms-excel");
        map.put("xml", "text/xml");
        map.put("xpm", "image/x-xpixmap");
        map.put("xsl", "text/xml");
        map.put("xwd", "image/x-xwindowdump");
        map.put("xyz", "chemical/x-xyz");
        map.put("zip", "application/zip");
        map.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        map.put("docm", "application/vnd.ms-word.document.macroEnabled.12");
        map.put("dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
        map.put("dotm", "application/vnd.ms-word.template.macroEnabled.12");
        map.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        map.put("xlsm", "application/vnd.ms-excel.sheet.macroEnabled.12");
        map.put("xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
        map.put("xltm", "application/vnd.ms-excel.template.macroEnabled.12");
        map.put("xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
        map.put("xlam", "application/vnd.ms-excel.addin.macroEnabled.12");
        map.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        map.put("pptm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
        map.put("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");
        map.put("ppsm", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
        map.put("potx", "application/vnd.openxmlformats-officedocument.presentationml.template");
        map.put("potm", "application/vnd.ms-powerpoint.template.macroEnabled.12");
        map.put("ppam", "application/vnd.ms-powerpoint.addin.macroEnabled.12");
        map.put("sldx", "application/vnd.openxmlformats-officedocument.presentationml.slide");
        map.put("sldm", "application/vnd.ms-powerpoint.slide.macroEnabled.12");
        map.put("one", "application/onenote");
        map.put("onetoc2", "application/onenote");
        map.put("onetmp", "application/onenote");
        map.put("onepkg", "application/onenote");
        map.put("thmx", "application/vnd.ms-officetheme");
    }

    public String getExt(String fileName) {
        String ext = "";
        int index = fileName.lastIndexOf(".");
        ext = fileName.substring(index + 1, fileName.length());

        if (index == -1) {
            return "";
        }
        if (ext.indexOf("/") != -1 || ext.indexOf("\\") != -1 || ext.indexOf(":") != -1) {
            return "";
        }
        
        return ext;
    }    

    public String getValueToFileName(String fileName) {        
        String ext = getExt(fileName);
        ext = ext.toLowerCase();        
        return getValue(ext);
    }

    public String getValue(String key) {
        return map.get(key);
    }
}
