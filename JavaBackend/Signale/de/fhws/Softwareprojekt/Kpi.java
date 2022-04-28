package de.fhws.Softwareprojekt;

import java.util.ArrayList;
import java.util.List;

public class Kpi {
	public String instrument;
	public String granularity;
	public int periods;
	double ema = 0;
	double vorema=0;
	double macd=0;
	double macdTriggert=0;
	//Signalinie unterhalb macd:Nur relevant für die letzten zwei Durchläufe
	ArrayList<Double> macds=new ArrayList<>();
	ArrayList<Double> signals=new ArrayList<>();
	boolean trendWechsel=false;
double rsi=0;
	
	double lastPrice = 0;
	String firstTime = "";
	String lastTime = "";
	double min = 0;
	double max = 0;
	double avg = 0;
	double parabolicSAR =0;
	String trend="";
	ArrayList<Double>emas=new ArrayList<>();
	public Kpi(String instrument,String granularity,int periods)
	{
		this.instrument=instrument;
		this.granularity=granularity;
		this.periods=periods;
	}
}
