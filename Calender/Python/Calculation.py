from ast import And
import datetime


FACTOR_VOTALITY_HIGH = 2.0
FACTOR_VOTALITY_MEDIUM = 1.0
longShort = True    #true bei long, false bei short

#Nutzung im Faktor möglich
POP_TRADING_PAIRS = ["EUR/USD", "USD/JPY", "GBP/USD", "AUD/USD", "USD/CAD", "USD/CNY", "USD/CHF", "USD/HKD", "EUR/GBP", "USD/KRW"]


def longShort_by_name(event):
    #Negatives Vorher-Nachher wirkt sich negativ auf Kurs aus -> Kurs 
    negativ_delta_names = ["Handelsbilanz"]
    
    minor_impact_names = ["Importe", "Exporte"]


@staticmethod
def DateStringToObject(word):
    word = datetime.datetime.strptime(word, "%Y-%m-%dT%H:%M:%SZ")
    return word


@staticmethod
def breakTimer(EventTime):
    return EventTime - datetime.datetime.utcnow()


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


    
    #Alternative: trading_factor =  volatity_factor * (delta_actprev_percent + ratioDeviation) - timedeltaFaktor

    #return trading_factor

    """  if (isBetterThanExpected is not None) and (consensus is not None):
        delta_actual_consensus = actual - consensus

        #bte true?  actual > consensus? -> longShort = True
        #bte true?  actual < consensus? -> longShort = True

    else:
        delta_actprev_percent """

    #factor_string = "factor:" + str(endfactor)

    return  endfactor
