package com.followinsider.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final static String SENDER = "hello@kirkandscott.world";

    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;

    public void sendFileEmail(String to, String path, String subject,
                              Map<String, String> toReplace) throws MessagingException {

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("email/" + path)) {
            if (in == null) {
                throw new IOException();
            }
            String body = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            sendPlainEmail(to, subject, body, toReplace);
        } catch (IOException e) {
            throw new MessagingException("File not found: " + path);
        }
    }

    private void sendPlainEmail(String to, String subject, String body,
                                Map<String, String> toReplace) throws MessagingException {

        // Replace all placeholders
        for (Map.Entry<String, String> entry : toReplace.entrySet()) {
            body = body.replace("%" + entry.getKey() + "%", entry.getValue());
        }
        // Create an email message
        Message message = new MimeMessage(startSession());
        message.setFrom(new InternetAddress(SENDER));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);

        // Set the email content
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(body, "text/html; charset=utf-8");
        Multipart content = new MimeMultipart();
        content.addBodyPart(mimeBodyPart);
        message.setContent(content);

        // Send the email message
        Transport.send(message);
    }

    private Session startSession() {
        return Session.getInstance(getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private Properties getProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        return prop;
    }

}
