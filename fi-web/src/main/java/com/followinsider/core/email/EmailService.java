package com.followinsider.core.email;

import com.followinsider.common.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Map;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;

    @Value("${email.sender}")
    private String sender;

    private static final String VALUE_MARK = "%";

    public void sendFileEmail(String to, String path, String subject,
                              Map<String, String> valueMap) throws MessagingException {

        String body = IOUtils.loadResourceFile(path)
                .orElseThrow(() -> new MessagingException("Missing template: " + path));

        sendEmail(to, subject, body, valueMap);
    }

    private void sendEmail(String to, String subject, String body,
                           Map<String, String> valueMap) throws MessagingException {

        for (Map.Entry<String, String> entry : valueMap.entrySet()) {
            String target = VALUE_MARK + entry.getKey() + VALUE_MARK;
            body = body.replace(target, entry.getValue());
        }
        sendEmail(to, subject, body);
    }

    private void sendEmail(String to, String subject, String body) throws MessagingException {
        Message message = buildMessage(to, subject, body);
        Transport.send(message);
    }

    private Message buildMessage(String to, String subject, String body) throws MessagingException {
        Message message = new MimeMessage(startSession());
        message.setFrom(new InternetAddress(sender));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(buildMessageContent(body));
        return message;
    }

    private Multipart buildMessageContent(String body) throws MessagingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(body, "text/html; charset=utf-8");
        return new MimeMultipart(mimeBodyPart);
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
