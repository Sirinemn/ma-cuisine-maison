package fr.sirine.starter.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    private MimeMessage mimeMessage;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mimeMessage = mock(MimeMessage.class);
    }

    @Test
    public void testSendEmail() throws MessagingException {
        // Mock de MimeMessage et MimeMessageHelper
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        // Mock du template engine
        when(templateEngine.process(eq("confirm-email"), any(Context.class)))
                .thenReturn("<html>Email Content</html>");

        // Appel de la méthode à tester
        emailService.sendEmail(
                "test@example.com",
                "TestUser",
                null,
                "http://example.com/confirm",
                "12345",
                "Test Subject"
        );

        // Vérifier que les méthodes JavaMailSender ont été appelées correctement
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(mimeMessage);

        // Capturer les arguments passés à MimeMessageHelper pour les vérifier
        ArgumentCaptor<MimeMessageHelper> helperCaptor = ArgumentCaptor.forClass(MimeMessageHelper.class);
        verify(javaMailSender).send(mimeMessage);

        // Vérifier que les bons paramètres sont utilisés dans le template
        verify(templateEngine).process(eq("confirm-email"), any(Context.class));
    }

    @Test
    public void testSendEmailWithCustomTemplate() throws MessagingException {
        // Mock de JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Mock du template engine avec le template "activate_account"
        when(templateEngine.process(eq("activate_account"), any(Context.class)))
                .thenReturn("<html>Activation Email Content</html>");

        // Appel de la méthode avec le template personnalisé "activate_account"
        emailService.sendEmail(
                "custom@example.com",
                "CustomUser",
                EmailTemplateName.ACTIVATE_ACCOUNT,  // Utilisation du template ACTIVATE_ACCOUNT
                "http://example.com/confirm",
                "54321",
                "Custom Subject"
        );

        // Vérifiez que templateEngine.process a été appelé avec le bon template
        verify(templateEngine, times(1)).process(eq("activate_account"), any(Context.class));

        // Vérifiez que le mail a bien été envoyé
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

}