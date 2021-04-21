package org.wallride.service;

import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wallride.autoconfigure.WallRideProperties;
import org.wallride.domain.FileDescriptor;
import org.wallride.domain.User;
import org.wallride.exception.FileOperationException;
import org.wallride.support.DateAndTimeUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.UUID;

@Service
public class FileStoreService {

    private static Logger logger = LoggerFactory.getLogger(FileStoreService.class);

    @Autowired
    private FileDescriptorService fileDescriptorService;

    //@Value("${flatshares.public.media}")
    private String publicMedia;

    /**
     * Store a MultipartFile on fileSystem and create a descriptor file about it.
     *
     * @param multipartFile
     * @return The created descriptor file
     */
    public FileDescriptor store(MultipartFile multipartFile, User user) {
        try {
            publicMedia = System.getProperty(WallRideProperties.MEDIA_LOCATION_PROPERTY);
            byte[] bytes = multipartFile.getBytes();
            String uuid = UUID.randomUUID().toString();
            Path path = Paths.get(publicMedia, uuid, multipartFile.getOriginalFilename());
            Tika tika = new Tika();

            File dir = new File(publicMedia, uuid);
            if (!dir.exists()) {
                FileUtils.forceMkdir(dir);
            }
            Files.write(path, bytes);

            FileDescriptor fileDescriptor = new FileDescriptor();

            fileDescriptor.setUuid(uuid);
            fileDescriptor.setOriginalName(multipartFile.getOriginalFilename());
            fileDescriptor.setFilePath(uuid + "/" + multipartFile.getOriginalFilename());
            fileDescriptor.setMimeType(tika.detect(multipartFile.getOriginalFilename()));
            fileDescriptor.setFileSize(Long.valueOf(bytes.length));

            fileDescriptor.setCreatedBy(user == null ? 1 : user.getId());
            fileDescriptor.setModifiedBy(user == null ? 1 : user.getId());

            Timestamp now = DateAndTimeUtil.now();
            fileDescriptor.setCreatedOn(now);
            fileDescriptor.setModifiedOn(now);

            fileDescriptorService.save(fileDescriptor);

            return fileDescriptor;
        } catch (Exception e) {
            throw new FileOperationException("An error has occurred during the file creation!", e);
        }
    }

    public void deleteOnDisk(FileDescriptor fileDescriptor) {
        try {
            java.io.File f = new File(publicMedia, fileDescriptor.getUuid());

            FileUtils.deleteDirectory(f);
        } catch (IOException e) {
            logger.error("An error has occurred during a file deletion on disk", e);
        }
    }

    //TODO ellenőrizni, hogy működik-e
    public java.io.File get(FileDescriptor fileDescriptor) {
        try {
            return new java.io.File(publicMedia, fileDescriptor.getFilePath());
        } catch (Exception e) {
            throw new FileOperationException("An error has occurred during getting the file!", e);
        }
    }
}
