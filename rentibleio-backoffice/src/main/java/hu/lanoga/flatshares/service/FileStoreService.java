package hu.lanoga.flatshares.service;

import hu.lanoga.flatshares.exception.FileOperationException;
import hu.lanoga.flatshares.model.FileDescriptor;
import hu.lanoga.flatshares.model.User;
import hu.lanoga.flatshares.util.DateAndTimeUtil;
import hu.lanoga.flatshares.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.UUID;


@Slf4j
@Service
public class FileStoreService {

    private final FileDescriptorService fileDescriptorService;

    @Value("${flatshares.public.media}")
    private String publicMedia;

    public FileStoreService(FileDescriptorService fileDescriptorService) {
        this.fileDescriptorService = fileDescriptorService;
    }

    /**
     * Store a MultipartFile on fileSystem and create a descriptor file about it.
     *
     * @param multipartFile
     * @return The created descriptor file
     */
    public FileDescriptor store(MultipartFile multipartFile) {
        try {
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
            fileDescriptor.setFilePath(publicMedia + uuid + "/" + multipartFile.getOriginalFilename());
            fileDescriptor.setMimeType(tika.detect(multipartFile.getOriginalFilename()));
            fileDescriptor.setFileSize(Long.valueOf(bytes.length));
            //fileDescriptor.setDescription("");

            User loggedInUser = SecurityUtil.getLoggedInUser();

            fileDescriptor.setCreatedBy(loggedInUser == null ? 1 : loggedInUser.getId());
            fileDescriptor.setModifiedBy(loggedInUser == null ? 1 : loggedInUser.getId());

            Timestamp now = DateAndTimeUtil.now();
            fileDescriptor.setCreatedOn(now);
            fileDescriptor.setModifiedOn(now);

            fileDescriptorService.save(fileDescriptor);

            return fileDescriptor;
        } catch (Exception e) {
            throw new FileOperationException("An error has occurred during the file creation!", e);
        }
    }

    //TODO
    public java.io.File get(FileDescriptor fileDescriptor) {
        try {
            return new java.io.File(publicMedia, fileDescriptor.getFilePath());
        } catch (Exception e) {
            throw new FileOperationException("An error has occurred during getting the file!", e);
        }
    }
}
