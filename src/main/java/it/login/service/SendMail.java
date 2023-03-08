package it.login.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class SendMail {
    public void sendMail() {

        String to = "Papercut@papercut.com";

        String from = "Papercut@papercut.com";
        // Mention the SMTP server address.
        String host = "smtp.papercut.com";

        Properties props = new Properties();

        props.put("mail.smtp.host", "127.0.0.1");
        props.put("mail.smtp.port", "25");
        props.put("mail.debug", "true");
        Session session = Session.getDefaultInstance(props);

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Accesso effettuato");
            message.setText("Accesso effettuato");
            System.out.println("Invio in corso...");

            Transport.send(message);

            System.out.println("Messaggio inviato con successo");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}