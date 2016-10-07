import java.net.*;
import java.io.*;
public class Prince {

	public static void main(String[] args) {

		try {
			URL myurl = new URL("https://www.cse.iitb.ac.in/~sharat/current/cs251/Assign/Lab09/support/Prince.php");
			URLConnection mConnect = myurl.openConnection();
			if(mConnect instanceof HttpURLConnection) {
				BufferedReader content = new BufferedReader(new InputStreamReader(mConnect.getInputStream()));
				String str = "", tmp;
				while((tmp = content.readLine()) != null) {
					str+=tmp;
				}
				PrintWriter fileSave = new PrintWriter("secret.txt","UTF-8");
				fileSave.write(str+"\n");
				fileSave.close();
			}
			else {
				System.out.println("Enter HTTP URL.");
			}			
		}
		catch(MalformedURLException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}