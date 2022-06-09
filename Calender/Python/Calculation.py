import datetime


FACTOR_VOTALITY_HIGH = 2.0
FACTOR_VOTALITY_MEDIUM = 1.0
longShort = True    #true bei long, false bei short





@staticmethod
def DateStringToObject(word):
    word = datetime.datetime.strptime(word, "%Y-%m-%dT%H:%M:%SZ")
    return word


def calc_longShort():
    return 0


@staticmethod
def breakTimer(EventTime):
    return EventTime - datetime.datetime.now()


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

    back_string = f",factor:{endfactor},volatility:{volatility},longShort:{longShort}"

    return  back_string

