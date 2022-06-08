package randomTrader;

import java.util.Date;

import Threads.stopableThread;
import positionen.Verwaltung;

public class randomTrader extends stopableThread{
	
	Verwaltung verwaltung;
	long nextBuy;
	
	public randomTrader(Verwaltung verwaltung) {
		this.verwaltung = verwaltung;
		nextBuy = System.currentTimeMillis();
		
		
	}
	
	public void onTick() {
		if(timeToBuy()) {
			verwaltung.pushRanomOrder(makeRandomOrder());
			caclulateNextBuy();
		}
	
		
	}
	
	public void caclulateNextBuy() {
		long time = System.currentTimeMillis();
		int wait = (int) (Math.random()*300000);
		long nextBuy = time + wait;
	}
	
	public boolean timeToBuy()	{
		return System.currentTimeMillis() > nextBuy;
	}
	
	public RandomOrder makeRandomOrder()	{
		return new RandomOrder(getRandomInstrument(), getRandomLongShort());
	}
	
	public String getRandomInstrument() {
		int range = verwaltung.getJsonInstrumentsRoot().instruments.size()-1;
		int index = (int) (Math.random()*range);
		return verwaltung.getJsonInstrumentsRoot().instruments.get(index).name;
		
	}
	
	public boolean getRandomLongShort(){
		int i  = (int)(Math.random()*2);
		return i == 1;
	}
	
	

}
