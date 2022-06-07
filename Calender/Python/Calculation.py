import datetime


FACTOR_VOTALITY_HIGH = 2
FACTOR_VOTALITY_MEDIUM = 1
longShort = True    #true bei long, false bei short


@staticmethod
def DateStringToObject(word):
    word = datetime.datetime.strptime(word, "%Y-%m-%dT%H:%M:%SZ")
    return word

@staticmethod
def breakTimer(EventTime):
    return EventTime - datetime.datetime.now()

def calculate(event):
    actual = event["actual"]
    previous = event["previous"]
    delta_act_prev = previous - actual
    ratioDeviation = event["ratioDeviation"]
    isBetterThanExpected = event["isBetterThanExpected"]
    timedeltaFaktor = (breakTimer(DateStringToObject(event["dateUtc"]))/datetime.timedelta(hours=1))*0.5
   
    factor = delta_act_prev 