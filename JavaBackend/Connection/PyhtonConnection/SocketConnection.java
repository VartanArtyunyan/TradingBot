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

public class SocketConnection extends StopableThread {

	int port;
	ServerSocket ss;
	Socket connection;
	BufferedReader br;
	BufferedWriter bw;

	String preConnectionText;
	String postConnectionText;

	public SocketConnection(int port, String preConnectionText, String postConnectionText) {

		this.port = port;
		this.preConnectionText = preConnectionText;
		this.postConnectionText = postConnectionText;

	}

	@Override
	public void onStart() {
		try {
			ss = new ServerSocket(port);
			System.out.println(preConnectionText);
			connection = ss.accept();
			System.out.println(postConnectionText);
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

		} catch (IOException e) {
			if(this.isRunning())e.printStackTrace();
		}
	}

	@Override
	public void onClose() {

		try {
			if(ss!=null)ss.close();
			
			if(connection!=null)connection.close();
			
			if(br!=null)br.close();
			
		} catch (IOException e) {
		}
	}

}
