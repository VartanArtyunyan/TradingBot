import json
import time
import Connection
import JsonReader
import Calculation
import threading
#import Client as cl
    


INSTRUMENTS = '{"instrumente":["EUR/USD","GBP/CHF","EUR/JPY"]}'
#JSON_FORMAT =  f"instrument" + {instrument}, "factor" + {factor}, "volatility" + {volatility}, longShort + {longShort}}"

class StoreList:
    list_news = None
    list_pairs = None

    def __init__(self, list_news, list_pairs):
        self.list_news = list_news
        self.list_pairs = list_pairs

    def filterSpeechAndReport(self):
        for nextEvent in self.list_news:
            if nextEvent["isSpeech"] or nextEvent["isReport"]:
                self.list_news.remove(nextEvent)
                
    def EventLoop(self):
        for nextEvent in self.list_news:
            print(nextEvent["name"])
            update = Connection.checkEvent(nextEvent)
            print(update["actual"])
            if update["actual"] is not None:
                self.handleNextEvent(update)
                self.list_news.remove(nextEvent)
    
    @staticmethod
    def handleNextEvent(event):
        factor = Calculation.calculate(event)
        volatility = event["volatility"]
        longShort = True
        for handelspaar in list_pairs["instrumente"]:
            print(handelspaar)
            instrument = handelspaar
            print(f"instrument:{instrument},factor:{factor},volatility:{volatility},longShort:{longShort}")
            #sending = f"instrument:{instrument},factor:{factor},volatility:{volatility},longShort:{longShort}"
            #cl.send(sending)
                
    def getData(self):
            return self.list_news




#Connection.start()

list_news = JsonReader.read()


#list_pairs = json.load(cl.read())
list_pairs = json.loads(INSTRUMENTS)
#print(list_pairs["instrumente"][1])



a = StoreList(list_news, list_pairs)

#a.filterSpeechAndReport()



#while (bool(a.data)):
a.EventLoop()


#exit()

""" with open('afterLoop.json', 'w') as f:
    json.dump(a.getData(), f) """


    




