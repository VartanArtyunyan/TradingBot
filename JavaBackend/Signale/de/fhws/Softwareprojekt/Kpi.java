package de.fhws.Softwareprojekt;

import java.util.ArrayList;
import java.util.List;

public class Kpi {
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
	
ArrayList<Double> superTrends=new ArrayList<>();
double superTrend=0;
	boolean trendWechsel=false;
double rsi=0;
ArrayList<Double>rsiListe=new ArrayList<>();	
	double lastPrice = 0;
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
}
