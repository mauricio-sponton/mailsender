package mailsender;

public class Executar {

	public static void main(String[] args) {
		Email email = new Email("mauricio.sponton.testes@gmail.com, mauricio.sponton.testes@outlook.com", "DEV",
				"Teste de envio de email", "Envio de email via outlook, carregando userName e password de arquivo...");
		email.enviarEmail();
	}
}
