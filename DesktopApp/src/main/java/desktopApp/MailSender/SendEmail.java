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

    String to = "norbert.kusmierczyk@gmail.com";

    String from = "prewarecompany@gamil.com";

    String user = "prewarecompany@gmail.com";

    String password = "Qwerty12345zz";

    String host = "smtp.gmail.com";


    Properties properties = System.getProperties();

    public void sendEmail(){

        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.required", "true");

        Session session = Session.getDefaultInstance(properties);
        int a = 420495;

        try {

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("PreWare.cba.pl - sklep komputerowy");

            message.setText("Twoje zamówienie nr "+a+" zostało wysłane z magazynu");

            Transport transport = session.getTransport("smtp");
            transport.connect(user, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        }catch (Exception e){

        }

    }
}
