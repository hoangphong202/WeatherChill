package com.example.demo.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class AlbumStorageService {
    private final String root = "D:/eclipjava/WeatherChill2/src/main/resources/static/Album";

    public void save(MultipartFile file) {
        Path rootPath = Paths.get(root);
        try {
            if (!Files.exists(rootPath)){
                Files.createDirectories(rootPath);
            }
            Files.copy(file.getInputStream(), rootPath.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println("Loi upload hinh album vao static: " +e.getLocalizedMessage());
        }
    }
}
