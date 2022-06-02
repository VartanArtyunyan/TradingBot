import datetime
from pickle import FALSE
import Connection
import JsonReader
import time
import EventRefresh

def DateStringToObject(word):
    word = datetime.datetime.strptime(word, "%Y-%m-%dT%H:%M:%SZ")
    return word

def calculate(event):
    print("success")

def breakTimer(EventTime):
    return EventTime - datetime.datetime.now()
    

def handleNextEvent(event):
    return event

def EventLoop(liste):
    list1 = liste
    for nextEvent in list1:
        print(nextEvent["name"])
        update = Connection.checkEvent(nextEvent)
        print(update["actual"])
        if update["actual"] is not None:
            calculate(nextEvent)
            list1.remove(nextEvent)
        return list1

        
        """ nextEventTime = DateStringToObject(nextEvent["dateUtc"])
        time.sleep(breakTimer(nextEventTime))
        timedelta = breakTimer(nextEventTime)
        print(timedelta.total_seconds()<0)
        print(f"{timedelta} :" + nextEvent["name"]) """

def filterSpeechAndReport(list):
    for nextEvent in list:
        if nextEvent["isSpeech"] or nextEvent["isReport"]:
            list.remove(nextEvent)
    return list


Connection.start()
JsonArray = JsonReader.read()
print(len(JsonArray))
JsonArray = filterSpeechAndReport(JsonArray)
print(len(JsonArray))

out = EventLoop(JsonArray)
print(len(out))


#while bool(JsonArray):

    #break 



""" 
    AktiveEvents = []
    while NOT bool(AktiveEvents):
    nextEventcheck -> 1+



 """




#del JsonArray[0]
#print(JsonArray)




""" while True:
    if nextEvent[1] == datetime.Datetime.now():
    calculate(nextEvent) """

