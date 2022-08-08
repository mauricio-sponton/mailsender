package mailsender;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

	private static final String LOCAL_ARQUIVO = "src/main/resources/mail.txt";
	private String userName;
	private String senha;
	private String destinatarios = "";
	private String remetente = "";
	private String assunto = "";
	private String mensagem = "";
	
	public Email(String destinatarios, String remetente, String assunto, String mensagem) {
		super();
		this.destinatarios = destinatarios;
		this.remetente = remetente;
		this.assunto = assunto;
		this.mensagem = mensagem;
	}

	public void enviarEmail() {

		try {

			lerArquivo();

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

			Address[] toUsers = InternetAddress.parse(destinatarios);
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(userName, remetente));
			message.setRecipients(Message.RecipientType.TO, toUsers);
			message.setSubject(assunto);
			message.setText(mensagem);

			Transport.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void lerArquivo() throws FileNotFoundException {
		FileInputStream entradaArquivo = new FileInputStream(LOCAL_ARQUIVO);
		Scanner lerArquivo = new Scanner(entradaArquivo, "UTF-8");

		while (lerArquivo.hasNext()) {
			String linha = lerArquivo.nextLine();

			if (linha != null && !linha.isEmpty()) {

				String[] dados = linha.split(";");

				this.userName = dados[0];
				this.senha = dados[1];

			}
		}

		lerArquivo.close();
	}
}
