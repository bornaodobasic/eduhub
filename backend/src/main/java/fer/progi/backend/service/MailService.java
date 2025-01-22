package fer.progi.backend.service;

public interface MailService {

	public void sendMail(String email, byte[] attachment, String filename);

	void RasporedMail(String email);
}
