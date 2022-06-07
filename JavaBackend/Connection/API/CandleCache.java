package API;

import java.util.ArrayList;
import java.util.HashMap;

import de.fhws.Softwareprojekt.JsonCandlesCandle;
import de.fhws.Softwareprojekt.JsonCandlesRoot;

public class CandleCache {

	HashMap<String, JsonCandlesRoot> cache = new HashMap<>();

	public JsonCandlesRoot get(String instrument, JsonCandlesCandle lastCandle) {
		JsonCandlesRoot output = cache.get(instrument);

		int lastIndex = output.candles.size() - 1;

		output.candles.set(lastIndex, lastCandle);
		return output;
	}

	public boolean needsUpdate(String instrument, JsonCandlesCandle jcc) {
		if(cache.containsKey(instrument)) return !getTimestamp(cache.get(instrument)).equals(getTimestamp(jcc));
		return true;
	}

	public void update(JsonCandlesRoot jcr) {
		cache.put(jcr.instrument, jcr);
	}

	public String getTimestamp(JsonCandlesRoot jcr) {
		int lastIndex = jcr.candles.size() - 1;
		return jcr.candles.get(lastIndex).time;
	}

	public String getTimestamp(JsonCandlesCandle jcc) {
		return jcc.time;
	}

	private class CandleContainer {
		JsonCandlesRoot jcr;
		JsonCandlesCandle lastCandle;
	}
}
