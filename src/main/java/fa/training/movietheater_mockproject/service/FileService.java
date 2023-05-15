package fa.training.movietheater_mockproject.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public interface FileService {

    /**
     * 1. create folder if not exist based on userFolder
     * 2. save file param to folder
     */


    String saveFile(MultipartFile file, String userFolder) throws IOException;

    Resource loadFileAsResource(String relativePath) throws MalformedURLException, FileNotFoundException;
}
