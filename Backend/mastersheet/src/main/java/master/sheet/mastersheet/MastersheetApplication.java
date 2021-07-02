package master.sheet.mastersheet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

@SpringBootApplication
public class MastersheetApplication {
	//username,colName,rowName,dateOfUpdate,beforeChange,afterChange
	public static void sendMail() throws MessagingException{
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
			   return new PasswordAuthentication("mrabdullah0102@gmail.com", "**");
			}
		 });
		 Message msg = new MimeMessage(session);
   msg.setFrom(new InternetAddress("mrabdullah0102@gmail.com", false));

   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("mrabdullah0102@gmail.com"));
   msg.setSubject("Tutorials point email");
   msg.setContent("Tutorials point email", "text/html");

   MimeBodyPart messageBodyPart = new MimeBodyPart();
   messageBodyPart.setContent("Tutorials point email", "text/html");
   Transport.send(msg);
	}
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MastersheetApplication.class, args);
	}

}
