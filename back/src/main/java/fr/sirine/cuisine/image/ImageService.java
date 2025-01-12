package fr.sirine.cuisine.image;

import fr.sirine.cuisine.exception.ImageProcessingException;
import fr.sirine.cuisine.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class ImageService {
    private static final Logger log = LoggerFactory.getLogger(ImageService.class);

    @Value("${app.image.folder_path.origine}")
    private String IMAGE_DIRECTORY_ORIGIN;

    @Value("${app.image.folder_path.thumb}")
    private String IMAGE_DIRECTORY_THUMB;

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }
    public Image saveImage(MultipartFile file) {
        try {
            log.info("Starting image save process for file: {}", file.getOriginalFilename());

            String imageName = file.getOriginalFilename();
            // Generate file names once
            String timestamp = String.valueOf(System.currentTimeMillis());
            String fileName = timestamp + "_origin_" + imageName;
            String fullImagePath = IMAGE_DIRECTORY_ORIGIN + fileName;
            String thumbnailName = timestamp + "_thumb_" + imageName;
            String thumbnailPath = IMAGE_DIRECTORY_THUMB + thumbnailName;

            // Verify file type
            if (!isImageFile(file)) {
                log.error("Invalid image file type for file: {}", imageName);
                throw new ImageProcessingException("Le fichier fourni n'est pas une image valide.");
            }

            // Save original image
            log.debug("Saving original image to path: {}", fullImagePath);
            Files.write(Paths.get(fullImagePath), file.getBytes());

            // Generate and save thumbnail
            log.debug("Generating thumbnail for image: {}", imageName);
            BufferedImage img = ImageIO.read(file.getInputStream());
            BufferedImage thumbnail = createThumbnail(img);
            ImageIO.write(thumbnail, "jpg", new File(thumbnailPath));

            // Create, save and return Image object
            Image image = Image.builder()
                    .name(imageName)
                    .imageName(fileName)
                    .thumbnailName(thumbnailName)
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .build();

            log.info("Saving image metadata to database");
            return imageRepository.save(image);

        } catch (IOException e) {
            log.error("Error processing image: {}", e.getMessage(), e);
            throw new ImageProcessingException("Une erreur est survenue lors de la sauvegarde de l'image.", e);
        }
    }

    @Transactional
    public void deleteImage(Integer imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        // Delete physical files
        deleteImageFiles(image.getImageName(), image.getThumbnailName());

        // Delete image entity
        imageRepository.delete(image);
    }

    protected void deleteImageFiles(String imageName, String thumbnailName) {
        try {
            log.info("Attempting to delete original image: {}", Paths.get(IMAGE_DIRECTORY_ORIGIN, imageName));
            log.info("Attempting to delete thumbnail image: {}", Paths.get(IMAGE_DIRECTORY_THUMB, thumbnailName));

            if (imageName != null) {
                Files.deleteIfExists(Paths.get(IMAGE_DIRECTORY_ORIGIN, imageName));
                log.info("Successfully deleted origin image: {}", imageName);
            }
            if (thumbnailName != null) {
                Files.deleteIfExists(Paths.get(IMAGE_DIRECTORY_THUMB, thumbnailName));
                log.info("Successfully deleted thumbnail image: {}", thumbnailName);
            }
        } catch (IOException e) {
            log.error("Failed to delete image files for: {}, {}", imageName, thumbnailName, e);
            throw new ImageProcessingException("Error deleting image files", e);
        }
    }
    public Image updateImage(Image image) {
        log.info("Updating image with ID: {}", image.getId());
        image.setModifiedAt(LocalDateTime.now());
        return imageRepository.save(image);
    }
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif"));
    }
    private BufferedImage createThumbnail(BufferedImage original) {
        int width = 150;
        int height = (original.getHeight() * width) / original.getWidth();
        BufferedImage thumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        thumbnail.getGraphics().drawImage(original, 0, 0, width, height, null);
        return thumbnail;
    }


    public Image getImagesByRecipeId(Integer recipeId) {
        log.debug("Fetching image for recipe ID: {}", recipeId);
        return imageRepository.findByRecipeId(recipeId);
    }

    public Image findById(Integer id) {
        log.debug("Fetching image by ID: {}", id);
        return imageRepository.findById(id).orElse(null);
    }
}
