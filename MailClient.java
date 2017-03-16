import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MailClient {
	JFrame loginFrame = new JFrame("Login");
	static JLabel captionLabel = new JLabel("Login to the server : ");
	JLabel usernameLabel = new JLabel("Username : ");
	JTextField usernameField = new JTextField();
	JLabel password = new JLabel("Password : ");
	JPasswordField passwordField = new JPasswordField();
	JButton login = new JButton("Login");

	JFrame homeFrame = new JFrame("Home");
	static JLabel captionHomeLabel = new JLabel("Welcome  Home  ");
	JButton composeButton = new JButton("Compose");
	JButton inboxButton = new JButton("Inbox");
	static Map<Integer, Email> userInbox = new HashMap<Integer, Email>();
	static String ServerIP = new String("");
	static JTextArea textArea = new JTextArea("");
	JScrollPane scroll;

	JFrame frame = new JFrame("SMTP CLient");
	JLabel compose = new JLabel("Compose Mail : ");
	JLabel sendTOLabe = new JLabel("Send To : ");

	JTextField sendTo = new JTextField();
	JLabel subLabel = new JLabel("Subject : ");
	JTextField subject = new JTextField();
	JLabel bodyLabel = new JLabel("Message : ");
	JTextArea body = new JTextArea("");
	JButton sendButton = new JButton("Send");
	static ClientCommunication comm = new ClientCommunication();
	String clientEmailAddrees;
	static MailClient mailWindow = new MailClient();
	static int loginCount = 0;

	public static void main(String args[]) {
		ServerIP = args[0];
		mailWindow.createLoginPage();
	}

	void createLoginPage() {
		JPanel mainJPanel = new JPanel();
		
		
		JLabel welcomeLabel = new JLabel("Welcome to "+ ServerIP);
		welcomeLabel.setForeground(Color.BLUE);
		welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel welcomePanel = new JPanel();
		welcomePanel.setPreferredSize(new Dimension(700, 200));
		welcomePanel.add(welcomeLabel);
		welcomePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomePanel.setMaximumSize(new Dimension(700, 200));
		// welcomePanel.setLayout(new GridLayout());
		welcomePanel.setBackground(new java.awt.Color(235, 204, 255));

		JPanel captionPanel = new JPanel();
		captionPanel.setPreferredSize(new Dimension(200, 40));
		captionPanel.add(captionLabel);
		captionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		captionPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		captionPanel.setMaximumSize(new Dimension(300, 50));
		captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.X_AXIS));
		captionPanel.setBackground(new java.awt.Color(255, 204, 255));

		JPanel usernamePanel = new JPanel();
		usernamePanel.setPreferredSize(new Dimension(200, 30));
		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameField);
		usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		usernamePanel.setBorder(BorderFactory.createLoweredBevelBorder());
		usernamePanel.setMaximumSize(new Dimension(300, 30));
		usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
		usernamePanel.setBackground(new java.awt.Color(255, 204, 255));

		JPanel passwordJPanel = new JPanel();
		passwordJPanel.setPreferredSize(new Dimension(200, 30));
		passwordJPanel.add(password);
		passwordJPanel.add(passwordField);
		passwordJPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordJPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		passwordJPanel.setMaximumSize(new Dimension(300, 30));
		passwordJPanel
				.setLayout(new BoxLayout(passwordJPanel, BoxLayout.X_AXIS));
		passwordJPanel.setBackground(new java.awt.Color(255, 204, 255));

		JPanel loginPanel = new JPanel();
		loginPanel.add(login);
		loginPanel.setPreferredSize(new Dimension(300, 100));
		loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginPanel.setMaximumSize(new Dimension(300, 100));
		loginPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		loginPanel.setLayout(new GridBagLayout());
		loginPanel.setBackground(new java.awt.Color(255, 204, 255));
		
		loginFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});

		LoginButton loginAction = new LoginButton();
		login.addActionListener(loginAction);
		login.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainJPanel.add(welcomePanel);
		mainJPanel.add(captionPanel);
		mainJPanel.add(usernamePanel);
		mainJPanel.add(passwordJPanel);
		mainJPanel.add(loginPanel);
		mainJPanel.setPreferredSize(new Dimension(300, 200));
		mainJPanel.setLayout(new GridLayout());
		mainJPanel.setMaximumSize(new Dimension(300, 200));
		mainJPanel.setLayout(new BoxLayout(mainJPanel, BoxLayout.Y_AXIS));
		loginFrame.setSize(700, 700);
		loginFrame.setMaximumSize(new Dimension(300, 200));
		loginFrame.setResizable(false);
		loginFrame.add(mainJPanel);
		loginFrame.setVisible(true);
		mainJPanel.setBackground(new java.awt.Color(235, 204, 255));

	}

 void createHomePage(){
	 	JPanel mainJPanel = new JPanel();
	 	JLabel userLabel = new JLabel("Hello "+clientEmailAddrees);
		userLabel.setForeground(Color.BLUE);
		userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel userPanel = new JPanel();
		userPanel.setPreferredSize(new Dimension(700, 100));
		userPanel.add(userLabel);
		userPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		userPanel.setMaximumSize(new Dimension(700, 100));
		// welcomePanel.setLayout(new GridLayout());
		userPanel.setBackground(new java.awt.Color(235, 204, 255));
		
		JPanel composePanel = new JPanel();
		composePanel.setPreferredSize(new Dimension(440, 30));
		composePanel.add(captionHomeLabel);
		composePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		composePanel.setMaximumSize(new Dimension(1550, 100));
		composePanel.setBorder(BorderFactory.createLoweredBevelBorder());
		composePanel.setLayout(new BoxLayout(composePanel, BoxLayout.X_AXIS));
		composePanel.setBackground(new java.awt.Color(235, 204, 255));

		JPanel sendToPanel = new JPanel();
		sendToPanel.setPreferredSize(new Dimension(440, 30));
		sendToPanel.add(composeButton);
		sendToPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		sendToPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		sendToPanel.setMaximumSize(new Dimension(1550, 30));
		sendToPanel.setLayout(new BoxLayout(sendToPanel, BoxLayout.X_AXIS));
		sendToPanel.setBackground(new java.awt.Color(235, 204, 255));
		composeButton composeAction = new composeButton();
		composeButton.addActionListener(composeAction);
		composeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel subPanel = new JPanel();
		subPanel.add(inboxButton);
		subPanel.add(textArea);
		subPanel.setPreferredSize(new Dimension(700, 700));
		subPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		subPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		subPanel.setMaximumSize(new Dimension(1550, 700));
		subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.X_AXIS));
		subPanel.setBackground(new java.awt.Color(235, 204, 255));
		inboxAButton inboxAction = new inboxAButton();
		inboxButton.addActionListener(inboxAction);
		inboxButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		homeFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				comm.closeConnection();
				System.exit(0);
			}
		});
		mainJPanel.add(userPanel);
		mainJPanel.add(composePanel);
		mainJPanel.add(sendToPanel);
		mainJPanel.add(subPanel);
		mainJPanel.setPreferredSize(new Dimension(350, 30));
		mainJPanel.setLayout(new GridLayout());
		mainJPanel.setMaximumSize(new Dimension(1550, 1000));
		mainJPanel.setLayout(new BoxLayout(mainJPanel, BoxLayout.Y_AXIS));
		homeFrame.setSize(750, 750);
		homeFrame.setMaximumSize(new Dimension(1050, 450));
		homeFrame.setResizable(true);
		homeFrame.add(mainJPanel);
		homeFrame.setVisible(true);
	 
 }
	void composeMailWindow() {
		JPanel mainJPanel = new JPanel();
		JLabel userLabel = new JLabel("Hello "+clientEmailAddrees);
		userLabel.setForeground(Color.BLUE);
		userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel userPanel = new JPanel();
		userPanel.setPreferredSize(new Dimension(700, 100));
		userPanel.add(userLabel);
		userPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		userPanel.setMaximumSize(new Dimension(700, 100));
		// welcomePanel.setLayout(new GridLayout());
		userPanel.setBackground(new java.awt.Color(235, 204, 255));
		
		JPanel composePanel = new JPanel();
		composePanel.setPreferredSize(new Dimension(440, 30));
		composePanel.add(compose);
		compose.setText("Compose Mail:");
		composePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		composePanel.setMaximumSize(new Dimension(1550, 100));
		composePanel.setBorder(BorderFactory.createLoweredBevelBorder());
		composePanel.setLayout(new BoxLayout(composePanel, BoxLayout.X_AXIS));
		composePanel.setBackground(new java.awt.Color(235, 204, 255));

		JPanel sendToPanel = new JPanel();
		sendToPanel.setPreferredSize(new Dimension(440, 30));
		sendToPanel.add(sendTOLabe);
		sendToPanel.add(sendTo);
		sendToPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		sendToPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		sendToPanel.setMaximumSize(new Dimension(1550, 30));
		sendToPanel.setLayout(new BoxLayout(sendToPanel, BoxLayout.X_AXIS));
		sendToPanel.setBackground(new java.awt.Color(235, 204, 255));

		JPanel subPanel = new JPanel();
		subPanel.add(subLabel);
		subPanel.add(subject);
		subPanel.setPreferredSize(new Dimension(440, 30));
		subPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		subPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		subPanel.setMaximumSize(new Dimension(1550, 30));
		subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.X_AXIS));
		subPanel.setBackground(new java.awt.Color(235, 204, 255));

		JPanel bodyPanel = new JPanel();
		bodyPanel.add(bodyLabel);
		bodyPanel.add(body);
		bodyPanel.setPreferredSize(new Dimension(440, 300));
		bodyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		bodyPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		bodyPanel.setMaximumSize(new Dimension(1550, 500));
		bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.X_AXIS));
		bodyPanel.setBackground(new java.awt.Color(235, 204, 255));

		SendButton enter = new SendButton();
		sendButton.addActionListener(enter);
		sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				compose.setText("Compose Mail");
				sendTo.setText("");
				subject.setText("");
				body.setText("");
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(sendButton);
		buttonPanel.setPreferredSize(new Dimension(440, 30));
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.setMaximumSize(new Dimension(1550, 200));
		buttonPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		// buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setLayout(new GridBagLayout());
		buttonPanel.setBackground(new java.awt.Color(25, 204, 255));

		mainJPanel.add(userPanel);
		mainJPanel.add(composePanel);
		mainJPanel.add(sendToPanel);
		mainJPanel.add(subPanel);
		mainJPanel.add(bodyPanel);
		mainJPanel.add(buttonPanel);
		mainJPanel.setPreferredSize(new Dimension(350, 30));
		mainJPanel.setLayout(new GridLayout());
		mainJPanel.setMaximumSize(new Dimension(1550, 1000));
		mainJPanel.setLayout(new BoxLayout(mainJPanel, BoxLayout.Y_AXIS));
		frame.setSize(750, 750);
		frame.setMaximumSize(new Dimension(1050, 450));
		frame.setResizable(true);
		frame.add(mainJPanel);
		frame.setVisible(true);
	}

	// Actionlister class for send button
	private class SendButton implements ActionListener {

		/*
		 * function : actionPerformed
		 * 
		 * @param ActionEvent type of action
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String sendToEmailAddress = sendTo.getText();
			String subjectText = subject.getText();
			List<String> allEmail = checkNoOfEmail(sendToEmailAddress);
			boolean validEmail = true;
			for (int i = 0; i < allEmail.size(); i++) {
				if (!validateEmail(allEmail.get(i))) {
					validEmail = false;
					break;
				}
			}
			if (!validEmail) {
				JOptionPane.showMessageDialog(frame, "Invalid Email Address.",
						"Warning", JOptionPane.ERROR_MESSAGE);
			}
			if (validEmail) {

				if (!subjectText.equals("")) {
					Email packetTobeSent = createPacket(allEmail);
					boolean status = comm.sendEmail(packetTobeSent);
					if (status) {
						compose.setForeground(Color.BLUE);
						compose.setText("The Email has been sent");
						//frame.setVisible(false);
						sendTo.setText("");
						subject.setText("");
						body.setText("");
						
					} else {
						compose.setForeground(Color.RED);
						compose.setText("The Server is Down");
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Subject is empty",
							"Warning", JOptionPane.ERROR_MESSAGE);
				}

			}

		}
	}

	// Actionlistener for Cancel Button
	private class LoginButton implements ActionListener {
		private String amountEntered;

		/*
		 * function : actionPerformed
		 * 
		 * @param ActionEvent type of action
		 */

		public void actionPerformed(ActionEvent e) {
			String userName = usernameField.getText();
			String password = passwordField.getText();

			if (userName.equals("")) {
				JOptionPane.showMessageDialog(frame, "Username is empty",
						"Warning", JOptionPane.ERROR_MESSAGE);
			} else if (password.equals("")) {
				JOptionPane.showMessageDialog(frame, "Password is empty",
						"Warning", JOptionPane.ERROR_MESSAGE);
			} else {

				try {
					String authentication = comm.communicate(userName,
							password, ServerIP);
					if (authentication.equals("AUTHENTICATED")) {
						clientEmailAddrees = usernameField.getText();
						loginFrame.setVisible(false);
						mailWindow.createHomePage();
					} else {
						if (authentication.equals("NOTAUTHENTICATED")) {
							captionLabel.setForeground(Color.RED);
							captionLabel.setText("Wrong username and password");
						} 
						else{
							if(authentication.equals("TOO_MANY")){
								captionLabel.setForeground(Color.RED);
								captionLabel.setText("BUFFER FULL: Too Many Active users of same client");
							}
							else {
								captionLabel.setForeground(Color.RED);
								captionLabel.setText("The Server is down");
							}
						}
						
						
					}

				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

	}

	// Actionlistener for Cancel Button
	private class composeButton implements ActionListener {
		private String amountEntered;

		/*
		 * function : actionPerformed
		 * 
		 * @param ActionEvent type of action
		 */

		public void actionPerformed(ActionEvent e) {
			System.out.println(loginCount);
			if (loginCount == 0) {
				mailWindow.composeMailWindow();
				loginCount++;
			} else {
				frame.setVisible(true);
			}
		}
	}

	// Actionlistener for Cancel Button
	private class inboxAButton implements ActionListener {
		private String amountEntered;

		/*
		 * function : actionPerformed
		 * 
		 * @param ActionEvent type of action
		 */

		public void actionPerformed(ActionEvent e) {
			String inbox = "";
			System.out.println("Click");
			userInbox = comm.fetchMail(clientEmailAddrees);
			System.out.println(userInbox.size());
			if (userInbox.containsKey(1000000)) {
				captionHomeLabel.setForeground(Color.RED);
				captionHomeLabel.setText("Server is Down");
			} else {
				
				if (userInbox.size() != 0) {
					System.out.println("Inbox size:"+userInbox.size());
					int size =userInbox.size();
					for (Entry<Integer, Email> entry : userInbox.entrySet()) {
						
						inbox+= "\n*******************************************\n";
						Email inboxEmail = userInbox.get(size);
						inbox += "Sender: " + inboxEmail.getSenderAddress()
								+ "\n";
						inbox += "Subject: " + inboxEmail.getSubject() + "\n";
						inbox += "Body: " + inboxEmail.getData() + "\n";
						inbox += "*******************************************" + '\n';
						size--;
					}

				} else {
					System.out.println("No Emails");
				}
				captionHomeLabel.setForeground(Color.BLUE);
				captionHomeLabel.setText("Welcome Home");
				textArea.setText(inbox);
				textArea.setForeground(Color.BLACK);
				
			}
		}
	}

	Email createPacket(List<String> listOfEmail) {
		String emailBody = body.getText();
		String emailSubject = subject.getText();
		Email packet = new Email();
		packet.setReceiverAddress(listOfEmail);
		packet.setData(emailBody);
		packet.setSenderAddress(clientEmailAddrees);
		packet.setSubject(emailSubject);
		return packet;

	}

	List<String> checkNoOfEmail(String sendTo) {
		String[] addresses = null;
		if (sendTo.contains(";")) {
			String regex = ";";
			addresses = sendTo.split(regex);
		} else {
			addresses = new String[1];
			addresses[0] = sendTo;
		}
		List<String> addressList = new ArrayList<String>();
		for (int i = 0; i < addresses.length; i++) {
			System.out.println("emails" + addresses[i]);
			if (!addressList.contains(addresses[i]))
				addressList.add(addresses[i].trim());
		}
		return addressList;
	}

	boolean validateEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = null;
		pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher;
		matcher = pattern.matcher(email);
		return matcher.matches();
	}

}
