package Threads;

//<<<<<<< HEAD:JavaBackend/Threads/Threads/stopableThread.java
public class StopableThread extends Thread {

	boolean execute;
	long period;
	long nextPeriodEnd;
	boolean timerIsSet;
	int refreshTime;

	public StopableThread() {
		setTickRate(240);
		execute = true;
		timerIsSet = false;
	}

	public StopableThread(int tickrate) {

		if (tickrate > 240)
			setTickRate(240);
		else if (tickrate < 10)
			setTickRate(10);
		else
			setTickRate(tickrate);

		execute = true;
		timerIsSet = false;
	}

	public void run() {
		onStart();
		while (execute) {
			onTick();
			if (isEndOfPeriod()) {
				onTimer();
				calculateNextPeriodEnd();
			}
			
		}
		onClose();
	}

	public void onStart() {

	}

	public void onTick() {

	}

	public void onTimer() {

	}


	
	public void onClose() {
		
	}

	public void setTimer(long period) {
		if (period < refreshTime)
			return;
		this.period = period;
		nextPeriodEnd = System.currentTimeMillis() + period;
		timerIsSet = true;
	}

	private void calculateNextPeriodEnd() {
		nextPeriodEnd += period;
	}

	private boolean isEndOfPeriod() {
		if (timerIsSet)
			return System.currentTimeMillis() > nextPeriodEnd;
		return false;

	}

	public boolean timerIsSet() {
		return timerIsSet;
	}

	private void setTickRate(int hz) {
		refreshTime = hz;
	}

	public void stopThread() {
		execute = false;
	}

}
