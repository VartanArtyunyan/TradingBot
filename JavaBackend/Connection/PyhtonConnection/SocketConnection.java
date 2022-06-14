package PyhtonConnection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import Threads.StopableThread;

public class SocketConnection extends StopableThread implements Closeable{
	
	
	int port;
	ServerSocket ss;
	Socket connection;
	BufferedReader br;
	BufferedWriter bw;
	
	
	
	
	
	public SocketConnection(int port) {
		
		
		this.port = port;
	
		try {
			ss = new ServerSocket(port);
			System.out.println("warte auf Client");
			connection = ss.accept();
			System.out.println("Ein Client hat sich verbunden");
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	
	
	
	@Override
	public void close() throws IOException {
		br.close();
		bw.close();
	}

}
