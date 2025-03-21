package fr.sirine.cuisine.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${app.image.folder_path.origine}")
    private String IMAGE_DIRECTORY_ORIGIN;

    @Value("${app.image.folder_path.thumb}")
    private String IMAGE_DIRECTORY_THUMB;

    @GetMapping(value = "/origin", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getOriginFile(@RequestParam String fileName) {
        try {
            File file = ResourceUtils.getFile(IMAGE_DIRECTORY_ORIGIN+"/" + fileName);
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            return ("Erreur lors de la lecture du fichier : " + e.getMessage()).getBytes();
        }
    }

    @GetMapping(value = "/thumb", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getThumbFile(@RequestParam String fileName) {
        try {
            File file = ResourceUtils.getFile(IMAGE_DIRECTORY_THUMB+"/" + fileName);
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            return ("Erreur lors de la lecture du fichier : " + e.getMessage()).getBytes();
        }
    }
}

