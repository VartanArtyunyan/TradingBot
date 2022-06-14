package LogFileWriter;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LogFileWriter implements Closeable {

	BufferedWriter bw;
	String inputPath;
	String path;
	boolean writeHeader;
	String header = "ID;Instrument;last Time;Kaufpreis;last Price;TakeProfit;StopLoss;macd;macdTrigger;parabolicSAR;ema200;SMA20;SMA50;ATR;RSI;VerkaufsPreis\n";

	LogFile file;

	public LogFileWriter() {
		initialise("siganls.csv");
	}

	public LogFileWriter(String path) {
		initialise(path);
	}

	public ArrayList<Integer> getMissingIDs(){
		return file.getMissingIDs();
	}

	public void log(String id, String instrument, String lastTime, double kaufpreis, double lastPrice, double takeProfit, double stopLoss,
			double macd, double macdTriggert, double parabolicSAR, double ema200, double sma20, double sma50, double ATR, double RSI) {
		String input = String.format("%s;%s;%s;%f;%f;%f;%f;%f;%f;%f;%f",id, instrument, lastTime, kaufpreis, lastPrice, takeProfit,
				stopLoss, macd, macdTriggert, parabolicSAR, ema200);
		try {	
			bw.write(input + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws IOException {
	
		bw.close();
	}
	
	public void flush() throws IOException {
	
		bw.flush();
	}

	private void initialise(String path) {
		this.inputPath = path;
		this.path = inputPath;
		

		writeHeader = !Files.exists(Paths.get(path));
		this.file = new LogFile(path);
		
		
		//openFile(path);


		if (writeHeader) {
			try {
				bw.write(header);
				bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void openFile(String path) {
		boolean retry = true;
		int postfix = 1;
		do {
			try {
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
				retry = false;
			} catch (FileNotFoundException e) {
				path = addPostfix(inputPath, Integer.toString(postfix));
				writeHeader = !Files.exists(Paths.get(path));
				postfix++;
			}
		} while (retry);
	}

	public String addPostfix(String path, String postfix) {
		int index = path.indexOf('.');
		return path.substring(0, index) + postfix + path.substring(index);
	}

}
