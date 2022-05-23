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

public class LogFileWriter implements Closeable{

	BufferedWriter bw;
	LogBuffer lBuffer;
	String header = "Instrument;last Time;last Price;TakeProfit;StopLoss;macd;macdTrigger;parabolicSAR;ema\n";

	public LogFileWriter() {
		initialise("siganls.csv");
	}

	public LogFileWriter(String path) {
		initialise(path);
	}

	private void initialise(String path) {

		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		lBuffer = new LogBuffer(path, bw);

		if (!Files.exists(Paths.get(path))) {
			try {
				bw.write(header);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void log(String instrument, String lastTime, double lastPrice, double takeProfit, double stopLoss,
			double macd, double macdTriggert, double parabolicSAR, double ema) {
		String input = String.format("%s;%s;%f;%f;%f;%f;%f;%f;%f", instrument, lastTime, lastPrice, takeProfit, stopLoss, macd, macdTriggert, parabolicSAR, ema);
		try {
			lBuffer.flush();
			bw.write(input);
		} catch (IOException e) {
			e.printStackTrace();
			lBuffer.put(input);
		}
	}

	@Override
	public void close() throws IOException {
		lBuffer.close();
		bw.close();
	}


	
	

}
