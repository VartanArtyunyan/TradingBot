import java.io.BufferedReader;
import java.io.IOException;

public class serverThread extends Thread{
	
	boolean exe;
	BufferedReader br;
	public serverThread(BufferedReader br)  {
		
		this.br = br;
		boolean exe = true;

	}
	
	public void run() {
		
		
		while (exe) {
			try {
				String s = br.readLine();
				System.out.println();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	
	public void stopThread() {
		exe = false;
	}

}
