package fer.progi.backend.service.impl;

import fer.progi.backend.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceJpa implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendMail(String email, byte[] attachment, String filename) {
        try {
           
        	
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

           
            helper.setFrom("eduxhubprogi@outlook.com");
            helper.setTo("dr53873@fer.hr"); 
            helper.setSubject("Potvrda o statusu učenika");
            helper.setText("Poštovani, u privitku se nalazi potvrda o vašem statusu.", false);

          
            helper.addAttachment(filename, new ByteArrayDataSource(attachment, "application/pdf"));

           
            mailSender.send(message);

        } catch (MessagingException e) {
            System.err.println("Greška pri slanju e-maila: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void RasporedMail(String email) {
        try {
           
        	
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

           
            helper.setFrom("eduxhubprogi@outlook.com");
            helper.setTo(email); 
            helper.setSubject("Raspored");
            helper.setText("Poštovani,\n\n" +
                    "Obavještavamo vas da je u aplikaciji dostupan raspored za nadolazeću školsku godinu. " +
                    "Molimo vas da se prijavite kako biste mogli pregledati detalje i planirati svoje aktivnosti na vrijeme.\n\n", false);
            
           
            mailSender.send(message);

        } catch (MessagingException e) {
            System.err.println("Greška pri slanju e-maila: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

