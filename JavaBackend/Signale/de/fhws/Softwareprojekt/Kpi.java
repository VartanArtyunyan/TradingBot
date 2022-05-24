package de.fhws.Softwareprojekt;

import java.util.ArrayList;
import java.util.List;

public class Kpi{
	
	boolean longShort;   //false = short, long = true
	
	JsonCandlesRoot root;
	public String instrument;
	public String granularity;
	public int periods;
	double atr=0;
	ArrayList<Double> atrListe=new ArrayList<>();
	double ema = 0;
	double vorema=0;
	double macd=0;
	ArrayList<Double>macds=new ArrayList<>();
    ArrayList<Double> macdsTriggert=new ArrayList<>();
	double macdTriggert=0;
	double sma=0;
	ArrayList<Double>smaList =new ArrayList<>();
ArrayList<Double> superTrends=new ArrayList<>();
double superTrend=0;
	boolean trendWechsel=false;
double rsi=0;
ArrayList<Double>rsiListe=new ArrayList<>();	
	double lastPrice = 0;
	double lastHighestPrice=0;
	double lastLowestPrice=0;
	String firstTime = "";
	String lastTime = "";
	double min = 0;
	double max = 0;
	double avg = 0;
	double parabolicSAR =0;
    ArrayList<Double>parabolicSARs=new ArrayList<>();
	String trend="";
	ArrayList<Double>emas=new ArrayList<>();
	public Kpi(String instrument,String granularity,int periods)
	{
		this.instrument=instrument;
		this.granularity=granularity;
		this.periods=periods;
	}
	
	public double getKaufpreis() {
		return 0.0;  // müsste eigentlich 0,02*kontostand zurückgeben
	}
	
	public double getLongStopLoss() {
		return parabolicSAR;
	}
	
	public double getLongTakeProfit() {
		return lastPrice + (lastPrice - parabolicSAR)*2;
	}
	
	public double getShortStopLoss() {
		return parabolicSAR;
	}
	
	public double getShortTakeProfit() {
		return lastPrice - (parabolicSAR - lastPrice)*2;
	}
	
	@Override
	public boolean equals(Object o) {
		
		Kpi input = (Kpi) o;
		return this.instrument.equals(input.instrument);
		
	}
	@Override
	public int hashCode() {
		
		return this.instrument.hashCode();
	}

	

	
}
