package com.apijustgifit.service;

import com.apijustgifit.domain.FileStorageProperties;
import com.apijustgifit.domain.StorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;
    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    public FileStorageService(StorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
        .toAbsolutePath().normalize();
    }

    public String store(MultipartFile file) {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());


        try {
            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return filename;

        }catch (IOException e) {
            e.printStackTrace();
            log.info("------>"+e.getMessage());
            return "";
        }

    }
}
