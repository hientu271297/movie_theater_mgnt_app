package fa.training.movietheater_mockproject.controller;


import fa.training.movietheater_mockproject.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;

@AllArgsConstructor
@RestController
public class FileController {

    private final FileService fileService;

    // http://localhost:8888/files/user01/RequestLifecycle.png
    @GetMapping("/files/**")
    public ResponseEntity<Resource> getFile(HttpServletRequest httpRequest) throws IOException {
        // /user01/RequestLifecycle.png
        String relativePath = httpRequest.getRequestURL().toString().split("files/")[1];
        Resource resource = fileService.loadFileAsResource(relativePath);
        String mimeType = Files.probeContentType(resource.getFile().toPath());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .body(resource);
    }
}
