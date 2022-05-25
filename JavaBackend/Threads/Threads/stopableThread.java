package Threads;

public class stopableThread extends Thread{
	
	boolean execute = true;
	
	
	public void stopThread() {
		execute = false;
	}

}
