import datetime
from pickle import FALSE
import Connection
import JsonReader
import time


def DateTimeSplitter(word):
    word = word[0:len(word)-1]
    word = word.split('T')
    return word

def calculate(event):
    return event

def breakTimer(event):
    time_1 = datetime.datetime.strptime(event,"%H:%M:%S")
    time_2 = datetime.datetime.strptime(datetime.datetime.now().strftime("%H:%M:%S"),"%H:%M:%S")
    time_interval = time_1 - time_2
    return time_interval

def timeInSeconds(date):
    arr = date.split(":")
    return (arr[0] * 3600 + arr[1] * 60 + arr[2])

def handleNextEvent(event):
    erfolgreich = FALSE
    while not erfolgreich:




#Connection.start()
JsonArray = JsonReader.read()


for nextEvent in JsonArray:
    nextEventTime = DateTimeSplitter(nextEvent["dateUtc"])
    print(str(breakTimer(nextEventTime[1])) + " : " + nextEvent["name"])
    handleNextEvent(nextEvent)

    #time.sleep(breakTimer(nextEventTime))
    """ while True:
        if nextEvent[1] == datetime.Datetime.now():
        calculate(nextEvent) """

