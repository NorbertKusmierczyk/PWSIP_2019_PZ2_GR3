package desktopApp.MailSender;

import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class SendEmail {

    String from = "type email here";

    String user = "type user password here";

    String password = "type password here";

    String host = "smtp.gmail.com";


    Properties properties = System.getProperties();

    public void sendEmail(String sendTo, String subject, String mailText){

        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.required", "true");

        Session session = Session.getDefaultInstance(properties);

        try {

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));

            message.setSubject(subject);

            message.setText(mailText);

            Transport transport = session.getTransport("smtp");
            transport.connect(user, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        }catch (Exception e){

        }

    }
}
