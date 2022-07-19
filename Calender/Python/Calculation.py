from ast import And
import datetime

#Nutzung im Faktor möglich
FACTOR_VOTALITY_HIGH = 2.0
FACTOR_VOTALITY_MEDIUM = 1.0
#longShort = True    -> true bei long, false bei short
POP_TRADING_PAIRS = ["EUR/USD", "USD/JPY", "GBP/USD", "AUD/USD", "USD/CAD", "USD/CNY", "USD/CHF", "USD/HKD", "EUR/GBP", "USD/KRW"]



@staticmethod
def DateStringToObject(date_string):
    word = datetime.datetime.strptime(word, "%Y-%m-%dT%H:%M:%SZ")
    return word


@staticmethod
def breakTimer(EventTime):
    return EventTime - datetime.datetime.utcnow()

def longShort_by_name(event):
    #weitere Implementierung für die Bestimmung von LongShort möglich bei nicht-typischen Kursverhalten

    negativ_delta_names = ["Handelsbilanz"]
    
    minor_impact_names = ["Importe", "Exporte"]

def calculate_delta_actual_prev(event):
    actual = float(event["actual"])
    prev = float(event["previous"])
    return actual - prev



def longShort(event):
    
    longShort = event["isBetterThanExpected"]
    delta_act_prev = calculate_delta_actual_prev(event)
   
    if longShort == None and delta_act_prev > 0:
        longShort = True
    else:
        longShort = False
    return longShort




def calculate(event):
    actual = float(event["actual"])
    prev = float(event["previous"])
    consensus = event["consensus"]
    ratioDeviation = event["ratioDeviation"] if event["ratioDeviation"] is not None else 0
    isBetterThanExpected = event["isBetterThanExpected"]
    timedeltaFaktor = (breakTimer(DateStringToObject(event["dateUtc"]))/datetime.timedelta(hours=1))
    volatility = event["volatility"]
    volatity_factor =  FACTOR_VOTALITY_HIGH if (str(event["volatility"]) == "HIGH") else FACTOR_VOTALITY_MEDIUM
    


    #Berechnung anpassbar


    delta_act_prev = actual - prev
    delta_actprev_percent = (delta_act_prev/prev)/10    #Spannweite von 1000%

    zwischenergebnis1 = delta_actprev_percent * 5

    zwischenergebnis2 = abs(zwischenergebnis1)
    
    endfactor = (zwischenergebnis2 % 5) + volatity_factor

    return  endfactor
