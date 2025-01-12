package fr.sirine.cuisine.image;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
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

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(imageService, "IMAGE_DIRECTORY_ORIGIN", "path/to/origin");
        ReflectionTestUtils.setField(imageService, "IMAGE_DIRECTORY_THUMB", "path/to/thumb");
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
        image.setImageName("testImage.jpg");
        image.setThumbnailName("testThumbnail.jpg");

        when(imageRepository.findById(1)).thenReturn(Optional.of(image));

        imageService.deleteImage(1);

        verify(imageService, times(1)).deleteImageFiles("testImage.jpg", "testThumbnail.jpg");
        verify(imageRepository, times(1)).delete(image);
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
