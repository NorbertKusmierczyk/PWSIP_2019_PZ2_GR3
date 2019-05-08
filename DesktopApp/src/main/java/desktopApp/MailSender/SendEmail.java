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

    String to = "groundgk@gmail.com";

    String from = "prewarecompany@gamil.com";

    String user = "prewarecompany@gmail.com";

    String password = "Qwerty12345zz";

    String host = "smtp.gmail.com";

    String message = "twoja stara";

    Properties properties = System.getProperties();

    public void sendEmail(){

        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.required", "true");

        Session session = Session.getDefaultInstance(properties);

        try {

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("test line");

            message.setText("body test message");

            Transport transport = session.getTransport("smtp");
            transport.connect(user, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        }catch (Exception e){

        }

    }
}
