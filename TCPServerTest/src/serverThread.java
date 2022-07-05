import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class serverThread extends Thread {

	boolean exe;
	BufferedReader br;
	String msg = "{\"signal\":{\"id\":40086,\"instrument\":\"USD_MXN\",\"lastTime\":\"2022-06-14T12:00\",\"buyingPrice\":997.76,\"lastPrice\":20.508,\"takeProfit\":20.51958,\"stopLoss\":20.504,\"macd\":0.03776,\"macdTriggered\":0.01,\"parabolicSAR14\":20.622,\"ema200\":20.255,\"sma\":0,\"atr\":0,\"rsi\":0}}";

	public serverThread() {

		
		exe = true;
		
		

	}

	public void run() {

		System.out.println("warte auf Client");
		int port = 12001;
		try (ServerSocket ss = new ServerSocket(port);
				Socket connection = ss.accept();
				InputStream is = connection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));

				OutputStream os = connection.getOutputStream();
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

		) {
			System.out.println("Ein Client hat sich verbunden");

			while (exe) {
				try {
					bw.write(msg);
					bw.newLine();
					bw.flush();
					this.sleep(10000);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void stopThread() {
		exe = false;
	}

}
