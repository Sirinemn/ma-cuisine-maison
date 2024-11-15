package fr.sirine.cuisine.image;

import fr.sirine.cuisine.exception.ImageProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ImageService {

    @Value("${app.image.folder_path.origine}")
    private String IMAGE_DIRECTORY_ORIGIN;

    @Value("${app.image.folder_path.thumb}")
    private String IMAGE_DIRECTORY_THUMB;

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }
    public Image saveImage(MultipartFile file) {
        try{
            String imageName = file.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_origin_" + imageName;
            String fullImagePath = IMAGE_DIRECTORY_ORIGIN + fileName;
            String thumbnailPath = IMAGE_DIRECTORY_THUMB + System.currentTimeMillis() + "_thumb_" + imageName;

            // Sauvegarde l'image originale
            Files.write(Paths.get(fullImagePath), file.getBytes());

            // Génère et sauvegarde la miniature
            BufferedImage img = ImageIO.read(file.getInputStream());
            BufferedImage thumbnail = createThumbnail(img);
            ImageIO.write(thumbnail, "jpg", new File(thumbnailPath));

            // Crée, sauvegarde et retourne l'objet Image
            Image image = Image.builder()
                    .name(imageName)
                    .imageLocation(fullImagePath)
                    .thumbnailLocation(thumbnailPath)
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .build();
            this.imageRepository.save(image);
            return image;
        } catch (IOException e){
            throw new ImageProcessingException("Une erreur est survenue lors de la sauvegarde de l'image.", e);
        }

    }

    private BufferedImage createThumbnail(BufferedImage original) {
        int width = 150;
        int height = (original.getHeight() * width) / original.getWidth();
        BufferedImage thumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        thumbnail.getGraphics().drawImage(original, 0, 0, width, height, null);
        return thumbnail;
    }

    public List<Image> getImagesByRecipeId(Integer recipeId) {
        return imageRepository.findByRecipeId(recipeId);
    }
    public Image findById(Integer id){
        return this.imageRepository.findById(id).orElse(null);
    }
}
