package Threads;

<<<<<<< HEAD:JavaBackend/Threads/Threads/stopableThread.java
public class stopableThread extends Thread {

	boolean execute;
	long period;
	long nextPeriodEnd;
	boolean timerIsSet;
	int refreshTime;

	public stopableThread() {
		setTickRate(240);
		execute = true;
		timerIsSet = false;
	}

	public stopableThread(int tickrate) {

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
			try {
				this.sleep(refreshTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
=======
public class StopableThread extends Thread{
>>>>>>> a9f838a13199b95f56a1e48b29bf434dc4df030f:JavaBackend/Threads/Threads/StopableThread.java
	
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
		refreshTime = 0;
	}

	public void stopThread() {
		execute = false;
	}

}
