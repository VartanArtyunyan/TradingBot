package Threads;

public class stopableThread extends Thread{
	
    boolean execute = true;
    
    public void run() {
    	while(execute) {
    		onTick();
    	}
    }
	
    public void onTick() {
    	
    }
    
    public boolean getExecute(){
    	return execute;
    }
	public void stopThread() {
		execute = false;
	}
	

}
