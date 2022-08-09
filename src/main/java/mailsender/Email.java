package mailsender;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class Email {

	private static final String LOCAL_ARQUIVO = "src/main/resources/mail.txt";
	private String userName;
	private String senha;
	private String destinatarios = "";
	private String remetente = "";
	private String assunto = "";
	private String mensagem = "";

	public Email(String destinatarios, String remetente, String assunto, String mensagem) {
		this.destinatarios = destinatarios;
		this.remetente = remetente;
		this.assunto = assunto;
		this.mensagem = mensagem;
	}

	public void enviarEmail(boolean envioHtml, boolean envioAnexo) {

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
			
			if(envioAnexo) {
				enviarAnexo(envioHtml, message);
			}else {
				
				if (envioHtml) {
					message.setContent(mensagem, "text/html; charset=utf-8");
				} else {
					message.setText(mensagem);
				}
			}

			Transport.send(message);
			System.out.println("Email enviado com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Falha ao enviar o email");
		}
	}
	
	
	private void enviarAnexo(boolean envioHtml, Message message)
			throws MessagingException, IOException, DocumentException {
		var corpoEmail = new MimeBodyPart();
		
		if (envioHtml) {
			corpoEmail.setContent(mensagem, "text/html; charset=utf-8");
		} else {
			corpoEmail.setText(mensagem);
		}
		
		var anexo = new MimeBodyPart();
		anexo.setDataHandler(new DataHandler(new ByteArrayDataSource(simuladorPDF(), "application/pdf")));
		anexo.setFileName("anexo.pdf");
		
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(corpoEmail);
		multipart.addBodyPart(anexo);
		
		message.setContent(multipart);
	}
	
	//Método que simula um PDF para enviar em anexo no email
	private FileInputStream simuladorPDF() throws IOException, DocumentException {
		
		var document = new Document();
		var file = new File("anexo.pdf");
		file.createNewFile();
		
		PdfWriter.getInstance(document, new FileOutputStream(file));
		
		document.open();
		document.add(new Paragraph("Conteúdo do PDF anexo"));
		document.close();
		
		return new FileInputStream(file);
		
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
