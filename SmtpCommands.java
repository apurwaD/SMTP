import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the SMPT commands
 * 
 * @author Apurva Dandekar
 * @author Krish Godiawala
 */
public class SmtpCommands {

	/**
	 * 
	 * All the commands of SMTP
	 *
	 */
	public String HELO;
	public String MAIL_FROM;
	public List<String> RCPT_TO;
	public String DATA;
	public String QUIT;

	public SmtpCommands() {
		this.RCPT_TO = new ArrayList<String>();
	}

	public void addReceivers(List<String> receivers) {
		for (int i = 0; i < receivers.size(); i++) {
			this.RCPT_TO.add("RCPT TO:<" + receivers.get(i) + ">");
		}
	}
}