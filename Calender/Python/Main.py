import json
import time
import Connection
import JsonReader
import Calculation
import threading

    
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
            print(nextEvent["name"])
            update = Connection.checkEvent(nextEvent)
            print(update["actual"])
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


while (bool(a.data)):
    a.EventLoop()


exit()

""" with open('afterLoop.json', 'w') as f:
    json.dump(a.getData(), f) """


    




