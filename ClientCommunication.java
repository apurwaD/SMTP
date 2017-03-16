import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ClientCommunication extends Thread {
	final String SERVER_IPADDRESS = "localhost";// needs to be changed
	private String username;
	private String password;
	private Map<String, String> usernamePass;
	private boolean authenticated = false;
	static Socket soc;
	static OutputStream os;
	static ObjectOutputStream oos;
	static InputStream objectStream;
	static ObjectInputStream objectStreamServer;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	ClientCommunication() {
	}

	public void run() {

	}

	/**
	 * Communicate with SMTP server
	 */
	@SuppressWarnings("finally")
	public String communicate(String username, String password, String ServerIP)
			throws UnknownHostException, IOException, ClassNotFoundException {
		String authenticateValue = "";
		try {
			soc = new Socket(ServerIP, 5555);
			os = soc.getOutputStream();
			oos = new ObjectOutputStream(os);
			Map<String, String> usernamePass = new HashMap<String, String>();
			usernamePass.put(username, password);
			oos.writeObject(usernamePass);
			oos.flush();
			objectStream = soc.getInputStream();
			objectStreamServer = new ObjectInputStream(objectStream);
			String serverInObject = (String) objectStreamServer.readObject();
			if (serverInObject.equals("AUTHENTICATED")
					|| serverInObject.equals("NOTAUTHENTICATED")) {
				if (serverInObject.equals("AUTHENTICATED")) {
					authenticateValue = "AUTHENTICATED";
				} else {
					authenticateValue = "NOTAUTHENTICATED";

				}
			}
			else{
				if (serverInObject.equals("TOO_MANY")){
					authenticateValue = "TOO_MANY";
				}
				
			}
		} catch (ConnectException ex) {
			authenticateValue = "DOWN";

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			authenticateValue = "DOWN";
		} finally {
			return authenticateValue;
		}

	}

	/**
	 * Send email packet to SMTP server
	 */
	@SuppressWarnings("finally")
	public boolean sendEmail(Email email) {
		boolean isSent = false;
		try {
			oos.writeObject(email);
			isSent = true;
		} catch (ConnectException ex) {
			isSent = false;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			isSent = false;
		} finally {
			return isSent;
		}

	}

	/**
	 * Fetch Email from the SMTP Server Message Queue
	 */
	public HashMap<Integer, Email> fetchMail(String username) {
		Map<Integer, Email> serverInObject = new HashMap<Integer, Email>();
		Email dummyEmail = new Email();
		HashMap<Integer, Email> dummyHashMap = new HashMap<Integer, Email>();
		try {
			oos.writeObject(username);
			oos.flush();
			objectStream = soc.getInputStream();
			objectStreamServer = new ObjectInputStream(objectStream);
			serverInObject = (HashMap<Integer, Email>) objectStreamServer
					.readObject();
			System.out.println(serverInObject.getClass());
		} catch (IOException | ClassNotFoundException e) {
			dummyHashMap.put(1000000, dummyEmail);
			return dummyHashMap;
		}

		return (HashMap<Integer, Email>) serverInObject;

	}

	public void closeConnection() {
		try {
			oos.writeObject("close");
			oos.flush();
		} catch (IOException e) {

		}

	}
}
