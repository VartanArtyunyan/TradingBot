import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		boolean run = true;

		while (run) {
			

				Scanner scanner = new Scanner(System.in);
				String input = "";
				serverThread st = new serverThread();
				st.start();

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
			

		}
	}

}
