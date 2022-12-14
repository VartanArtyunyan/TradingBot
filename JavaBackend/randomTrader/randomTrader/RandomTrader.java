package randomTrader;

import java.util.Date;

import Threads.StopableThread;
import positionen.Verwaltung;

public class RandomTrader extends StopableThread{
	
	Verwaltung verwaltung;
	
	public RandomTrader(Verwaltung verwaltung) {
		this.verwaltung = verwaltung;
		caclulateNextBuy();
	}
	@Override
	public void onTick() {
	
	}
	
	
	@Override
	public void onTimer() {
		verwaltung.pushRanomOrder(makeRandomOrder());
		caclulateNextBuy();
	}
	
	public void caclulateNextBuy() {
		long wait = (int) (Math.random()*6000);
		setTimer(wait);
	}
	
	
	
	public RandomOrder makeRandomOrder()	{
		return new RandomOrder(getRandomInstrument(), getRandomLongShort());
	}
	
	public String getRandomInstrument() {
		int range = verwaltung.getJsonInstrumentsRoot().instruments.size()-1;
		String output = "";
		boolean searchCurrency = true;
		while(searchCurrency) {
		int index = (int) (Math.random()*range);
		if(verwaltung.getJsonInstrumentsRoot().instruments.get(index).type.equals("CURRENCY")) {
			searchCurrency = false;
			output = verwaltung.getJsonInstrumentsRoot().instruments.get(index).name;
		}
		}
		
		return output;
	}
	
	public boolean getRandomLongShort(){
		int i  = (int)(Math.random()*2);
		return i == 1;
	}
	
	

}
