package com.apijustgifit.service;

import com.apijustgifit.domain.FileStorageProperties;
import com.apijustgifit.domain.StorageProperties;
import com.apijustgifit.validation.FileNotFoundException;
import com.apijustgifit.validation.FileStorageException;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Pattern;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;
    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    public FileStorageService(StorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
        .toAbsolutePath().normalize();
    }

    public String storeFile(MultipartFile file) throws IOException {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (!isValid(fileName)){
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;

        }catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName+ ". Please try again!", ex);
        }

    }

    private Boolean isValid(String filename) {
        String invalidCaracters = "[^..a-zA-Z0-9.æøåÆØÅ_ -..]";
        Pattern pattern = Pattern.compile(invalidCaracters);
        if (pattern.matcher(filename).find() || filename.contains("..")){
            return false;
        }
        return true;

    }

    public Resource loadFile(String fileName){

        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()){
                return resource;
            }else {
                throw new FileNotFoundException("File not found "+ fileName);
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("File not found "+ fileName, e);
        }

    }
}
