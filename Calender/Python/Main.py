import datetime
from pickle import FALSE
import Connection
import JsonReader
import time


def DateStringToObject(word):
    word = datetime.datetime.strptime(word, "%Y-%m-%dT%H:%M:%SZ")
    return word

def calculate(event):
    return event

def breakTimer(EventTime):
    return EventTime - datetime.datetime.now()
    

def handleNextEvent(event):
    """erfolgreich = FALSE
            print("Hey") """

Connection.start()
JsonArray = JsonReader.read()
AktiveEvents = []







#del JsonArray[0]
#print(JsonArray)

for nextEvent in JsonArray:
    nextEventTime = DateStringToObject(nextEvent["dateUtc"])
    
    #time.sleep(breakTimer(nextEventTime))

    timedelta = breakTimer(nextEventTime)
    print(timedelta.total_seconds()<0)
    print(f"{timedelta} :" + nextEvent["name"])
    handleNextEvent(nextEvent)


""" while True:
    if nextEvent[1] == datetime.Datetime.now():
    calculate(nextEvent) """

