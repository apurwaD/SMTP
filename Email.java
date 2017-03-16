import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Apurva Dandekar
 * @author Krish Godiawala
 */
public class Email implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1963665071658842192L;
	private String senderAddress;
	private List<String> receiverAddress;
	private String data;
	private List<String> attachedFile;
	private String subject;

	public Email() {
		senderAddress = new String();
		receiverAddress = new ArrayList<String>();
		data = new String("");
		attachedFile = new ArrayList<>();
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress.add(receiverAddress);
	}

	public List<String> getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(List<String> receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data += data;
	}

	public List<String> getAttachedFile() {
		return attachedFile;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

}
