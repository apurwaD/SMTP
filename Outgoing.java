import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

/**
 * This class represents the smtp senders which send the email
 * 
 * @author Apurwa Dandekar
 * @author Krish Godiawala
 *
 */
public class Outgoing extends Thread {
	Socket sslSocket;
	String serverName;
	Email email;
	Scanner scanner;
	PrintWriter out;
	SmtpCommands smtpCommands;
	String userName;
	String password;

	public Outgoing(Socket socket, String serverName, Email email,
			String userid, String pass) {
		try {
			this.sslSocket = socket;
			this.serverName = serverName;
			this.email = email;
			this.userName = userid;
			this.password = pass;

			scanner = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);
			smtpCommands = new SmtpCommands();
			loadCommands();

			start();
		} catch (IOException e) {
			System.out.println("An IOException occured");
		}
	}

	/**
	 * This method loads all the commandValues;
	 */
	private void loadCommands() {
		this.smtpCommands.HELO = "HELO";
		// Take care of Sender Address modify in MAILFROM
		this.smtpCommands.MAIL_FROM = "MAIL FROM:<"
				+ this.email.getSenderAddress() + ">";
		// add the receivers
		this.smtpCommands.addReceivers(email.getReceiverAddress());
		this.smtpCommands.DATA = "DATA";
		this.smtpCommands.QUIT = "QUIT";
	}

	/**
	 * This method sends the email composed by the client
	 */
	public void smtpSendingEmail() {
		String message = "\000" + this.userName + "\000" + this.password;
		byte[] auth = message.getBytes();
		String encoded = DatatypeConverter.printBase64Binary(auth);

		System.out.println(scanner.nextLine());

		out.println(this.smtpCommands.HELO);
		System.out.println(scanner.nextLine());

		if (email.getSenderAddress().contains("gmail.com")) {
			out.println("AUTH PLAIN " + encoded);
			System.out.println(scanner.nextLine());
		}

		out.println(this.smtpCommands.MAIL_FROM);
		System.out.println(scanner.nextLine());

		// Need to accomodate multiple receipt to
		out.println(smtpCommands.RCPT_TO.get(0));
		System.out.println(scanner.nextLine());

		out.println("DATA");
		System.out.println(scanner.nextLine());

		String emailMessage = new String();

		emailMessage += "From:<" + email.getSenderAddress() + ">\r\n";
		emailMessage += "To:<" + email.getReceiverAddress() + ">\r\n";
		emailMessage += "Subject: " + this.email.getSubject() + "\r\n";
		emailMessage += email.getData() + "\r\n";

		Scanner scanContent = new Scanner(emailMessage);
		while (scanContent.hasNextLine()) {
			out.println(scanContent.nextLine());

		}
		scanContent.close();

		out.println("\r\n.\r\n");
		System.out.println(scanner.nextLine());
		out.println(this.smtpCommands.QUIT);
		System.out.println(scanner.nextLine());
		scanner.close();
		out.close();
	}

	public void run() {
		smtpSendingEmail();
	}

}
