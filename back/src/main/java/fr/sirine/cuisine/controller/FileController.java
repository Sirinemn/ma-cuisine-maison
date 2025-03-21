package fr.sirine.cuisine.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${app.image.folder_path.origine}")
    private String IMAGE_DIRECTORY_ORIGIN;

    @Value("${app.image.folder_path.thumb}")
    private String IMAGE_DIRECTORY_THUMB;

    @GetMapping("/origin")
    public ResponseEntity<byte[]> getOriginFile(@RequestParam String fileName) {
        try {
            Path path = Paths.get(IMAGE_DIRECTORY_ORIGIN, fileName);
            byte[] data = Files.readAllBytes(path);
            return ResponseEntity.ok(data);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erreur lors de la lecture du fichier : " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/thumb")
    public ResponseEntity<byte[]> getThumbFile(@RequestParam String fileName) {
        try {
            Path path = Paths.get(IMAGE_DIRECTORY_THUMB, fileName);
            byte[] data = Files.readAllBytes(path);
            return ResponseEntity.ok(data);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erreur lors de la lecture du fichier : " + e.getMessage()).getBytes());
        }
    }
}

