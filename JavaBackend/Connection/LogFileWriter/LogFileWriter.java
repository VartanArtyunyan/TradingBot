package LogFileWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import PyhtonConnection.WebInterfaceConnection;
import Threads.StopableThread;
import de.fhws.Softwareprojekt.Kpi;
import positionen.Verwaltung;
import positionen.trade;

public class LogFileWriter extends StopableThread implements Closeable {

	BufferedWriter bw;
	String inputPath;
	String path;
	WebInterfaceConnection webInterfaceConnection;
	Verwaltung verwaltung;
	String header = "ID;Instrument;last Time;Kaufpreis;last Price;TakeProfit;StopLoss;macd;macdTrigger;parabolicSAR;ema200;SMA20;SMA50;ATR;RSI;VerkaufsPreis\n";

	String notSoldText = "Noch Nicht Verkauft";

	ArrayList<Integer> lastCheckedOpenTradeIDs;

	ArrayList<Integer> loggedOpenTradeIDs;


	public LogFileWriter(Verwaltung verwaltung, WebInterfaceConnection webInterfaceConnection) {
		this.verwaltung = verwaltung;
		this.webInterfaceConnection = webInterfaceConnection;
		initialise("siganls.csv");
	}

	public LogFileWriter(Verwaltung verwaltung, WebInterfaceConnection webInterfaceConnection, String path) {
		this.verwaltung = verwaltung;
		this.webInterfaceConnection = webInterfaceConnection;
		initialise(path);
	}

	private void initialise(String path) {
		this.inputPath = path;
		this.path = inputPath;

		openFile(path);

		readFile();

		setTimer(2000);

	}

	@Override
	public void onTimer() {
		if (postionsHaveChanged()) {
			ArrayList<trade> trades = verwaltung.getTrades(getMissingIDs());

			for (trade t : trades) {
				if (tradeIsSold(t)) {

					addSellingPrice(t.getId(), t.getRealizedPl());

				}
			}

		}
	}

	private boolean tradeIsSold(trade t) {
		return t.getRealizedPl() == 0;
	}

	private ArrayList<Integer> getMissingIDs() {
		return loggedOpenTradeIDs;
	}

	public void logSignal(String orderID,double buyingPrice, Kpi kpi) {
		int id = Integer.parseInt(orderID);
		addOpenTradeId(id);
		webInterfaceConnection.pushSignal(id,buyingPrice, kpi);
	}

	public void addSellingPrice(int id, double realizedPL) {
		webInterfaceConnection.addSellingPrice(id, realizedPL);
		int index = loggedOpenTradeIDs.indexOf(id);
		loggedOpenTradeIDs.remove(index);
	}

	private void addOpenTradeId(int id) {
		loggedOpenTradeIDs.add(id);
	}

	public boolean postionsHaveChanged() {
		ArrayList<Integer> newTrades = verwaltung.getTradeIDs();
		boolean output = !newTrades.equals(lastCheckedOpenTradeIDs);
		lastCheckedOpenTradeIDs = newTrades;
		return output;
	}

	@Override
	public void close() throws IOException {
		bw.close();
	}

	public void flush() throws IOException {
		bw.flush();
	}

	private void readFile() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {

			loggedOpenTradeIDs = new ArrayList<>();
			String input = br.readLine();
			String[] ids = input.split(";");

			for (int i = 0; i < ids.length; i++) {

				loggedOpenTradeIDs.add(Integer.parseInt(ids[i]));

			}

			br.close();

		} catch (FileNotFoundException e) {
			loggedOpenTradeIDs = new ArrayList<>();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void openFile(String path) {
		boolean retry = true;
		int postfix = 1;
		do {
			try {
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true)));
				retry = false;
			} catch (FileNotFoundException e) {
				path = addPostfix(inputPath, Integer.toString(postfix));
				postfix++;
			}
		} while (retry);
	}

	public String addPostfix(String path, String postfix) {
		int index = path.indexOf('.');
		return path.substring(0, index) + postfix + path.substring(index);
	}

}
