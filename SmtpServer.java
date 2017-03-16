import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * This class is the Smpt sender and receiver server
 * 
 * @author Apurwa Dandekar
 * @author Krish Godiawala
 */
public class SmtpServer extends Thread {
	private static String serverName;
	static Map<String, String> hosts;
	public static final int CLIENT_LISTEN_PORT = 5555;
	private Socket server;
	private static Map<String, HashMap<Integer, Email>> listOfEmail;
	static Map<String, Integer> activeUsers;
	static {
		listOfEmail = new HashMap<String, HashMap<Integer, Email>>();
		activeUsers = new HashMap<String, Integer>();
	}

	public SmtpServer(String Servername, int number) {
		SmtpServer.serverName = Servername;
		hosts = new HashMap<String, String>();
	}

	public SmtpServer(Socket socket) {
		this.server = socket;

	}

	public SmtpServer() {

	}

	/**
	 * Main Method
	 */
	public static void main(String args[]) {
		SmtpServer smtpServer = new SmtpServer(args[0], -1);
		// System.out.println(args[0]);
		smtpServer.addHosts();

		new Thread(new SmtpServer(), "ClientReceipt").start();

		new Thread(new SmtpServer(), "ServerReceipt").start();

	}

	/**
	 * Smtp Server Receiver listening for connection
	 */
	public void incomingSmtpServerConnection() {
		try {
			ServerSocket sock = new ServerSocket(Domains.PERSON_SMTP_PORT);
			// System.out.println("Waiting to connect");
			while (true) {
				Socket serv = sock.accept();
				// System.out.println("Connected");
				new Incom(serv, serverName, listOfEmail);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * For initiating outgoing connection
	 * 
	 * @param email
	 * @param userID
	 * @param Password
	 */
	public void outgoingSmtpServerConnection(Email email, String userID,
			String Password) {
		try {
			Socket socket;
			if (userID.contains(Domains.FCN_DOMAIN)
					|| userID.contains(Domains.EXAMPLE_DOMAIN)) {
				String op[] = Domains.getServerPortIP(email.getSenderAddress(),
						email.getReceiverAddress().get(0));
				socket = new Socket(op[0], Integer.parseInt(op[1]));
				new Outgoing(socket, serverName, email, userID, Password);
			} else if (userID.contains(Domains.GMAIL_DOMAIN)) {
				SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory
						.getDefault();
				String op[] = Domains.getServerPortIP(email.getSenderAddress(),
						email.getReceiverAddress().get(0));
				SSLSocket sslSocket = (SSLSocket) sslsocketfactory
						.createSocket(op[0], Integer.parseInt(op[1]));

				// imap.gmail.com

				sslSocket.startHandshake();
				socket = sslSocket;
				new Outgoing(socket, serverName, email, userID, Password);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(Email email, String userID, String Password) {
		try {
			Socket client = new Socket("aspmx.l.google.com", 25);
			new Outgoing(client, serverName, email, userID, Password);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method keeps track of the number of Active Users
	 * 
	 * @param userName
	 */
	private void incrementAciveUsers(String userName) {
		if (activeUsers.containsKey(userName)) {
			int num = activeUsers.get(userName);
			activeUsers.put(userName, num + 1);

		} else {
			activeUsers.put(userName, 1);
		}

	}

	private void decrementActiveUsers(String userName) {
		if (activeUsers.containsKey(userName)) {
			int num = activeUsers.get(userName);
			activeUsers.put(userName, num - 1);

		}
	}

	/**
	 * This method returns the number of active users for a particular user
	 * 
	 * @param userName
	 * @return
	 */
	private int getActiveUsers(String userName) {
		if (activeUsers.containsKey(userName))
			return activeUsers.get(userName);
		return 0;
	}

	/**
	 * Please work
	 */
	public void testSendEmail() {
		Email email = new Email();
		email.setSenderAddress("krish@fcn.com");
		email.setReceiverAddress("krish@example.com");
		email.setSubject("Yes I am a genius");
		email.setData("Sent from our code u can thank me");
		this.outgoingSmtpServerConnection(email, "krish@fcn.com", "xxxx");
	}

	/**
	 * This method accepts clients
	 */
	private void acceptClient() {
		try {
			// System.out.println("here");
			ServerSocket serverSocket = new ServerSocket(CLIENT_LISTEN_PORT);
			while (true) {
				this.server = serverSocket.accept();
				// System.out.println("Accepted");
				new Thread(new SmtpServer(server), "email").start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void communicateWithClient() {
		// System.out.println("in coom");
		String usrIdPass[] = new String[2];
		try {
			ObjectInputStream ois = new ObjectInputStream(
					this.server.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(
					this.server.getOutputStream());
			@SuppressWarnings("unchecked")
			HashMap<String, String> userIdPassword = (HashMap<String, String>) ois
					.readObject();
			usrIdPass = authenticateUser(userIdPassword);
			if (usrIdPass != null) {
				this.incrementAciveUsers(usrIdPass[0]);
				if ((this.getActiveUsers(usrIdPass[0]) > 2)) {
					oos.writeObject("TOO_MANY");
					this.decrementActiveUsers(usrIdPass[0]);
					return;
				}
				oos.writeObject("AUTHENTICATED");
			} else {
				oos.writeObject("NOTAUTHENTICATED");
				return;
			}

			while (true) {
				Object clientReq = (Object) ois.readObject();

				// System.out.println("type Of class" + clientReq.getClass());
				// //////////////////////
				String classname = clientReq.getClass().getName();
				if (classname.contains("java.lang.String")) {
					if (clientReq.equals("close"))
						break;
					sendInbox((String) clientReq);
				} else {
					Email email = (Email) clientReq;
					this.outgoingSmtpServerConnection(email, usrIdPass[0],
							usrIdPass[1]);
				}
			}
			this.decrementActiveUsers(usrIdPass[0]);
		} catch (SocketException e) {

			System.out.println("Client has crashed!!!!!!!");
			this.decrementActiveUsers(usrIdPass[0]);
		} catch (IOException | ClassNotFoundException e) {
			this.decrementActiveUsers(usrIdPass[0]);
			e.printStackTrace();
		}
	}

	/**
	 * This Method authenticates the user
	 * 
	 * @param userIdPassword
	 */
	private String[] authenticateUser(HashMap<String, String> userIdPassword) {
		String usr[] = new String[2];
		for (Entry<String, String> entry : userIdPassword.entrySet()) {
			String username = entry.getKey();
			String password = entry.getValue();
			try {
				if (hosts.get(username).equals(password)) {
					usr[0] = username;
					usr[1] = password;
					return usr;
				} else {
					return null;
				}
			} catch (NullPointerException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * Method to add hosts
	 */
	private void addHosts() {
		if (serverName.equals("fcn")) {
			hosts.put("abc@fcn.com", "buddhi");
			hosts.put("user@fcn.com", "user");
			hosts.put("krish@fcn.com", "krish");
			hosts.put("krishgodiawala@gmail.com", "xxxx");
		} else if (serverName.equals("example")) {
			hosts.put("fcn@example.com", "fcn");
			hosts.put("krishgodiawala@gmail.com", "xxxx");
		}

	}

	private void sendInbox(String username) {
		System.out.println("sending");
		try {

			ObjectOutputStream oos = new ObjectOutputStream(
					this.server.getOutputStream());
			if (listOfEmail.containsKey(username)) {
				oos.writeObject(listOfEmail.get(username));
				oos.flush();
			} else {
				// System.out.println("writing object");
				HashMap<Integer, Email> temp = new HashMap<Integer, Email>();
				oos.writeObject(temp);
				oos.flush();
			}
		} catch (Exception e) {
		}

	}

	/**
	 * Create a new thread
	 */
	public void run() {
		if (Thread.currentThread().getName().equals("email"))
			communicateWithClient();
		else if (Thread.currentThread().getName().equals("ClientReceipt"))
			this.acceptClient();
		else if (Thread.currentThread().getName().equals("ServerReceipt"))
			this.incomingSmtpServerConnection();
	}

}
