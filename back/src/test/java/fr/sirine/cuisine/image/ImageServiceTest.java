package fr.sirine.cuisine.image;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;
    @Spy
    @InjectMocks
    private ImageService imageService;
    private static final String TEST_IMAGE_DIRECTORY = "src/test/resources/test-images/original/";
    private static final String TEST_THUMB_DIRECTORY = "src/test/resources/test-images/thumbnails/";

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(Paths.get(TEST_IMAGE_DIRECTORY));
        Files.createDirectories(Paths.get(TEST_THUMB_DIRECTORY));
        ReflectionTestUtils.setField(imageService, "IMAGE_DIRECTORY_ORIGIN", TEST_IMAGE_DIRECTORY);
        ReflectionTestUtils.setField(imageService, "IMAGE_DIRECTORY_THUMB", TEST_THUMB_DIRECTORY);
    }
    @AfterEach
    void tearDown() throws IOException {
        // Clean up test directories after each test
        FileUtils.deleteDirectory(new File(TEST_IMAGE_DIRECTORY));
        FileUtils.deleteDirectory(new File(TEST_THUMB_DIRECTORY));
    }

    @Test
    void testSaveImage() throws IOException {
        // Charge un fichier image valide à partir des ressources de test
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("test-images/test.jpg");
        assertNotNull(inputStream, "L'image de test est introuvable.");

        // Crée un MockMultipartFile avec le contenu de l'image
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", inputStream);

        Image savedImage = new Image();
        savedImage.setName("test.jpg");

        when(imageRepository.save(any(Image.class))).thenReturn(savedImage);
        Image image = imageService.saveImage(file);

        assertNotNull(image);
        assertEquals("test.jpg", image.getName());
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void deleteImageTest() {
        Image image = new Image();
        image.setId(1);
        image.setImageName("test.jpg");
        image.setThumbnailName("testThumbnail.jpg");

        when(imageRepository.findById(1)).thenReturn(Optional.of(image));

        imageService.deleteImage(1);

        verify(imageService, times(1)).deleteImageFiles("test.jpg", "testThumbnail.jpg");
        verify(imageRepository, times(1)).delete(image);
    }

    @Test
    void updateImageTest() {
        Image image = new Image();
        image.setId(1);

        imageService.updateImage(image);

        verify(imageRepository, times(1)).save(image);
    }

    @Test
    void testFindById() {
        Image image = new Image();
        image.setId(1);
        when(imageRepository.findById(1)).thenReturn(Optional.of(image));
        Image result = imageService.findById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
    }
}
