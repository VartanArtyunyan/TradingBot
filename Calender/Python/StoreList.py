from dataclasses import dataclass
import json
#import Connection
import Calculation

class StoreList:

    def __init__(self, data):
        self.data = data
        print("jo")


    def filterSpeechAndReport(self):
        for nextEvent in self.data:
            if nextEvent["isSpeech"] or nextEvent["isReport"]:
                list.remove(nextEvent)

    def getData(self):
            return self.data


a = StoreList('hez')
print(a.data)




"""  def EventLoop(self):
        for nextEvent in self.data:
            update = Connection.checkEvent(nextEvent)
            if update["actual"] is not None:
                Calculation.calculate(nextEvent)
                list.remove(nextEvent) """
    
    
    

        
""" nextEventTime = DateStringToObject(nextEvent["dateUtc"])
        time.sleep(breakTimer(nextEventTime))
        timedelta = breakTimer(nextEventTime)
        print(timedelta.total_seconds()<0)
        print(f"{timedelta} :" + nextEvent["name"]) """
