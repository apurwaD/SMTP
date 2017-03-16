/*
 * IncomingSmtpConnection.java
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the smtp receiver which receives emails.
 * 
 * @author Apurwa Dandekar
 * @author Krish Godiawala
 *
 */
public class IncomingSmtpConnection extends Thread {
	Socket sslSocket;
	String serverName;
	InputStream inputstream;
	InputStreamReader inputstreamreader;
	BufferedReader bufferedreader;
	OutputStream outputstream;
	Email email;
	PrintWriter pw;
	private Map<String, HashMap<Integer, Email>> listOfEmail;

	public IncomingSmtpConnection(Socket socket, String serverName,
			Map<String, HashMap<Integer, Email>> listOfEmail) {
		try {
			this.sslSocket = socket;
			this.serverName = serverName;
			this.inputstream = sslSocket.getInputStream();
			this.inputstreamreader = new InputStreamReader(inputstream);
			this.bufferedreader = new BufferedReader(inputstreamreader);
			this.outputstream = sslSocket.getOutputStream();
			this.pw = new PrintWriter(outputstream, true);
			this.email = new Email();
			this.listOfEmail = listOfEmail;
			start();
		} catch (IOException e) {
			// System.out.println("An IOException occured");
		}
	}

	/**
	 * This method is the method communicating with the SMTP sender server
	 */
	public void smtpReceivingEmail() {
		this.pw.println("220 " + this.serverName
				+ " Simple Mail Transfer Protocol Ready");

		try {
			while (true) {
				String str = this.bufferedreader.readLine();
				if (str.contains("HELO"))
					this.pw.println("250 " + this.serverName);
				if (str.contains("MAIL FROM")) {
					this.email.setSenderAddress(str.substring(
							str.indexOf("<") + 1, str.length() - 1));
					this.pw.println("250 OK");
				}
				if (str.contains("AUTH PLAIN"))
					continue;
				if (str.contains("RCPT TO")) {
					this.email.setReceiverAddress(str.substring(
							str.indexOf("<") + 1, str.length() - 1));
					this.pw.println("250 OK");
				}
				if (str.contains("DATA")) {

					this.pw.println("354 Start mail input; end with <CRLF>.<CRLF>");
					while (!(str = this.bufferedreader.readLine()).equals(".")) {
						if (str.contains("Subject")) {
							this.email.setSubject(str.substring(
									str.indexOf(":") + 2, str.length()));
							System.out.println("From: "
									+ this.email.getReceiverAddress().get(0));
							System.out.println("Subject: "
									+ this.email.getSubject());
						}
						if (!str.contains("From:<") && !str.contains("To:<")
								&& !str.contains("Subject: ")) {
							this.email.setData(str);

						}
					}
					System.out.println("BODY: " + this.email.getData());
					// System.out.println("Bodywewewewe :"+this.email.getData());
					this.storeEmails(email);

					this.pw.println("250 OK");
				}

				if (str.contains("QUIT")) {
					this.pw.println(this.serverName
							+ " Service closing transmission channel");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				this.sslSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	/**
	 * Method to update the emailList for user
	 * 
	 * @param email
	 */
	private void storeEmails(Email email) {
		ArrayList<String> receiverList = (ArrayList<String>) email
				.getReceiverAddress();
		for (int i = 0; i < receiverList.size(); i++) {
			if (listOfEmail.containsKey(receiverList.get(i))) {
				HashMap<Integer, Email> temp = listOfEmail.get(receiverList
						.get(i));
				int noOfEmail = temp.size();
				temp.put(noOfEmail + 1, email);
			} else {
				Map<Integer, Email> temp = new HashMap<Integer, Email>();
				temp.put(1, email);
				listOfEmail.put(receiverList.get(i),
						(HashMap<Integer, Email>) temp);
			}

		}

	}

	/**
	 * The run method for instantiating multiple threads
	 */
	public void run() {
		smtpReceivingEmail();
	}
}
