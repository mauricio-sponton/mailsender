package mailsender;

public class Executar {

	public static void main(String[] args) {
		
		var builder = new StringBuilder();
		builder.append("Olá, <br/>");
		builder.append("Teste de email... <br/> Clique no link abaixo. <br/>");
		builder.append("<a target=\"_blank\" href=\"www.google.com\" style=\"text-decoration:none; color:white; padding: 14px 25px; background:green; text-align:center; border-radius: 10px; display:inline-block\">Acessar o link</a>");
		
		Email email = new Email("mauricio.sponton.testes@gmail.com, mauricio.sponton.testes@outlook.com", "DEV",
				"Teste de envio de email", builder.toString());
		email.enviarEmail(true, true);
	}
}
