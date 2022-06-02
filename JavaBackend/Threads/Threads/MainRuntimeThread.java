package Threads;

import positionen.Verwaltung;

public class MainRuntimeThread extends stopableThread{
	
	Verwaltung verwaltung;
	
	public MainRuntimeThread(Verwaltung verwaltung) {
		
		this.verwaltung = verwaltung;
		
	}
	
	public void run(){
		while(execute) {
			verwaltung.onTick();
		}
	}

}
