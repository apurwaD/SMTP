/*
 * Domains.java
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Apurwa Dandekar
 * @author Krish Godiawala
 */
public class Domains {
	public static final String FCN_DOMAIN = "fcn.com";
	public static final String EXAMPLE_DOMAIN = "example.com";
	public static final String GMAIL_DOMAIN = "gmail.com";
	// public static final String GMAIL_RESTRICTED = "aspmx.l.google.com";
	public static final int SMTP_PORT = 25;
	public static final int GMAIL_IMAP_PORT = 465;
	public static final int PERSON_SMTP_PORT = 9999;

	public static String[] getServerPortIP(String sender, String receiver) {
		File file = new File("Util.txt");
		Scanner kbd = null;
		sender = sender.substring(sender.indexOf("@") + 1, sender.length());
		receiver = receiver.substring(receiver.indexOf("@") + 1,
				receiver.length());
		System.out.println(sender + " " + receiver);
		try {
			kbd = new Scanner(file);
			while (kbd.hasNext()) {
				String op[] = checkPortIP(kbd.nextLine(), sender, receiver);
				if (op != null)
					return op;
			}
			kbd.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}

	private static String[] checkPortIP(String input, String sender,
			String receiver) {
		Scanner kbd = new Scanner(input);
		String op[] = new String[2];
		if (sender.contains(kbd.next()) && receiver.contains(kbd.next())) {
			op[0] = kbd.next();
			op[1] = kbd.next();
			return op;
		}
		return null;
	}

	public static void main(String args[]) {
		Domains dm = new Domains();
		getServerPortIP("krisgf@fcn.com", "kkg@example.com");
	}
}
