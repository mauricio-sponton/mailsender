package mailsender;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Test;

public class AppTest {
	
	//Adicionar email remetente
	private String userName;
	
	//Adicionar senha
	private String senha;

	@Test
	public void testeEmail() {
		
		try {
			
			var properties = new Properties();
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.host", "smtp-mail.outlook.com");
			properties.put("mail.smtp.port", "587");
			properties.put("mail.smtp.socketFactory.port", "587");
			properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			
			Session session = Session.getInstance(properties, new Authenticator() {
				
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, senha);
				}
			});
			
			Address[] toUsers = InternetAddress.parse("mauricio.sponton.testes@gmail.com, mauricio.sponton.testes@outlook.com");
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(userName, "JDev Treinamento"));
			message.setRecipients(Message.RecipientType.TO, toUsers);
			message.setSubject("Email enviado com Java");
			message.setText("Testando envio de email");
			
			Transport.send(message);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
