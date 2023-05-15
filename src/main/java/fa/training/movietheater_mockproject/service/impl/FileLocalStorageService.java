package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileLocalStorageService implements FileService {
    @Value("${app.file.location}")
    String fileLocal;
    @Override
    public String saveFile(MultipartFile file, String imageFolder) throws IOException {
        // 1. create folder
        Objects.requireNonNull(file.getOriginalFilename());

        Path imageFolderPath = Paths.get(fileLocal).resolve(imageFolder);
        Files.createDirectories(imageFolderPath);


        // 2. save file param to folder
        file.transferTo(imageFolderPath.resolve(file.getOriginalFilename()));


        // 3. Build relative path url
        Path relativePath = Paths.get(imageFolder).resolve(file.getOriginalFilename());
        return relativePath.toString();
    }

    @Override
    public Resource loadFileAsResource(String relativePath) throws MalformedURLException, FileNotFoundException {
        Path absolutePath = Paths.get(fileLocal).resolve(relativePath);
        Resource resource = new UrlResource(absolutePath.toUri());
        if (resource.exists()) {
            return resource;
        }
        throw new FileNotFoundException("Can not find file with url: " + relativePath);
    }
}
