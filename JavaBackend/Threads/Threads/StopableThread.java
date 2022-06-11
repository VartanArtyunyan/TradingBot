package Threads;

public class StopableThread extends Thread{
	
    boolean execute = true;
    
    public void run() {
    	onStart();
    	while(execute) {
    		onTick();
    	}
    }
    
    public void onStart() {
    	
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
