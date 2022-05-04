package de.fhws.Softwareprojekt;

import java.util.ArrayList;

public class JsonInstrumentsInstrument {
	public String name;
    public String type;
    public String displayName;
    public int pipLocation;
    public int displayPrecision;
    public int tradeUnitsPrecision;
    public String minimumTradeSize;
    public String maximumTrailingStopDistance;
    public String minimumTrailingStopDistance;
    public String maximumPositionSize;
    public String maximumOrderUnits;
    public String marginRate;
    public String guaranteedStopLossOrderMode;
	public ArrayList<JsonInstrumentsTag> tags;
    public JsonInstrumentsFinancing financing;
    public String minimumGuaranteedStopLossDistance;
    public String guaranteedStopLossOrderExecutionPremium;
    public JsonInstrumentsGuaranteedSLOLR guaranteedStopLossOrderLevelRestriction;
}
