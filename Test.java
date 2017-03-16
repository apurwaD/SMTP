import java.util.Scanner;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		String g = "From: <Krish@fcn.xom>\r\n";
		g = "To: <Krish@fcn.xom>\r\n";
		g += "Subject: asas \r\n";
		g += "Ok hopefully it starts here";
		g += "\r\n.\r\n";
		get(g);
	}

	public static void get(String read) {
		Scanner scn = new Scanner(read);
		String str = "";
		while (!(str = scn.nextLine()).equals(".")) {
			if (str.contains("Subject"))
				System.out.println(str.substring(str.indexOf(":") + 2,
						str.length()));
			if (!str.contains("From: <") && !str.contains("To: <")
					&& !str.contains("Subject: "))
				System.out.println((str + "\r\n"));
			System.out.println(str);
		}
	}
}
