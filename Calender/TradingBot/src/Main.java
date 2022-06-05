import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		

		int port = 12000;
		try(ServerSocket ss = new ServerSocket(port);
			Socket connection = ss.accept();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));	

				){
			
			Scanner scanner = new Scanner(System.in);
			String input = "";
			serverThread st = new serverThread(br);
			st.start();
			
			boolean run = true;
			while (run) {

				input = scanner.nextLine();
				input = input.toLowerCase();

				if (input.equals("stop")) {
					run = false;
					st.stopThread();

				} else {
					System.out.println("ungültige eingabe");
				}

			}

			
			
			
			
			
		}catch(IOException e) {
					
				}

	}

}
