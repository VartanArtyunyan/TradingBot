import datetime
import json
import Connection
import JsonReader
import Calculation


def DateStringToObject(word):
    word = datetime.datetime.strptime(word, "%Y-%m-%dT%H:%M:%SZ")
    return word

def calculate(event):
    print("success")

def breakTimer(EventTime):
    return EventTime - datetime.datetime.now()
    

def handleNextEvent(event):
    return event


class StoreList:
    data = None
    
    def __init__(self, data):
        self.data = data


    def filterSpeechAndReport(self):
        for nextEvent in self.data:
            if nextEvent["isSpeech"] or nextEvent["isReport"]:
                self.data.remove(nextEvent)
                
    def EventLoop(self):
        for nextEvent in self.data:
            update = Connection.checkEvent(nextEvent)
            if update["actual"] is not None:
                Calculation.calculate(nextEvent)
                self.data.remove(nextEvent)

    def getData(self):
            return self.data


#Connection.start()

a = StoreList(JsonReader.read())
print(len(a.getData()))
a.filterSpeechAndReport()
print(len(a.getData()))
a.EventLoop()
print(len(a.getData()))

with open('afterLoop.json', 'w') as f:
    json.dump(a.getData(), f)

""" while bool(List):
    myl.EventLoop(List)
    
    

exit()  """



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

