package net.comtor.ocelot.engine.commons.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import net.comtor.ocelot.engine.exceptions.OcelotException;
import net.comtor.ocelot.engine.persistence.BusinessService;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Guido Cafiel
 * @param <E>
 * @param <ID>
 */
public abstract class FileManager<E, ID extends Serializable> {

    protected abstract BusinessService<E, ID> getBusinessService();

    protected abstract String getRootDirectory();

    protected abstract E getEntity(String id, String originalName, String contentType);

    protected abstract String getContentType(E entity);

    protected abstract String getOriginalFileName(E entity);

    protected int getDirectoryLevels() {
        return 5;
    }

    public static class FileEntry {

        private final byte[] bytes;

        private final String originalName;

        private final String contentType;

        public FileEntry(byte[] bytes, String contentType, String originalName) {
            this.bytes = bytes;
            this.contentType = contentType;
            this.originalName = originalName;
        }

        public String getContentType() {
            return (this.contentType).trim();
        }

        public String getOriginalName() {
            return this.originalName;
        }

        public byte[] getBytes() {
            return this.bytes;
        }

    }

    private String rootDirectory;

    public void setRoot() {
        this.rootDirectory = getRootDirectory();
        File rootFile = new File(rootDirectory);
        if (!rootFile.exists()) {
            try {
                FileUtils.forceMkdir(rootFile);
            } catch (IOException e) {
                throw new RuntimeException("No es posible crear el directorio: " + rootDirectory);
            }
        }
    }

    public String save(byte[] bytes, String contentType, String originalName) throws IOException, OcelotException {
        String id = null;
        File file = null;
        do {
            id = newId();
            file = new File(getPath(id));
        } while (file.exists());

        FileUtils.writeByteArrayToFile(file, bytes);
        getBusinessService().save(getEntity(id, originalName, contentType));

        return id;
    }

    public FileEntry read(String id) throws IOException {
        String path = getPath(id);
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("No se encuentra el archivo: " + path);
        }
        byte[] bytes = FileUtils.readFileToByteArray(file);

        E entity = (E) getBusinessService().findOne((ID) id);

        String contentType = getContentType(entity);
        String originalName = getOriginalFileName(entity);

        return new FileEntry(bytes, contentType, originalName);
    }

    private String getPath(String id) {
        StringBuilder b = new StringBuilder(getRootDirectory());
        for (int i = 0; i < getDirectoryLevels(); i++) {
            b.append("/").append(id.charAt(i));
        }
        b.append("/").append(id);
        return b.toString();
    }

    public final static String newId() {
        return UUID.randomUUID().toString().replaceAll("UUID-", "");
    }

}
