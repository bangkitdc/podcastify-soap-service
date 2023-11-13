package com.podcastify.implementor;

import java.util.Properties;

import com.podcastify.service.EmailService;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailServiceImpl implements EmailService {
  public void sendEmail(String to, String subject, String body) {
    // Set SMTP server details
    Dotenv dotenv = Dotenv.load();
    String host = dotenv.get("SMTP_HOST");
    String port = dotenv.get("SMTP_PORT");
    String username = dotenv.get("SENDER_EMAIL");
    String password = dotenv.get("SENDER_PASSWORD");

    // Set up JavaMail properties
    Properties properties = new Properties();
    properties.put("mail.smtp.host", host);
    properties.put("mail.smtp.port", port);
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.ssl.trust", host);

    // Create a mail session with authentication
    Session session = Session.getInstance(properties, new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });

    try {
      // Create a MimeMessage object
      MimeMessage message = new MimeMessage(session);

      // Set the sender's email address
      message.setFrom(new InternetAddress(username));

      // Set the recipient's email address
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

      // Set the email subject and body
      message.setSubject(subject);
      // message.setText(body);
      
      // Create the multipart/alternative part
      Multipart multipart = new MimeMultipart("alternative");

      // Add the HTML part
      MimeBodyPart htmlPart = new MimeBodyPart();
      htmlPart.setContent(body, "text/html; charset=utf-8");

      // Add parts to the multipart
      multipart.addBodyPart(htmlPart);

      // Set the content of the message
      message.setContent(multipart);

      // Send the email
      Transport.send(message);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}
