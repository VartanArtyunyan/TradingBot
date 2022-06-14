package positionen;

public class trade {

	
	
	
	
	public trade(int id, String instrument, double price, String openTime, int initialUnits,
			String initialMarginRequired, int currentunits, String realizedPL, String unrealizedPL, String marginUsed
			) {
		super();
		this.id = id;
		this.instrument = instrument;
		this.price = price;
		this.openTime = openTime;
		this.initialUnits = initialUnits;
		this.initialMarginRequired = initialMarginRequired;
		this.currentunits = currentunits;
		this.realizedPL = realizedPL;
		this.unrealizedPL = unrealizedPL;
		this.marginUsed = marginUsed;
	
	}

	int id;
	
	String instrument;
	double price; //kaufpreis
	String openTime;
	
	int initialUnits;
	String initialMarginRequired;//Kredit welcher benötigt wird um den trade auszuführen
	int currentunits;// aktuelle einheiten
	
	String realizedPL;
	String unrealizedPL;
	
	String marginUsed;
	String averageClosePrice;
	String[] closingTransactionIDs;
	String financing;
	String dividendAdjustment;
	String closeTime;
	
	public int getId() {
		return id;
	}
	
	public String getInstrument() {
		return instrument;
	}
	
	
	public double getPrice() {
		return price;
	}
	
	public int getUnits() {
		return currentunits;
	}
	
	public double getRealizedPl()	{
		return Double.parseDouble(realizedPL);
	}
	
	public String toString() {
		String output = "";
		
		output+= "id: "+id;
		output+= ", ";
		output+= "instrument: "+ instrument;
		output+= ", ";
		output+= "price: " + price;
		output+= ", ";
		output+= "initialUnits: " + initialUnits;
		
		return output;
	}
}
